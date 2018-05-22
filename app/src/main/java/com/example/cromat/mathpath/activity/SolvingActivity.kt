package com.example.cromat.mathpath.activity

import android.annotation.SuppressLint
import android.app.Activity
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
import com.example.cromat.mathpath.model.Equation
import com.example.cromat.mathpath.model.EquationConfig
import com.example.cromat.mathpath.view.GoldView
import kotlinx.android.synthetic.main.activity_solving.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
class SolvingActivity : AppCompatActivity() {
    private var score: Int = 0
    private var taskNum: Int = 1
    private var equationConfig: EquationConfig = EquationConfig()
    private var equation = Equation(equationConfig)
    private val listAnswers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solving)
        equationConfig = intent.getSerializableExtra("equationConfig") as EquationConfig

        // Initialize SolvingActivity view
        initView()

        // Create first equation
        nextEquation()

        // Next
        when (equationConfig.gameType) {
            GameType.STEPS.toString() -> {
                setStepGameType()
            }
            GameType.TIME.toString() -> {
                setTimeGameType()
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

    private fun initView() {
        val goldCurrent = DbHelper.getGoldValue(applicationContext).toString()
        goldViewSolving.text = goldCurrent

        layoutSolving.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            edtViewAnswer.requestFocus()
        }

        val gameTypeStr = getString(R.string.game_type)
        val gameTypeValStr = getString(resources.getIdentifier(equationConfig.gameType,
                "string", packageName))
        solvingTitle.text = gameTypeStr.plus(": ").plus(gameTypeValStr)
    }

    private fun setStepGameType() {
        stepperText.text = taskNum.toString() + "/" + equationConfig.stepsNum.toString()
        progressBarSolving.max = equationConfig.stepsNum
        btnNext.setOnClickListener {
            stepGame()
        }
    }

    private fun setTimeGameType() {
        stepperText.visibility = View.GONE
        timerText.visibility = View.VISIBLE
        progressBarSolving.max = equationConfig.timeSec
        timerText.text = equationConfig.timeSec.toString()
        startTimer()
        btnNext.setOnClickListener {
            timeGame()
        }
    }

    private fun nextEquation() {
        equation = Equation(equationConfig)
        if (equationConfig.randomizeInput) {
            val strings = equation.splitAtOperandIndex()
            txtViewEquationFirst.text = strings[0].replace("/", "\u00F7")
                    .replace("*", "\u02E3")
            txtViewEquationSecond.text = strings[1].replace("/", "\u00F7")
                    .replace("*", "\u02E3")
        } else {
            val userEquation = equation.toString().replace("/", "\u00F7")
                    .replace("*", "\u02E3")
            txtViewEquationFirst.text = "$userEquation="
        }
    }

    private fun stepGame() {
        stepperText.text = (taskNum + 1).toString() + "/" + equationConfig.stepsNum.toString()
        val userAns = edtViewAnswer.text.toString()
        checkAnswer(userAns)

        if (taskNum < equationConfig.stepsNum) {
            edtViewAnswer.text = SpannableStringBuilder("")
            nextEquation()
            taskNum++
        } else {
            stepperText.visibility = View.GONE
            setSolvingFinishedView()
        }
        progressBarSolving.incrementProgressBy(1)
    }

    private fun timeGame() {
        val userAns = edtViewAnswer.text.toString()
        checkAnswer(userAns)
        edtViewAnswer.text = SpannableStringBuilder("")
        nextEquation()
        taskNum++
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer((equationConfig.timeSec * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / 1000).toString()
                progressBarSolving.incrementProgressBy(1)
            }
            override fun onFinish() {
                timerText.text = "0"
                setSolvingFinishedView()
            }
        }.start()
    }

    private fun setSolvingFinishedView() {
        edtViewAnswer.visibility = View.GONE
        txtViewEquationSecond.visibility = View.GONE
        btnNext.text = getString(R.string.finish)
        txtViewEquationFirst.text = "${getString(R.string.your_score)} $score/${equationConfig.stepsNum}"
        textShowAnswers.visibility = View.VISIBLE
        btnNext.setOnClickListener {
            finishSolving()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun finishSolving() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDateTime = dateFormat.format(Date()).toString()
        DbHelper.insertResult(score, currentDateTime, equationConfig.stepsNum,
                GameType.STEPS.toString(), applicationContext)
        DbHelper.addGold(score, applicationContext)
        goldViewSolving.text = DbHelper.getGoldValue(applicationContext).toString()
        finish()
    }

    private fun checkAnswer(userAns: String) {
        val values: MutableMap<String, Int> = mutableMapOf("+" to 0, "-" to 0, "/" to 0, "*" to 0)

        var addVal = +1
        if (equation.isCorrect(userAns)) {
            score++
            addVal = -1
        }

        listAnswers.add("${equation.toUserEquationString()};$userAns;${equation.getRightAns()}")

        for (key in values.keys) {
            if (equation.toString().contains(key))
                values[key] = values[key]!! + addVal
        }

        DbHelper.updateOperations(values, applicationContext)
    }
}
