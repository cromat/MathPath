package com.example.cromat.mathpath

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.cromat.mathpath.model.PetItem
import org.jetbrains.anko.db.*
import java.util.*


class DbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MathPath", null, 1) {
    companion object {
        const val TABLE_RESULT = "result"
        const val TABLE_OPERATIONS = "operations"
        const val TABLE_PET_ITEMS = "pet_items"
        const val TABLE_GOLD = "gold"
        const val TABLE_HAPPINESS = "happiness"
        private var instance: DbHelper? = null

        private val parserPetItem = rowParser { id: Int, name: String, price: Int, permanent: Int, activated: Int, bought: Int,
                                                picture: Int, bindedElementId: Int, happiness: Int ->
            PetItem(name, price, permanent.toBoolean(), bought.toBoolean(), activated.toBoolean(),
                    picture, bindedElementId, happiness)
        }

        private val parserOperatorsPercentage = rowParser { plus: Long?, minus: Long?, divide: Long?,
                                                            multiple: Long? ->
            listOf(plus.toFiftyIfNull(), minus.toFiftyIfNull(), divide.toFiftyIfNull(),
                    multiple.toFiftyIfNull())
        }

        private fun Long?.toFiftyIfNull() = this?.toInt() ?: 50

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

        fun addHappiness(value: Int, ctx: Context): Boolean {
            var addValue = value
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

            val currHappiness = instance!!.use {
                select(TABLE_HAPPINESS, "value").whereArgs("id == 0").exec { parseSingle(IntParser) }
            }

            if (currHappiness + addValue > 100)
                addValue = 100 - currHappiness

            instance!!.use {
                execSQL("UPDATE " + TABLE_HAPPINESS + " SET value = value + " + addValue.toString())
            }
            return true
        }

        fun getGoldValue(ctx: Context): Int {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            return instance!!.use {
                select(TABLE_GOLD, "value").whereArgs("id = 0").exec { parseSingle(IntParser) }
            }
        }

        fun getHapiness(ctx: Context): Int {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

            val lastHappines = instance!!.use {
                select(TABLE_HAPPINESS, "value").whereArgs("id = 0").exec { parseSingle(IntParser) }
            }

            var remainder = instance!!.use {
                select(TABLE_HAPPINESS, "remainder").whereArgs("id = 0").exec { parseSingle(IntParser) }
            }

            val curDateTimeMillis = Calendar.getInstance().timeInMillis
            val lastDateTimeMillis = instance!!.use {
                select(TABLE_HAPPINESS, "dateTimeMillis").whereArgs("id = 0").exec { parseSingle(StringParser) }
            }.toLong()

            val deltaTimeMillis = curDateTimeMillis - lastDateTimeMillis + remainder
            val minusHappines = deltaTimeMillis / 360000
            remainder = deltaTimeMillis.toInt() % 360000
            var currHappiness = (lastHappines - minusHappines).toInt()

            if (currHappiness < 0)
                currHappiness = 0

            instance!!.use {
                execSQL("UPDATE $TABLE_HAPPINESS SET value = $currHappiness, dateTimeMillis = " +
                        "'$curDateTimeMillis', remainder = $remainder")
            }

            return currHappiness
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

        fun updatePetItem(petItem: PetItem, ctx: Context) {
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
                        ", happiness = " + petItem.happiness.toString() +
                        " WHERE name = '" + petItem.name + "'"
                )
            }
        }

        fun addOperations(values: MutableMap<String, Int>, ctx: Context) {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

//            val valuesStr = mutableMapOf<String, String>()
//            for (key in values.keys) {
//                if (values[key]!! < 0)
//                    valuesStr[key] = values[key].toString()
//            }

            instance!!.use {
                execSQL("INSERT INTO $TABLE_OPERATIONS (plus, minus, divide, multiple) " +
                        "VALUES(" + values["+"].toString() + ","
                        + values["-"].toString() + ","
                        + values["/"].toString() + ","
                        + values["*"].toString() + ");")
            }
        }

        fun getPercentageByOperation(ctx: Context): List<Int> {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

            var percentageList = listOf(0)

            instance!!.use {
                percentageList = select(TABLE_OPERATIONS,
                        "CAST((AVG(NULLIF(plus, 0)) + 1)/0.02 AS INTEGER) AS plus",
                        "CAST((AVG(NULLIF(minus, 0)) + 1)/0.02 AS INTEGER) AS minus",
                        "CAST((AVG(NULLIF(divide, 0)) + 1)/0.02 AS INTEGER) AS divide",
                        "CAST((AVG(NULLIF(multiple, 0)) + 1)/0.02 AS INTEGER) AS multiple")
                        .exec { parseSingle(parserOperatorsPercentage) }
            }
            return percentageList
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val dateTimeMillis = Calendar.getInstance().timeInMillis
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
                    "id" to INTEGER + PRIMARY_KEY,
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
                    "bindedElementId" to INTEGER + DEFAULT("0"),
                    "happiness" to INTEGER + DEFAULT("0")
            )


            // Gold
            createTable(TABLE_GOLD, true,
                    "id" to INTEGER + PRIMARY_KEY + SqlTypeModifier.create("CHECK (id = 0)"),
                    "value" to INTEGER + DEFAULT("0")
            )

            // Happiness
            createTable(TABLE_HAPPINESS, true,
                    "id" to INTEGER + PRIMARY_KEY + SqlTypeModifier.create("CHECK (id = 0)"),
                    "value" to INTEGER + DEFAULT("100"),
                    "dateTimeMillis" to TEXT + DEFAULT(dateTimeMillis.toString()),
                    "remainder" to INTEGER + DEFAULT("0")
            )

            // Default gold value to 0
            execSQL("INSERT INTO $TABLE_GOLD (id, value) VALUES(0, 0)")

            // Default happiness value to 80 and millis to now
            execSQL("INSERT INTO $TABLE_HAPPINESS (id, value, dateTimeMillis, remainder) " +
                    "VALUES(0, 80, '$dateTimeMillis', 0)")

            // Default operations value to 0
            execSQL("INSERT INTO $TABLE_OPERATIONS (id, plus, minus, divide, multiple) " +
                    "VALUES(0, 0, 0, 0, 0)")

            // Default pet items
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId, happiness) VALUES('Drink', 5, 0, 0, 0, " +
                    R.drawable.drink.toString() + ", 0, 10)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId, happiness) VALUES('Ice Cream', 7, 0, 0, 0, " +
                    R.drawable.icecream.toString() + ", 0, 15)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId, happiness) VALUES('Food', 10, 0, 0, 0, " +
                    R.drawable.food.toString() + ", 0, 20)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId, happiness) VALUES('Ball', 25, 1, 0, 0, " +
                    R.drawable.ball.toString() + ", " + R.id.imagePetBall.toString() + ", 30)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId, happiness) VALUES('Hat', 30, 1, 0, 0, " +
                    R.drawable.hat.toString() + ", " + R.id.imagePetHat.toString() + ", 30)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId, happiness) VALUES('Shirt', 50, 1, 0, 0, " +
                    R.drawable.shirt.toString() + ", " + R.id.imagePetShirt.toString() + ", 50)")
            execSQL("INSERT INTO $TABLE_PET_ITEMS (name, price, permanent, activated, " +
                    "bought, picture, bindedElementId, happiness) VALUES('Car', 75, 1, 0, 0, " +
                    R.drawable.car.toString() + ", " + R.id.imagePetCar.toString() + ", 50)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(TABLE_RESULT, true)
    }
}

val Context.database: DbHelper
    get() = DbHelper.getInstance(applicationContext)
