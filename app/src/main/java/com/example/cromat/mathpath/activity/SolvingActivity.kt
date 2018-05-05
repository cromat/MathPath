package com.example.cromat.mathpath.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.cromat.mathpath.*
import com.example.cromat.mathpath.enums.GameType
import com.example.cromat.mathpath.fragment.GoldFragment
import com.example.cromat.mathpath.model.EquationConfig
import kotlinx.android.synthetic.main.activity_solving.*
import kotlinx.android.synthetic.main.fragment_gold.*
import org.mvel2.MVEL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SolvingActivity : AppCompatActivity() {

    private var SCORE: Int = 0
    private var TASK_NUM: Int = 1
    private var RIGHT_ANS: String = ""
    private var equationConfig: EquationConfig = EquationConfig()
    private val listAnswers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solving)

        relativeSolvingContainer.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            edtViewAnswer.requestFocus()
        }

//        val prefs = this.defaultSharedPreferences
        equationConfig = intent.getSerializableExtra("equationConfig") as EquationConfig
        val gameTypeStr = getString(R.string.game_type)
        val gameTypeValStr = getString(resources.getIdentifier(equationConfig.GAME_TYPE,
                "string", packageName))
        solvingTitle.text = gameTypeStr.plus(": ").plus(gameTypeValStr)


                getString(R.string.game_type) + ": " +
                getString(resources.getIdentifier(equationConfig.GAME_TYPE,
                        "string", packageName))

        // Create first equation
        nextEquation()

        // Next
        when (equationConfig.GAME_TYPE) {
            GameType.STEPS.toString() -> {
                stepperText.text = TASK_NUM.toString() + "/" + equationConfig.STEPS_NUM.toString()
                progressBarSolving.max = equationConfig.STEPS_NUM
                btnNext.setOnClickListener {
                    stepGame()
                }
            }

            GameType.TIME.toString() -> {
                stepperText.visibility = View.GONE
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

    private fun generateEquation(): String {
        var equation: String = ""
        val rand = Random()

        var RAND_NUM_OPERANDS = equationConfig.MAX_NUM_OPERANDS
        if (equationConfig.MAX_NUM_OPERANDS > equationConfig.MIN_NUM_OPERANDS) {
            RAND_NUM_OPERANDS = rand.nextInt(equationConfig.MAX_NUM_OPERANDS + 1 - equationConfig.MIN_NUM_OPERANDS) +
                    equationConfig.MIN_NUM_OPERANDS
        }

        var OPERATOR = ""
        var RAND_NUM = 0
        for (i in 0 until RAND_NUM_OPERANDS) {
            if (OPERATOR == "/") {
                val lastNum = RAND_NUM
                RAND_NUM = 1
                if (lastNum > 1) {
                    for (j in 2 until lastNum) {
                        if (lastNum % j == 0) {
                            RAND_NUM = j
                            break
                        }
                    }
                }
            } else {
                RAND_NUM = rand.nextInt(equationConfig.MAX_NUM - equationConfig.MIN_NUM) + equationConfig.MIN_NUM
            }
            OPERATOR = equationConfig.OPERATORS[rand.nextInt(equationConfig.OPERATORS.size)]
            equation += RAND_NUM.toString() + OPERATOR
        }

        equation = equation.dropLast(1)
        return equation
    }

    private fun nextEquation() {
        val equationText = generateEquation()
        txtViewEquation.text = equationText + "="
        val evaluated: Double = MVEL.eval(equationText + ".0") as Double
        val rightAnsInt = evaluated.toInt()

        if (!equationConfig.NEGATIVE_RES && rightAnsInt < 0)
            nextEquation()
        else
            RIGHT_ANS = rightAnsInt.toString()
    }

    private fun stepGame() {
        stepperText.text = (TASK_NUM + 1).toString() + "/" + equationConfig.STEPS_NUM.toString()
        val userAns = edtViewAnswer.text.toString()

        if (userAns == RIGHT_ANS)
            SCORE++

        listAnswers.add(txtViewEquation.text.toString() + ";" + userAns + ";" + RIGHT_ANS)
        if (TASK_NUM < equationConfig.STEPS_NUM) {

            edtViewAnswer.text = SpannableStringBuilder("")
            nextEquation()
            TASK_NUM++
        } else {
            stepperText.visibility = View.GONE
            edtViewAnswer.visibility = View.INVISIBLE
            btnNext.text = "FINISH"
            txtViewEquation.text = "Your score: " + SCORE.toString() + "/" + equationConfig.STEPS_NUM.toString()
            textShowAnswers.visibility = View.VISIBLE
            btnNext.setOnClickListener {
                finishSolving()
            }
        }

        progressBarSolving.incrementProgressBy(1)
    }

    private fun timeGame() {
        val userAns = edtViewAnswer.text.toString()
        if (userAns == RIGHT_ANS)
            SCORE++

        listAnswers.add(txtViewEquation.text.toString() + ";" + userAns + ";" + RIGHT_ANS)
        edtViewAnswer.text = SpannableStringBuilder("")
        nextEquation()
        TASK_NUM++
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer((equationConfig.TIME_SEC * 1000).toLong(), 1000) {

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
                    finishSolving()
                }
            }
        }.start()
    }

    private fun finishSolving(){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDateTime = dateFormat.format(Date()).toString()
        DbHelper.insertResult(SCORE, currentDateTime, equationConfig.STEPS_NUM,
                GameType.STEPS.toString(), applicationContext)
        DbHelper.updateGold(SCORE, applicationContext)
        textGold.text = DbHelper.getGoldValue(applicationContext).toString()
        finish()
    }
}
