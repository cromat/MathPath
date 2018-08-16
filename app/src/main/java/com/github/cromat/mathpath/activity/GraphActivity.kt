package com.github.cromat.mathpath.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.github.cromat.mathpath.DbHelper
import com.github.cromat.mathpath.R
import com.github.cromat.mathpath.database
import com.github.cromat.mathpath.enums.GameType
import com.github.cromat.mathpath.enums.Range
import com.github.cromat.mathpath.model.Result
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.activity_graph.*
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GraphActivity : BgMusicActivity() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val parser = rowParser { id: Int, date: String, score: Int, numAns: Int, gameType: String ->
        Result(id, dateFormat.parse(date), score, numAns, gameType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        getPieChart(Range.WEEK.toString(), GameType.STEPS.toString())
        setPercentageByOperators()
    }

    private fun getPieChart(range: String, gameType: String) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val results = database.use {
            select(DbHelper.TABLE_RESULT).exec { parseList(parser) }
        }

        val sumRight = results.sumBy { result -> result.score }
        val sumBad = results.sumBy { result -> result.numAns } - sumRight
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(sumRight.toFloat(), getString(R.string.right)))
        entries.add(PieEntry(sumBad.toFloat(), getString(R.string.bad)))

        val dataSet = PieDataSet(entries, "")
        dataSet.valueTextSize = 12f
        dataSet.setColors(ContextCompat.getColor(this, R.color.colorBtnGreen),
                ContextCompat.getColor(this, R.color.colorBtnRed))
        val pieData = PieData(dataSet)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = getString(R.string.answers)
        pieChart.setBackgroundColor(Color.parseColor("#eeeeee"))
        pieChart.setCenterTextSize(18f)
        pieChart.setCenterTextColor(Color.GRAY)
        pieChart.invalidate()
    }

    @SuppressLint("SetTextI18n")
    private fun setPercentageByOperators(){
        val perByOperators = DbHelper.getPercentageByOperation(this)
        progressPlus.progress = perByOperators[0]
        progressMinus.progress = perByOperators[1]
        progressDivide.progress = perByOperators[2]
        progressMultiple.progress = perByOperators[3]

        textPercentPlus.text = perByOperators[0].toString() + "%"
        textPercentMinus.text = perByOperators[1].toString() + "%"
        textPercentDivide.text = perByOperators[2].toString() + "%"
        textPercentMultiple.text = perByOperators[3].toString() + "%"


        when (perByOperators[0]) {
            in 66..100 -> {
                progressPlus.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBgGreen),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 34..65 -> {
                progressPlus.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnYellow),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 0..33 -> {
                progressPlus.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)

            }
        }

        when (perByOperators[1]) {
            in 66..100 -> {
                progressMinus.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBgGreen),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 34..65 -> {
                progressMinus.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnYellow),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 0..33 -> {
                progressMinus.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)

            }
        }

        when (perByOperators[2]) {
            in 66..100 -> {
                progressDivide.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBgGreen),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 34..65 -> {
                progressDivide.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnYellow),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 0..33 -> {
                progressDivide.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)

            }
        }

        when (perByOperators[3]) {
            in 66..100 -> {
                progressMultiple.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBgGreen),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 34..65 -> {
                progressMultiple.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnYellow),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            in 0..33 -> {
                progressMultiple.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)

            }
        }

    }
}
