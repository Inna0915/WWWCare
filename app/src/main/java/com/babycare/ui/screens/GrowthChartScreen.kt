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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthChartScreen(
    onBackClick: () -> Unit,
    onRecordListClick: () -> Unit
) {
    var selectedMetric by remember { mutableStateOf("体重") }
    val metrics = listOf("体重", "身高", "BMI", "头围", "脚长")
    var selectedStandard by remember { mutableStateOf("WHO（世界卫生组织）标准") }

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
            GrowthChart()

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

@Composable
private fun GrowthChart() {
    // 模拟图表数据
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Y轴标签和网格线
            val yLabels = listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13")
            val xLabels = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")

            // 图表主体
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // 网格线
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(12) { index ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = yLabels.getOrElse(11 - index) { "" },
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                modifier = Modifier.width(24.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(0.5.dp)
                                    .background(Gray200)
                            )
                        }
                    }
                }

                // 百分位曲线（简化示意）
                PercentileCurves()

                // 实际数据点
                DataPoints()
            }

            // X轴标签
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                xLabels.forEach { label ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // X轴标题
            Text(
                text = "年龄(月)",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.End)
            )
        }

        // Y轴单位
        Text(
            text = "单位(kg)",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun PercentileCurves() {
    // 这里应该使用真实的图表库，如 Compose Charts 或 MPAndroidChart
    // 现在用简单的Box模拟曲线效果
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp)
    ) {
        // 97% 曲线（红色虚线）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(ChartRed.copy(alpha = 0.5f))
                .align(Alignment.TopEnd)
        ) {
            Text(
                text = "97%",
                style = MaterialTheme.typography.bodySmall,
                color = ChartRed,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        // 85% 曲线（橙色虚线）
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(2.dp)
                .background(ChartOrange.copy(alpha = 0.5f))
                .align(Alignment.CenterEnd)
        ) {
            Text(
                text = "85%",
                style = MaterialTheme.typography.bodySmall,
                color = ChartOrange,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        // 50% 曲线（绿色实线）
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(2.dp)
                .background(ChartGreen)
                .align(Alignment.Center)
        ) {
            Text(
                text = "50%",
                style = MaterialTheme.typography.bodySmall,
                color = ChartGreen,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        // 15% 曲线（橙色虚线）
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(2.dp)
                .background(ChartOrange.copy(alpha = 0.5f))
                .align(Alignment.CenterStart)
        ) {
            Text(
                text = "15%",
                style = MaterialTheme.typography.bodySmall,
                color = ChartOrange,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        // 3% 曲线（红色虚线）
        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(2.dp)
                .background(ChartRed.copy(alpha = 0.5f))
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = "3%",
                style = MaterialTheme.typography.bodySmall,
                color = ChartRed,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun DataPoints() {
    // 实际测量数据点（绿色圆点）
    val dataPoints = listOf(
        0.1f to 0.9f,   // 3.8kg
        0.2f to 0.85f,  // 4.2kg
        0.4f to 0.7f,   // 5.2kg
        0.6f to 0.5f    // 6.0kg
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp)
    ) {
        dataPoints.forEach { (x, y) ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .offset(
                        x = (x * 300).dp,
                        y = (y * 300).dp
                    )
                    .clip(CircleShape)
                    .background(ChartGreen)
            )
        }
    }
}
