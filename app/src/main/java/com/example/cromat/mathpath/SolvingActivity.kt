package com.example.cromat.mathpath
import java.util.Random


import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SolvingActivity : AppCompatActivity() {

    val MAX_NUM_FIELDS : Int = 5
    val MIN_NUM_FIELDS: Int = 2
    val OPERATORS = listOf("+", "-", "*")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solving)
    }

    fun generateEquation() : String {
        var equation : String = ""
        val rand = Random()

        for (i in 0..rand.nextInt(MAX_NUM_FIELDS - MIN_NUM_FIELDS) + MIN_NUM_FIELDS){
            equation += i.toChar() + OPERATORS[rand.nextInt(OPERATORS.size)]
        }

        equation = equation.dropLast(1)
        return equation
    }

    fun generateTextView(text : String) {

    }
}
