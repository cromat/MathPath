package com.example.cromat.mathpath

import java.util.Random
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_solving.*
import org.mvel2.MVEL

class SolvingActivity : AppCompatActivity() {

    private val MAX_NUM_FIELDS : Int = 5
    private val MIN_NUM_FIELDS: Int = 2
    private val MAX_NUM : Int = 10
    private val OPERATORS = listOf("+", "-", "*")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solving)

        val equationText = generateEquation()
        txtViewEquation.text = equationText + "="
        val rightAns = MVEL.eval(equationText).toString()

        btnNext.setOnClickListener(View.OnClickListener {
            val userAns = edtViewAnswer.text.toString()
            if (rightAns == userAns)
                Toast.makeText(applicationContext, "RIGHT!", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(applicationContext, "BAD ANSWER", Toast.LENGTH_SHORT).show()
        })
    }

    private fun generateEquation() : String {
        var equation : String = ""
        val rand = Random()

        for (i in 0..rand.nextInt(MAX_NUM_FIELDS - MIN_NUM_FIELDS) + MIN_NUM_FIELDS){
            equation += rand.nextInt(MAX_NUM).toString() + OPERATORS[rand.nextInt(OPERATORS.size)]
        }

        equation = equation.dropLast(1)
        return equation
    }
}
