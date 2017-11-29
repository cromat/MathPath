package com.example.cromat.mathpath

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_solving.*
import org.mvel2.MVEL
import java.text.SimpleDateFormat
import java.util.*

class SolvingActivity : AppCompatActivity() {

    private val MAX_NUM_FIELDS : Int = 5
    private val MIN_NUM_FIELDS: Int = 2
    private val MAX_NUM : Int = 10
    private val OPERATORS = listOf("+", "-", "*")
    private var SCORE : Int= 0
    private var TASK_NUM : Int = 1
    private var RIGHT_ANS : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solving)

        val checkedCateg = intent.getStringExtra("checkedCateg")
        nextEquation()

        // Next
        btnNext.setOnClickListener(View.OnClickListener {
            if (TASK_NUM < 10) {
                val userAns = edtViewAnswer.text.toString()

                if (userAns == RIGHT_ANS)
                    SCORE++

                edtViewAnswer.text = SpannableStringBuilder("")
                nextEquation()
                TASK_NUM++
            }
            else {
                edtViewAnswer.visibility = View.INVISIBLE
                btnNext.text = "FINISH"
                txtViewEquation.text = "Your score: " + SCORE.toString() + "/10"
                btnNext.setOnClickListener(View.OnClickListener {

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val currentDateTime = dateFormat.format(Date()).toString()

                    Toast.makeText(applicationContext, currentDateTime, Toast.LENGTH_SHORT).show()

                    val values = ContentValues()
                    values.put("score", SCORE)
                    values.put("date", currentDateTime)
                    database.use {
                        insert(DbHelper.TABLE_RESULT, null, values)
                    }
                    finish()
                })
            }
            progressBarSolving.incrementProgressBy(10)
        })

        // Quit
        btnQuit.setOnClickListener(View.OnClickListener {
            finish()
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

    private fun nextEquation() {
        val equationText = generateEquation()
        txtViewEquation.text = equationText + "="
        RIGHT_ANS = MVEL.eval(equationText).toString()
    }
}
