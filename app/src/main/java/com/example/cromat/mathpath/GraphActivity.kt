package com.example.cromat.mathpath

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.activity_graph.*
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import kotlin.collections.HashMap
import android.widget.ArrayAdapter




class GraphActivity : AppCompatActivity() {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateFormatCroShort = SimpleDateFormat("dd.MM.")
    val parser = rowParser {
        id: Int, date: String, score: Int, numAns: Int, gameType: String ->
        Result(id, dateFormat.parse(date), score, numAns, gameType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val adapterRangeSpinner = ArrayAdapter.createFromResource(this,
                R.array.graph_range_array, android.R.layout.simple_spinner_dropdown_item)
        adapterRangeSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        spinnerGraphRange.adapter = adapterRangeSpinner
        spinnerGraphRange.setSelection(0)

        val adapterTypeSpinner = ArrayAdapter.createFromResource(this,
                R.array.graph_type_array, android.R.layout.simple_spinner_dropdown_item)
        adapterTypeSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        spinnerGraphType.adapter = adapterTypeSpinner
        spinnerGraphType.setSelection(0)

        getWeekly()
        getPie()
    }

    private fun getWeekly(){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val sundayStr: String = dateFormat.format(calendar.time)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val mondayStr: String = dateFormat.format(calendar.time)

        val results2 = database.use {
            select(DbHelper.TABLE_RESULT)
        }

        val results = database.use {
            select(DbHelper.TABLE_RESULT).whereArgs("date >= '" + mondayStr + "' and date <= '" + sundayStr + "';" ).exec { parseList(parser) }
        }

        val resultDateStr = results.map { result -> dateFormat.format(result.date) }
        val entries = ArrayList<Entry>()
        val datesMap = HashMap<Float, String>()

        for (i in 1..7){
            val calStr = dateFormat.format(calendar.time)
            val calStrShort = dateFormatCroShort.format(calendar.time)
            if (calStr in resultDateStr) {
                val res = results.filter { result -> dateFormat.format(result.date) == calStr }
                val scorePercent = (res.sumBy { result -> result.score }.toDouble() / res.size) * 10
                entries.add(Entry(i.toFloat(), scorePercent.toFloat()))
            }
            else {
                entries.add(Entry(i.toFloat(), 0f))
            }
            datesMap[i.toFloat()] = calStrShort
            calendar.add(Calendar.DATE, 1)
        }

        val dataSet = LineDataSet(entries, "Weekly scores")
        dataSet.setColor(Color.RED)
        dataSet.setCircleColor(Color.GRAY)
        val lineData = LineData(dataSet)
        lineChart.description.isEnabled = false
        lineChart.data = lineData

        val xAxis = lineChart.xAxis
        xAxis.setValueFormatter { value, axis -> datesMap.get(value) }
        xAxis.position = XAxisPosition.BOTTOM

        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 100f
        lineChart.isClickable = false
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.setPinchZoom(false)
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false
        lineChart.invalidate()
    }

    private fun getPie(){
        val results = database.use {
            select(DbHelper.TABLE_RESULT).exec { parseList(parser) }
        }
        val sumRight = results.sumBy { result -> result.score }
        val sumBad = results.sumBy { result -> result.numAns } - sumRight
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(sumRight.toFloat(), "Right"))
        entries.add(PieEntry(sumBad.toFloat(), "Bad"))

        val dataSet = PieDataSet(entries, "Right and False answers")
        dataSet.valueTextSize = 12f
        dataSet.setColors(Color.parseColor("#cc0004"), Color.parseColor("#8c8c8c"))
        val pieData = PieData(dataSet)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Answers"
        pieChart.setBackgroundColor(Color.parseColor("#eeeeee"))
        pieChart.setCenterTextSize(18f)
        pieChart.setCenterTextColor(Color.GRAY)
        pieChart.invalidate()
    }
}
