package com.example.cromat.mathpath

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*


class DbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MathPath", null, 1) {
    companion object {
        val TABLE_RESULT = "result"
        val TABLE_OPERATIONS = "operations"
        val TABLE_GOLD = "gold"

        private var instance: DbHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DbHelper {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            return instance!!
        }

        fun updateGold(value: Int, ctx: Context) {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            instance!!.use {
                execSQL("UPDATE " + TABLE_GOLD + " SET value = value + " + value.toString())
            }
        }

        fun getGoldValue(ctx: Context): Int {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            return instance!!.use {
                select(TABLE_GOLD, "value").whereArgs("id == 0").exec { parseSingle(IntParser) }
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

        fun updateOperations(values: MutableMap<String, Int>, ctx: Context){
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }

            val valuesStr = mutableMapOf<String, String>()
            for (key in values.keys){
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
        db.createTable(TABLE_RESULT, true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "date" to TEXT,
                "score" to INTEGER,
                "numAns" to INTEGER,
                "gameType" to TEXT
        )

        // Operations statistics tracking by solving table
        db.createTable(TABLE_OPERATIONS, true,
                "id" to INTEGER + PRIMARY_KEY + SqlTypeModifier.create("CHECK (id = 0)"),
                "plus" to INTEGER + DEFAULT("0"),
                "minus" to INTEGER + DEFAULT("0"),
                "divide" to INTEGER + DEFAULT("0"),
                "multiple" to INTEGER + DEFAULT("0")
        )

        // Gold
        db.createTable(TABLE_GOLD, true,
                "id" to INTEGER + PRIMARY_KEY + SqlTypeModifier.create("CHECK (id = 0)"),
                "value" to INTEGER + DEFAULT("0")
        )

        // Default gold value to 0
        db.execSQL("INSERT INTO $TABLE_GOLD (id, value) VALUES(0, 0)")

        // Default operations value to 0
        db.execSQL("INSERT INTO $TABLE_OPERATIONS (id, plus, minus, divide, multiple) " +
                "VALUES(0, 0, 0, 0, 0)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(TABLE_RESULT, true)
    }

}

val Context.database: DbHelper
    get() = DbHelper.getInstance(applicationContext)
