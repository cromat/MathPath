package com.example.cromat.mathpath

import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.view.View
import kotlinx.android.synthetic.main.activity_solving.*
import org.mvel2.MVEL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SolvingActivity : AppCompatActivity() {

    private var SCORE : Int= 0
    private var TASK_NUM : Int = 1
    private var RIGHT_ANS : String = ""
    private var equationConfig: EquationConfig = EquationConfig()
    private val listAnswers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solving)

//        val prefs = this.defaultSharedPreferences
        equationConfig  = intent.getSerializableExtra("equationConfig") as EquationConfig
        solvingTitle.text = "Game Type: " + getString(resources.getIdentifier(equationConfig.GAME_TYPE,
                "string", packageName));

        // Create first equation
        nextEquation()

        // Next
        when (equationConfig.GAME_TYPE){
            GameType.STEPS.toString() -> {
                btnNext.setOnClickListener {
                    stepGame()
                }
            }

            GameType.TIME.toString() -> {
                timerText.visibility = View.VISIBLE
                progressBarSolving.max = equationConfig.TIME_SEC
                timerText.text = equationConfig.TIME_SEC.toString()
                startTimer()
                btnNext.setOnClickListener {
                    timeGame()
                }
            }
        }

        // Quit
        btnQuit.setOnClickListener {
            finish()
        }

        // Show Answers
        textShowAnswers.setOnClickListener {
            val intent = Intent(applicationContext, AnswerListActivity::class.java)
            intent.putStringArrayListExtra("listAnswers", listAnswers)
            startActivity(intent)
        }
    }

    private fun generateEquation() : String {
        var equation : String = ""
        val rand = Random()

        val RAND_NUM_OPERANDS = rand.nextInt(equationConfig.MAX_NUM_OPERANDS - equationConfig.MIN_NUM_OPERANDS) +
                equationConfig.MIN_NUM_OPERANDS

        for (i in 0..RAND_NUM_OPERANDS){
            val RAND_NUM = rand.nextInt(equationConfig.MAX_NUM - equationConfig.MIN_NUM) + equationConfig.MIN_NUM
            equation += RAND_NUM.toString() + equationConfig.OPERATORS[rand.nextInt(equationConfig.OPERATORS.size)]
        }

        equation = equation.dropLast(1)
        return equation
    }

    private fun nextEquation() {
        val equationText = generateEquation()
        txtViewEquation.text = equationText + "="
        RIGHT_ANS = MVEL.eval(equationText).toString()
    }

    private fun stepGame(){
        if (TASK_NUM < equationConfig.STEPS_NUM) {
            val userAns = edtViewAnswer.text.toString()

            if (userAns == RIGHT_ANS)
                SCORE++

//            listAnswers.add(Answer(txtViewEquation.text.toString(), userAns, RIGHT_ANS))
            listAnswers.add(txtViewEquation.text.toString() + ";" + userAns + ";" + RIGHT_ANS)

//            listAnswers.add("blaa")
            edtViewAnswer.text = SpannableStringBuilder("")
            nextEquation()
            TASK_NUM++
        }
        else {
            edtViewAnswer.visibility = View.INVISIBLE
            btnNext.text = "FINISH"
            txtViewEquation.text = "Your score: " + SCORE.toString() + "/" + equationConfig.STEPS_NUM.toString()
            textShowAnswers.visibility = View.VISIBLE
            btnNext.setOnClickListener(View.OnClickListener {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val currentDateTime = dateFormat.format(Date()).toString()

                val values = ContentValues()
                values.put("score", SCORE)
                values.put("date", currentDateTime)
                values.put("numAns", equationConfig.STEPS_NUM)
                database.use {
                    insert(DbHelper.TABLE_RESULT, null, values)
                }
                finish()
            })
        }

        progressBarSolving.incrementProgressBy(10)
    }

    private fun timeGame() {
        val userAns = edtViewAnswer.text.toString()
        if (userAns == RIGHT_ANS)
            SCORE++

//        listAnswers.add(Answer(txtViewEquation.text.toString(), userAns, RIGHT_ANS))
        listAnswers.add(txtViewEquation.text.toString() + ";" + userAns + ";" + RIGHT_ANS)


//        listAnswers.add("beee")
        edtViewAnswer.text = SpannableStringBuilder("")
        nextEquation()
        TASK_NUM++
    }

    private fun startTimer(){
        val countDownTimer = object: CountDownTimer((equationConfig.TIME_SEC * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / 1000).toString()
                progressBarSolving.incrementProgressBy(1)
            }

            override fun onFinish() {
                timerText.text = "0"
                edtViewAnswer.visibility = View.INVISIBLE
                btnNext.text = "FINISH"
                txtViewEquation.text = "Your score: " + SCORE.toString() + "/" + (TASK_NUM - 1).toString()
                btnNext.setOnClickListener {

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val currentDateTime = dateFormat.format(Date()).toString()

                    val values = ContentValues()
                    values.put("score", SCORE)
                    values.put("date", currentDateTime)
                    values.put("numAns", TASK_NUM - 1)
                    database.use {
                        insert(DbHelper.TABLE_RESULT, null, values)
                    }
                    finish()
                }
            }
        }.start()
    }
}
