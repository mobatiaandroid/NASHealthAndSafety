package com.nas.healthandsafety.activity.report

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.nas.healthandsafety.R

class ReportActivity : AppCompatActivity() {

    private lateinit var chart: PieChart
    private lateinit var chart2: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        chart = findViewById(R.id.chart1)
        chart2 = findViewById(R.id.chart2)
        chart.setUsePercentValues(false)
        chart.description.isEnabled = false
        chart.setExtraOffsets(2f, 10f, 7f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

//        chart.centerText = generateCenterSpannableText()

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart2.setUsePercentValues(false)
        chart2.description.isEnabled = false
        chart2.setExtraOffsets(2f, 10f, 7f, 5f)

        chart2.dragDecelerationFrictionCoef = 0.95f

//        chart.centerText = generateCenterSpannableText()

        chart2.isDrawHoleEnabled = true
        chart2.setHoleColor(Color.WHITE)
        chart2.setTransparentCircleColor(Color.WHITE)
        chart2.setTransparentCircleAlpha(110)

        chart2.holeRadius = 58f
        chart2.transparentCircleRadius = 61f

        chart2.setDrawCenterText(true)

        chart2.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        chart2.isRotationEnabled = true
        chart2.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e == null) return
                Log.i(
                    "VAL SELECTED",
                    "Value: " + e.y + ", index: " + h!!.x
                            + ", DataSet index: " + h.dataSetIndex
                )
            }

            override fun onNothingSelected() {
//                TODO("Not yet implemented")
            }

        })



        chart.animateY(1400, Easing.EaseInOutQuad)
        chart2.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        // chart.spin(2000, 0, 360);
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK)
        chart.setEntryLabelTextSize(12f)
        setData()

    }

    val parties = arrayOf(
        "Point A", "Point B", "Point C", "Point D", "Point E", "Point F", "Party G", "Party H",
        "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
        "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
        "Party Y", "Party Z"
    )

    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString("Today's Evacuation Report")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)
        return s
    }

    private fun setData() {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        val count = 3
        val range = 4
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties.get(i % parties.size),
                    resources.getDrawable(R.drawable.star)
                )
            )
        }

        val dataSet = PieDataSet(entries, "Evacuation Results")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors


        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        chart.data = data
        chart2.data = data

        // undo all highlights

        // undo all highlights
        chart.highlightValues(null)

        chart.invalidate()
    }

}