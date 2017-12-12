package com.example.cromat.mathpath

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*


class DbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MathPath", null, 1) {
    companion object {
        val TABLE_RESULT = "result"
        private var instance: DbHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DbHelper {
            if (instance == null) {
                instance = DbHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Results table
        db.createTable(TABLE_RESULT, true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "date" to TEXT,
                "score" to INTEGER,
                "numAns" to INTEGER,
                "gameType" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(TABLE_RESULT, true)
    }
}

val Context.database: DbHelper
    get() = DbHelper.getInstance(getApplicationContext())
