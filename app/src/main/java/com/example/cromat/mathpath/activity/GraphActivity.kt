package com.example.cromat.mathpath.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
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
import com.example.cromat.mathpath.*
import com.example.cromat.mathpath.enums.GameType
import com.example.cromat.mathpath.enums.Range
import com.example.cromat.mathpath.model.Result


class GraphActivity : AppCompatActivity() {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateFormatCroShort = SimpleDateFormat("dd.MM.")
    val parser = rowParser {
        id: Int, date: String, score: Int, numAns: Int, gameType: String ->
        Result(id, dateFormat.parse(date), score, numAns, gameType)
    }

    var curRange: String = Range.WEEK.toString()
    var curType: String = GameType.STEPS.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        getLineChart(Range.WEEK.toString(), GameType.STEPS.toString())
        getPieChart(Range.WEEK.toString(), GameType.STEPS.toString())

        // Spinner filters

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

        spinnerGraphRange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                when(pos) {
                // 7 days
                    0 -> {
                        curRange = Range.WEEK.toString()
                        getLineChart(curRange, curType)
                        getPieChart(curRange, curType)
                    }
                // 1 month
                    1 -> {
                        curRange = Range.MONTH.toString()
                        getLineChart(curRange, curType)
                        getPieChart(curRange, curType)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }
        }

        spinnerGraphType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                when(pos) {
                // All
                    0 -> {
                        curType = GameType.ALL.toString()
                        getLineChart(curRange, curType)
                        getPieChart(curRange, curType)
                    }
                // Steps
                    1 -> {
                        curType = GameType.STEPS.toString()
                        getLineChart(curRange, curType)
                        getPieChart(curRange, curType)
                    }
                // Time
                    2 -> {
                        curType = GameType.TIME.toString()
                        getLineChart(curRange, curType)
                        getPieChart(curRange, curType)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }
        }
    }

    private fun getLineChart(range: String, gameType: String){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        var dateTo: String = dateFormat.format(calendar.time)
        var dayTo: Int = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var dateFrom: String = dateFormat.format(calendar.time)
        var dayFrom: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val xAxis = lineChart.xAxis


        if(range == Range.MONTH.toString()){
            dayTo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            calendar.set(Calendar.DAY_OF_MONTH, dayTo)
            dateTo = dateFormat.format(calendar.time)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            dateFrom = dateFormat.format(calendar.time)
            dayFrom = calendar.get(Calendar.DAY_OF_MONTH)
            xAxis.labelCount = 3
        }


        val results = database.use {
            if (gameType != GameType.ALL.toString()) {
                select(DbHelper.TABLE_RESULT).whereArgs("date >= '" + dateFrom + "' and date <= '" +
                        dateTo + "' and gameType == '" + gameType + "';").exec { parseList(parser) }
            }
            else {
                select(DbHelper.TABLE_RESULT).whereArgs("date >= '" + dateFrom + "' and date <= '" +
                        dateTo + "';").exec { parseList(parser) }
            }
        }

        val resultDateStr = results.map { result -> dateFormat.format(result.date) }
        val entries = ArrayList<Entry>()
        val datesMap = HashMap<Float, String>()

        for (i in 1..(dayTo - dayFrom + 1)){
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
        if(range == Range.MONTH.toString())
            dataSet.label = "Monthly scores"
        dataSet.setDrawValues(false)
        dataSet.setColor(Color.RED)
        dataSet.setCircleColor(Color.GRAY)
        val lineData = LineData(dataSet)
        lineChart.description.isEnabled = false
        lineChart.data = lineData

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

    private fun getPieChart(range: String, gameType: String){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        var dateTo: String = dateFormat.format(calendar.time)
        var dayTo: Int = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var dateFrom: String = dateFormat.format(calendar.time)
        var dayFrom: Int = calendar.get(Calendar.DAY_OF_MONTH)


        if(range == Range.MONTH.toString()){
            dayTo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            calendar.set(Calendar.DAY_OF_MONTH, dayTo)
            dateTo = dateFormat.format(calendar.time)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            dateFrom = dateFormat.format(calendar.time)
            dayFrom = calendar.get(Calendar.DAY_OF_MONTH)
        }


        val results = database.use {
            if (gameType != GameType.ALL.toString()) {
                select(DbHelper.TABLE_RESULT).whereArgs("date >= '" + dateFrom + "' and date <= '" +
                        dateTo + "' and gameType == '" + gameType + "';").exec { parseList(parser) }
            }
            else {
                select(DbHelper.TABLE_RESULT).whereArgs("date >= '" + dateFrom + "' and date <= '" +
                        dateTo + "';").exec { parseList(parser) }
            }
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
