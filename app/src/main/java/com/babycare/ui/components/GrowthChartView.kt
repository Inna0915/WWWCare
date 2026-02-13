package com.babycare.ui.components

import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import com.babycare.data.model.Gender
import java.util.*

/**
 * 生长曲线数据点
 */
data class GrowthDataPoint(
    val timestamp: Long,
    val value: Float,
    val ageInMonths: Float
)

/**
 * WHO 百分位数据
 */
data class PercentileData(
    val p3: List<Pair<Float, Float>>,   // 3rd percentile
    val p15: List<Pair<Float, Float>>,  // 15th percentile
    val p50: List<Pair<Float, Float>>,  // 50th percentile (median)
    val p85: List<Pair<Float, Float>>,  // 85th percentile
    val p97: List<Pair<Float, Float>>   // 97th percentile
)

/**
 * 图表类型
 */
enum class ChartType {
    HEIGHT,   // 身高
    WEIGHT,   // 体重
    HEAD,     // 头围
    BMI       // BMI
}

/**
 * Compose 封装的生长曲线图表
 */
@Composable
fun GrowthChart(
    userData: List<GrowthDataPoint>,
    percentileData: PercentileData,
    chartType: ChartType,
    gender: Gender,
    modifier: Modifier = Modifier,
    title: String = ""
) {
    val textColor = MaterialTheme.colorScheme.onSurface.hashCode()
    val gridColor = MaterialTheme.colorScheme.outlineVariant.hashCode()

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // 基本配置
                description.isEnabled = false
                legend.isEnabled = true
                legend.textColor = textColor
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)

                // X轴配置 (月龄)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = textColor
                    gridColor = gridColor
                    granularity = 1f
                    labelCount = 12
                    valueFormatter = MonthValueFormatter()
                }

                // Y轴配置
                axisLeft.apply {
                    textColor = textColor
                    gridColor = gridColor
                }
                axisRight.isEnabled = false

                // 设置数据
                setupChartData(this, userData, percentileData, chartType)
            }
        },
        modifier = modifier.fillMaxWidth().height(300.dp),
        update = { chart ->
            setupChartData(chart, userData, percentileData, chartType)
            chart.invalidate()
        }
    )
}

/**
 * 设置图表数据
 */
private fun setupChartData(
    chart: LineChart,
    userData: List<GrowthDataPoint>,
    percentileData: PercentileData,
    chartType: ChartType
) {
    val dataSets = mutableListOf<LineDataSet>()

    // 添加百分位曲线
    dataSets.add(createPercentileLine(percentileData.p3, "P3", 0xFFE0E0E0.toInt()))
    dataSets.add(createPercentileLine(percentileData.p15, "P15", 0xFFBDBDBD.toInt()))
    dataSets.add(createPercentileLine(percentileData.p50, "P50", 0xFF4CAF50.toInt(), lineWidth = 2f))
    dataSets.add(createPercentileLine(percentileData.p85, "P85", 0xFFBDBDBD.toInt()))
    dataSets.add(createPercentileLine(percentileData.p97, "P97", 0xFFE0E0E0.toInt()))

    // 添加用户数据
    if (userData.isNotEmpty()) {
        val entries = userData.map { Entry(it.ageInMonths, it.value) }
        val userDataSet = LineDataSet(entries, "宝宝数据").apply {
            color = AndroidColor.parseColor("#FF6B7A")
            setCircleColor(AndroidColor.parseColor("#FF6B7A"))
            circleRadius = 5f
            lineWidth = 2.5f
            setDrawValues(false)
            setDrawCircleHole(true)
            circleHoleColor = AndroidColor.WHITE
        }
        dataSets.add(userDataSet)
    }

    chart.data = LineData(dataSets.toList())
}

/**
 * 创建百分位线
 */
private fun createPercentileLine(
    data: List<Pair<Float, Float>>,
    label: String,
    color: Int,
    lineWidth: Float = 1f
): LineDataSet {
    val entries = data.map { Entry(it.first, it.second) }
    return LineDataSet(entries, label).apply {
        this.color = color
        this.lineWidth = lineWidth
        setDrawCircles(false)
        setDrawValues(false)
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }
}

/**
 * X轴月龄格式化器
 */
class MonthValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}月"
    }
}

/**
 * 简单的折线图组件（用于趋势显示）
 */
@Composable
fun SimpleLineChart(
    data: List<Pair<Long, Float>>,
    modifier: Modifier = Modifier,
    lineColor: Int = AndroidColor.parseColor("#FF6B7A"),
    fillColor: Int = AndroidColor.parseColor("#20FF6B7A")
) {
    val textColor = MaterialTheme.colorScheme.onSurface.hashCode()

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                isDragEnabled = false
                setScaleEnabled(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = textColor
                    setDrawGridLines(false)
                    valueFormatter = DateValueFormatter()
                }

                axisLeft.apply {
                    textColor = textColor
                    setDrawGridLines(true)
                }
                axisRight.isEnabled = false

                updateSimpleChartData(this, data, lineColor, fillColor)
            }
        },
        modifier = modifier,
        update = { chart ->
            updateSimpleChartData(chart, data, lineColor, fillColor)
            chart.invalidate()
        }
    )
}

private fun updateSimpleChartData(
    chart: LineChart,
    data: List<Pair<Long, Float>>,
    lineColor: Int,
    fillColor: Int
) {
    if (data.isEmpty()) return

    val entries = data.mapIndexed { index, pair ->
        Entry(index.toFloat(), pair.second)
    }

    val dataSet = LineDataSet(entries, "Data").apply {
        color = lineColor
        setCircleColor(lineColor)
        circleRadius = 3f
        lineWidth = 2f
        setDrawFilled(true)
        fillColor = fillColor
        setDrawValues(false)
    }

    chart.data = LineData(dataSet)
}

/**
 * 日期格式化器
 */
class DateValueFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        return dateFormat.format(Date(value.toLong()))
    }
}
