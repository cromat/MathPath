package com.example.cromat.mathpath

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.cromat.mathpath.model.PetItem
import org.jetbrains.anko.db.*


class DbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MathPath", null, 1) {
    companion object {
        const val TABLE_RESULT = "result"
        const val TABLE_OPERATIONS = "operations"
        const val TABLE_PET_ITEMS = "pet_items"
        const val TABLE_GOLD = "gold"
        private var instance: DbHelper? = null

        private val parserPetItem = rowParser {
            id: Int, name: String, price : Int, permanent : Int, activated: Int, bought: Int,
            picture : Int, bindedElementId : Int ->
            PetItem(name, price, permanent.toBoolean(), bought.toBoolean(), activated.toBoolean(),
                    picture, bindedElementId)
        }

        private fun Int.toBoolean() = this != 0

        private fun Boolean.toInt() = if (this) 1 else 0

        @Synchronized
        fun getInstance(ctx: Context): DbHelper {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            return instance!!
        }

        fun addGold(value: Int, ctx: Context): Boolean {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

            val currGold = instance!!.use {
                select(TABLE_GOLD, "value").whereArgs("id == 0").exec { parseSingle(IntParser) }
            }

            if (currGold + value < 0)
                return false

            instance!!.use {
                execSQL("UPDATE " + TABLE_GOLD + " SET value = value + " + value.toString())
            }
            return true
        }

        fun getGoldValue(ctx: Context): Int {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            return instance!!.use {
                select(TABLE_GOLD, "value").whereArgs("id == 0").exec { parseSingle(IntParser) }
            }
        }

        fun getPetItems(ctx: Context): List<PetItem> {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            return instance!!.use {
                select(TABLE_PET_ITEMS).exec { parseList(parserPetItem) }
            }
        }

        fun insertResult(score: Int, dateTime: String, numAns: Int, gameType: String, ctx: Context) {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

            val values = ContentValues()
            values.put("score", score)
            values.put("date", dateTime)
            values.put("numAns", numAns)
            values.put("gameType", gameType)

            instance!!.use {
                insert(DbHelper.TABLE_RESULT, null, values)
            }
        }

        fun updatePetItem(petItem: PetItem, ctx: Context){
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            instance!!.use {
                execSQL("UPDATE " + TABLE_PET_ITEMS +
                " SET price = " + petItem.price.toString() +
                ", permanent = " + petItem.permanent.toInt().toString() +
                ", activated = " + petItem.activated.toInt().toString() +
                ", bought = " + petItem.bought.toInt().toString() +
                ", picture = " + petItem.picture.toString() +
                ", bindedElementId = " + petItem.bindedElementId.toString() +
                " WHERE name = '" + petItem.name + "'"
                )
            }
        }


        fun updateOperations(values: MutableMap<String, Int>, ctx: Context) {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

            val valuesStr = mutableMapOf<String, String>()
            for (key in values.keys) {
                if (values[key]!! < 0)
                    valuesStr[key] = "-" + values[key].toString()
                else
                    valuesStr[key] = "+" + values[key].toString()
            }

            instance!!.use {
                execSQL("UPDATE " + TABLE_OPERATIONS +
                        " SET plus = plus " + valuesStr["+"] +
                        ", minus = minus " + valuesStr["-"] +
                        ", divide = divide " + valuesStr["/"] +
                        ", multiple = multiple " + valuesStr["*"]
                )
                execSQL("UPDATE $TABLE_OPERATIONS SET plus = 0 WHERE plus < 0")
                execSQL("UPDATE $TABLE_OPERATIONS SET minus = 0 WHERE minus < 0")
                execSQL("UPDATE $TABLE_OPERATIONS SET divide = 0 WHERE divide < 0")
                execSQL("UPDATE $TABLE_OPERATIONS SET multiple = 0 WHERE multiple < 0")
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Results
        db.run {
            createTable(TABLE_RESULT, true,
                    "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                    "date" to TEXT,
                    "score" to INTEGER,
                    "numAns" to INTEGER,
                    "gameType" to TEXT
            )

            // Operations statistics tracking by solving table
            createTable(TABLE_OPERATIONS, true,
                    "id" to INTEGER + PRIMARY_KEY + SqlTypeModifier.create("CHECK (id = 0)"),
                    "plus" to INTEGER + DEFAULT("0"),
                    "minus" to INTEGER + DEFAULT("0"),
                    "divide" to INTEGER + DEFAULT("0"),
                    "multiple" to INTEGER + DEFAULT("0")
            )

            // Pet Item
            createTable(TABLE_PET_ITEMS, true,
                    "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                    "name" to TEXT + DEFAULT("0"),
                    "price" to INTEGER + DEFAULT("0"),
                    "permanent" to INTEGER + DEFAULT("0"),
                    "activated" to INTEGER + DEFAULT("0"),
                    "bought" to INTEGER + DEFAULT("0"),
                    "picture" to INTEGER + DEFAULT("0"),
                    "bindedElementId" to INTEGER + DEFAULT("0")
            )


            // Gold
            createTable(TABLE_GOLD, true,
                    "id" to INTEGER + PRIMARY_KEY + SqlTypeModifier.create("CHECK (id = 0)"),
                    "value" to INTEGER + DEFAULT("0")
            )

            // Default gold value to 0
            execSQL("INSERT INTO $TABLE_GOLD (id, value) VALUES(0, 0)")

            // Default operations value to 0
            execSQL("INSERT INTO $TABLE_OPERATIONS (id, plus, minus, divide, multiple) " +
                    "VALUES(0, 0, 0, 0, 0)")

            // Default pet items
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId) VALUES('Drink', 5, 0, 0, 0, " +
                    R.drawable.drink.toString() + ", 0)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId) VALUES('Food', 10, 0, 0, 0, " +
                    R.drawable.food.toString() + ", 0)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId) VALUES('Ball', 25, 1, 0, 0, " +
                    R.drawable.ball.toString() + ", " + R.id.imagePetBall.toString() + ")")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId) VALUES('Shirt', 50, 1, 0, 0, " +
                    R.drawable.shirt.toString() + ", " + R.id.imagePetShirt.toString() + ")")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(TABLE_RESULT, true)
    }
}

val Context.database: DbHelper
    get() = DbHelper.getInstance(applicationContext)
