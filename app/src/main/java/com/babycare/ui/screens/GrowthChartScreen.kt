package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.data.model.Gender
import com.babycare.data.model.WHOGrowthStandards
import com.babycare.ui.components.ChartType
import com.babycare.ui.components.GrowthChart
import com.babycare.ui.components.GrowthDataPoint
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthChartScreen(
    onBackClick: () -> Unit,
    onRecordListClick: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // 获取生长记录
    val growthRecords by viewModel.getAllGrowthRecords().collectAsState(initial = emptyList())

    // 当前选择的指标
    var selectedMetric by remember { mutableStateOf("体重") }
    val metrics = listOf("体重", "身高", "头围", "BMI", "脚长")
    var selectedStandard by remember { mutableStateOf("WHO（世界卫生组织）标准") }

    // 宝宝性别（默认男孩，可从设置中获取）
    val gender = remember { Gender.BOY }

    // 根据选择的指标获取对应的数据和百分位
    val chartType = when (selectedMetric) {
        "体重" -> ChartType.WEIGHT
        "身高" -> ChartType.HEIGHT
        "头围" -> ChartType.HEAD
        else -> ChartType.WEIGHT
    }

    // 转换记录为图表数据点
    val userData = remember(growthRecords, chartType) {
        growthRecords.mapNotNull { record ->
            val ageInMonths = calculateAgeInMonths(record.record.startTime)
            val value = when (chartType) {
                ChartType.WEIGHT -> record.growthDetail.weight
                ChartType.HEIGHT -> record.growthDetail.height
                ChartType.HEAD -> record.growthDetail.headCircumference
                else -> record.growthDetail.weight
            }
            value?.let {
                GrowthDataPoint(
                    timestamp = record.record.startTime,
                    value = it,
                    ageInMonths = ageInMonths
                )
            }
        }.sortedBy { it.ageInMonths }
    }

    // 获取 WHO 百分位数据
    val percentileData = remember(chartType, gender) {
        when (chartType) {
            ChartType.WEIGHT -> WHOGrowthStandards.getWeightPercentiles(gender)
            ChartType.HEIGHT -> WHOGrowthStandards.getHeightPercentiles(gender)
            ChartType.HEAD -> WHOGrowthStandards.getHeadCircumferencePercentiles(gender)
            else -> WHOGrowthStandards.getWeightPercentiles(gender)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* 切换宝宝 */ }
                    ) {
                        Text(
                            text = "生长发育",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "切换",
                            tint = PrimaryPink
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 指标选择标签
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Gray100)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                metrics.forEach { metric ->
                    val isSelected = metric == selectedMetric
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) White else Color.Transparent)
                            .clickable { selectedMetric = metric }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = metric,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        )
                    }
                }
            }

            // 标准选择和记录列表按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 标准选择
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(TagPink)
                        .clickable { /* 选择标准 */ }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = selectedStandard,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "选择",
                            modifier = Modifier.size(16.dp),
                            tint = TextSecondary
                        )
                    }
                }

                // 记录列表按钮
                OutlinedButton(
                    onClick = onRecordListClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "记录列表",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "记录列表",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 图表区域
            if (userData.isNotEmpty()) {
                GrowthChart(
                    userData = userData,
                    percentileData = percentileData,
                    chartType = chartType,
                    gender = gender,
                    title = selectedMetric
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "暂无数据",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "请先添加生长记录",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextHint
                        )
                    }
                }
            }

            // 生长评价按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(LightOrange.copy(alpha = 0.2f))
                        .clickable { /* 查看评价 */ }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "生长评价",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightOrange
                    )
                }
            }
        }
    }
}

/**
 * 计算月龄（从出生到记录日期）
 */
private fun calculateAgeInMonths(timestamp: Long): Float {
    // 这里简化计算，假设出生日期可以通过设置获取
    // 实际应该从宝宝出生日期计算
    val calendar = Calendar.getInstance()
    val recordDate = calendar.apply { timeInMillis = timestamp }

    // 临时方案：使用相对时间（模拟0-24个月）
    // 实际项目中应该从设置的出生日期计算
    val now = System.currentTimeMillis()
    val diffMillis = now - timestamp
    val diffDays = diffMillis / (1000 * 60 * 60 * 24)
    return (24 - diffDays / 30f).coerceIn(0f, 24f)
}
