package com.example.cromat.mathpath.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.enums.GameType
import com.example.cromat.mathpath.model.EquationConfig
import kotlinx.android.synthetic.main.activity_custom_game.*
import me.bendik.simplerangeview.SimpleRangeView
import org.jetbrains.annotations.NotNull
import android.graphics.Typeface.createFromAsset
import android.support.v4.content.res.ResourcesCompat


class CustomGameActivity : BgMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_game)

        // RangeView listeners
        rangeNumbers.onTrackRangeListener = (object : SimpleRangeView.OnTrackRangeListener {
            override fun onStartRangeChanged(@NotNull rangeView: SimpleRangeView, start: Int) {
                editRangeNumStart.setText(start.toString())
            }

            override fun onEndRangeChanged(@NotNull rangeView: SimpleRangeView, end: Int) {
                editRangeNumEnd.setText(end.toString())
            }
        })

        rangeOperands.onTrackRangeListener = (object : SimpleRangeView.OnTrackRangeListener {
            override fun onStartRangeChanged(@NotNull rangeView: SimpleRangeView, start: Int) {
                textRangeOperStart.text = (start + 2).toString()
            }

            override fun onEndRangeChanged(@NotNull rangeView: SimpleRangeView, end: Int) {
                textRangeOperEnd.text = (end + 2).toString()
            }
        })

        // Checkboxes font family
        val font = ResourcesCompat.getFont(this, R.font.im_wunderland_cro)
        checkRandomInputs.typeface = font
        checkNegativeRes.typeface = font
        checkFixedNumOperands.typeface = font

        // Radio buttons listeners
        radioSteps.setOnClickListener {
            editTime.visibility = View.GONE
            textTime.visibility = View.GONE
            editSteps.visibility = View.VISIBLE
            textSteps.visibility = View.VISIBLE
        }

        radioTime.setOnClickListener {
            editSteps.visibility = View.GONE
            textSteps.visibility = View.GONE
            editTime.visibility = View.VISIBLE
            textTime.visibility = View.VISIBLE
        }

        radioNumOperands.check(radioBtnOperNum2.id)

        //  Number operands checkbox
        checkFixedNumOperands.setOnClickListener {
            if (checkFixedNumOperands.isChecked) {
                rangeOperands.visibility = View.GONE
                textRangeOperStart.visibility = View.GONE
                textRangeOperEnd.visibility = View.GONE
                radioNumOperands.visibility = View.VISIBLE
            } else {
                rangeOperands.visibility = View.VISIBLE
                textRangeOperStart.visibility = View.VISIBLE
                textRangeOperEnd.visibility = View.VISIBLE
                radioNumOperands.visibility = View.GONE
            }
        }

        // Start
        btnCustomStart.setOnClickListener {
            if (validateFields()) {
                var maxNumOperands = textRangeOperEnd.text.toString().toInt()
                var minNumOperands = textRangeOperStart.text.toString().toInt()

                if (checkFixedNumOperands.isChecked) {
                    val selectedId = radioNumOperands.checkedRadioButtonId
                    val selectedRadioBtn = findViewById<View>(selectedId) as RadioButton
                    maxNumOperands = selectedRadioBtn.text.toString().toInt()
                    minNumOperands = selectedRadioBtn.text.toString().toInt()
                }

                val maxNum = editRangeNumEnd.text.toString().toInt()
                val minNum = editRangeNumStart.text.toString().toInt()
                val operators = ArrayList<String>()

                (0 until relativeCheckboxes.childCount)
                        .map { relativeCheckboxes.getChildAt(it) as CheckBox }
                        .filter { it.isChecked }
                        .mapTo(operators) {
                            when {
                                it.text == getString(R.string.divide) -> "/"
                                it.text == getString(R.string.multiple) -> "*"
                                else -> it.text.toString()
                            }
                        }

                var gameType = GameType.STEPS.toString()
                if (radioTime.isChecked)
                    gameType = GameType.TIME.toString()
                val timeSec = editTime.text.toString().toInt()
                val stepsNum = editSteps.text.toString().toInt()
                val negativeRes = checkNegativeRes.isChecked
                val randomizeInput = checkRandomInputs.isChecked

                val equationConfig = EquationConfig(maxNumOperands, minNumOperands, maxNum,
                        minNum, operators, gameType, timeSec, stepsNum, negativeRes,
                        randomizeInput)
                val intent = Intent(applicationContext, SolvingActivity::class.java)
                intent.putExtra("equationConfig", equationConfig)
                startActivity(intent)
            }
        }
    }

    private fun validateFields(): Boolean {
        // Steps validation
        if (radioSteps.isChecked && editSteps.text.toString().toInt() < 1) {
            Toast.makeText(applicationContext,
                    applicationContext.resources.getString(R.string.valid_steps),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        // Time validation
        if (radioTime.isChecked && editTime.text.toString().toInt() < 10) {
            Toast.makeText(applicationContext,
                    applicationContext.resources.getString(R.string.valid_time),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        // Operators validation
        var atLeastOneChecked = false
        for (i in 0 until relativeCheckboxes.childCount) {
            val checkBox = relativeCheckboxes.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                atLeastOneChecked = true
                break
            }
        }

        if (!atLeastOneChecked) {
            Toast.makeText(applicationContext,
                    applicationContext.resources.getString(R.string.valid_operators),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        // Number range validation
        if (editRangeNumStart.text.toString().toIntOrNull() == null ||
                editRangeNumEnd.text.toString().toIntOrNull() == null) {
            Toast.makeText(applicationContext,
                    applicationContext.resources.getString(R.string.valid_range),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        if (editRangeNumStart.text.toString().toInt() < rangeNumbers.startFixed ||
                editRangeNumEnd.text.toString().toInt() > rangeNumbers.endFixed) {
            Toast.makeText(applicationContext,
                    applicationContext.resources.getString(R.string.valid_range),
                    Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
