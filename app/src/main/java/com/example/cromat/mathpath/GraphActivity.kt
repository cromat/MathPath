package com.example.cromat.mathpath

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.activity_graph.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap


class GraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        graph.setBackgroundColor(Color.WHITE)
        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(applicationContext)
        graph.gridLabelRenderer.numHorizontalLabels = 3
        graph.gridLabelRenderer.numVerticalLabels = 11
        graph.viewport.setMinY(0.0)
        graph.viewport.setMaxY(100.0)
        graph.viewport.isYAxisBoundsManual = true
        graph.gridLabelRenderer.isHumanRounding = false

        getWeekly()
    }

    fun getWeekly(){

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatCro = SimpleDateFormat("dd.MM.yyyy")

        val parser = rowParser {
            id: Int, score: Int, date: String ->
            Result(id, dateFormat.parse(date), score)
        }

        val results = database.use {
            select(DbHelper.TABLE_RESULT).exec { parseList(parser) }
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val minX = calendar.time.time.toDouble()

        var dataPoints = arrayListOf<DataPoint>()
        val resultDateStr = results.map { result -> dateFormat.format(result.date) }

        for (i in 1..7){
            if (dateFormat.format(calendar.time) in resultDateStr) {
                val res = results.filter { result -> dateFormat.format(result.date) == dateFormat.format(calendar.time) }
                val scorePercent = (res.sumBy { result -> result.score }.toDouble() / res.size) * 10
                dataPoints.add(DataPoint(calendar.time, scorePercent))
            }
            else {
                dataPoints.add(DataPoint(calendar.time, 0.0))
            }
            calendar.add(Calendar.DATE, 1)
        }

        calendar.add(Calendar.DATE, -1)
        val maxX = calendar.time.time.toDouble()

        val series = PointsGraphSeries<DataPoint>(dataPoints.toTypedArray())
        val series2 = LineGraphSeries<DataPoint>(dataPoints.toTypedArray())

        graph.addSeries(series)
        graph.addSeries(series2)
        graph.viewport.setMinX(minX)
        graph.viewport.setMaxX(maxX)
        graph.viewport.isXAxisBoundsManual = true
    }
}
