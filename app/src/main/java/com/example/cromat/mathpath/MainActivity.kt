package com.example.cromat.mathpath

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import me.bendik.simplerangeview.SimpleRangeView
import org.jetbrains.annotations.NotNull
import android.preference.PreferenceActivity
import android.widget.RadioButton
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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
                textRangeOperStart.setText((start + 2).toString())
            }

            override fun onEndRangeChanged(@NotNull rangeView: SimpleRangeView, end: Int) {
                textRangeOperEnd.setText((end + 2).toString())
            }
        })

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
            if(checkFixedNumOperands.isChecked){
                rangeOperands.visibility = View.GONE
                textRangeOperStart.visibility = View.GONE
                textRangeOperEnd.visibility = View.GONE
                radioNumOperands.visibility = View.VISIBLE
            }
            else{
                rangeOperands.visibility = View.VISIBLE
                textRangeOperStart.visibility = View.VISIBLE
                textRangeOperEnd.visibility = View.VISIBLE
                radioNumOperands.visibility = View.GONE
            }
        }

        // Open Db and create tables if not existing
        DbHelper(applicationContext)
        database.onUpgrade(database.writableDatabase, 1, 2)

        database.onCreate(database.writableDatabase)
        // Start
        btnStart.setOnClickListener {

            if(validateFields()) {

                var MAX_NUM_OPERANDS = textRangeOperEnd.text.toString().toInt()
                var MIN_NUM_OPERANDS = textRangeOperStart.text.toString().toInt()

                if (checkFixedNumOperands.isChecked) {
                    val selectedId = radioNumOperands.getCheckedRadioButtonId()
                    val selectedRadioBtn = findViewById<View>(selectedId) as RadioButton
                    MAX_NUM_OPERANDS = selectedRadioBtn.text.toString().toInt()
                    MIN_NUM_OPERANDS = selectedRadioBtn.text.toString().toInt()
                }

                val MAX_NUM = editRangeNumEnd.text.toString().toInt()
                val MIN_NUM = editRangeNumStart.text.toString().toInt()
                val OPERATORS = ArrayList<String>()

                (0 until relativeCheckboxes.childCount)
                        .map { relativeCheckboxes.getChildAt(it) as CheckBox }
                        .filter { it.isChecked }
                        .mapTo(OPERATORS) { it.text.toString() }

                var GAME_TYPE = GameType.STEPS.toString()
                if (radioTime.isChecked)
                    GAME_TYPE = GameType.TIME.toString()
                val TIME_SEC = editTime.text.toString().toInt()
                val STEPS_NUM = editSteps.text.toString().toInt()

                val equationConfig = EquationConfig(MAX_NUM_OPERANDS, MIN_NUM_OPERANDS, MAX_NUM,
                        MIN_NUM, OPERATORS, GAME_TYPE, TIME_SEC, STEPS_NUM)
                val intent = Intent(applicationContext, SolvingActivity::class.java)
                intent.putExtra("equationConfig", equationConfig)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment::class.java!!.getName())
                intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
                startActivity(intent)
            }
            R.id.action_stats -> {
                startActivity(Intent(applicationContext, GraphActivity::class.java))
            }
        }
        return true
    }

    fun validateFields(): Boolean{
        // Steps validation
        if(radioSteps.isChecked && editSteps.text.toString().toInt() < 1){
            Toast.makeText(applicationContext,
                    applicationContext.getResources().getString(R.string.valid_steps),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        // Time validation
        if(radioTime.isChecked && editTime.text.toString().toInt() < 10){
            Toast.makeText(applicationContext,
                    applicationContext.getResources().getString(R.string.valid_time),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        // Operators validation
        var atLeastOneChecked = false
        for (i in 0 until relativeCheckboxes.childCount) {
            val checkBox = relativeCheckboxes.getChildAt(i) as CheckBox
            if(checkBox.isChecked) {
                atLeastOneChecked = true
                break
            }
        }

        if(!atLeastOneChecked){
            Toast.makeText(applicationContext,
                    applicationContext.getResources().getString(R.string.valid_operators),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        // Number range validation
        if(editRangeNumStart.text.toString().toIntOrNull() == null ||
                editRangeNumEnd.text.toString().toIntOrNull() == null){
            Toast.makeText(applicationContext,
                    applicationContext.getResources().getString(R.string.valid_range),
                    Toast.LENGTH_SHORT).show()
            return false
        }

        if(editRangeNumStart.text.toString().toInt() < rangeNumbers.startFixed ||
                editRangeNumEnd.text.toString().toInt() > rangeNumbers.endFixed){
            Toast.makeText(applicationContext,
                    applicationContext.getResources().getString(R.string.valid_range),
                    Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
