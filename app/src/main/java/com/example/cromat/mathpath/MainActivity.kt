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
                editRangeOperStart.setText((start + 2).toString())
            }

            override fun onEndRangeChanged(@NotNull rangeView: SimpleRangeView, end: Int) {
                editRangeOperEnd.setText((end + 2).toString())
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

        // Open Db and create tables if not existing
        DbHelper(applicationContext)

        // Start
        btnStart.setOnClickListener {
            val MAX_NUM_OPERANDS = editRangeOperEnd.text.toString().toInt()
            val MIN_NUM_OPERANDS = editRangeOperStart.text.toString().toInt()
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
        }
        return true
    }

}
