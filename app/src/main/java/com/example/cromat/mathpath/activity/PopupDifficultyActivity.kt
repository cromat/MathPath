package com.example.cromat.mathpath.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.enums.GameType
import com.example.cromat.mathpath.model.EquationConfig
import kotlinx.android.synthetic.main.activity_popup_difficulty.*

class PopupDifficultyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_difficulty)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width: Int = dm.widthPixels
        val height: Int = dm.heightPixels

        val intent = Intent()
        setResult(1, intent)

        window.setLayout((width * .8).toInt(), (height * .7).toInt())

        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params

        // Buttons
        btnCustom.setOnClickListener {
            startActivity(Intent(applicationContext, CustomGameActivity::class.java))
            finish()
        }

        btnEasy.setOnClickListener {
            val equationConfig = EquationConfig(
                    MAX_NUM_OPERANDS = 2,
                    MIN_NUM_OPERANDS = 2,
                    MAX_NUM = 10,
                    MIN_NUM = 0,
                    OPERATORS = listOf<String>("+", "-"),
                    GAME_TYPE = GameType.STEPS.toString(),
                    TIME_SEC = 60,
                    STEPS_NUM = 10,
                    NEGATIVE_RES = false,
                    RANDOMIZE_INPUT = false
            )
            val intent = Intent(applicationContext, SolvingActivity::class.java)
            intent.putExtra("equationConfig", equationConfig)
            startActivity(intent)
            finish()
        }

        btnMedium.setOnClickListener {
            val equationConfig = EquationConfig(
                    MAX_NUM_OPERANDS = 3,
                    MIN_NUM_OPERANDS = 2,
                    MAX_NUM = 20,
                    MIN_NUM = 0,
                    OPERATORS = listOf<String>("+", "-", "*", "/"),
                    GAME_TYPE = GameType.STEPS.toString(),
                    TIME_SEC = 60,
                    STEPS_NUM = 15,
                    NEGATIVE_RES = false,
                    RANDOMIZE_INPUT = true
            )
            val intent = Intent(applicationContext, SolvingActivity::class.java)
            intent.putExtra("equationConfig", equationConfig)
            startActivity(intent)
            finish()
        }

        btnHard.setOnClickListener {
            val equationConfig = EquationConfig(
                    MAX_NUM_OPERANDS = 5,
                    MIN_NUM_OPERANDS = 3,
                    MAX_NUM = 100,
                    MIN_NUM = 0,
                    OPERATORS = listOf<String>("+", "-", "*", "/"),
                    GAME_TYPE = GameType.STEPS.toString(),
                    TIME_SEC = 60,
                    STEPS_NUM = 20,
                    NEGATIVE_RES = true,
                    RANDOMIZE_INPUT = true
            )
            val intent = Intent(applicationContext, SolvingActivity::class.java)
            intent.putExtra("equationConfig", equationConfig)
            startActivity(intent)
            finish()
        }
    }
}
