package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onChartClick: () -> Unit
) {
    var currentDate by remember { mutableStateOf("02月13日") }
    var selectedTab by remember { mutableStateOf("全部") }

    val tabs = listOf("全部", "喂养", "换尿布", "睡眠", "吸奶器", "辅食", "其他")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { /* 上一天 */ }) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "上一天"
                            )
                        }
                        Text(
                            text = currentDate,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryPink
                            )
                        )
                        IconButton(onClick = { /* 下一天 */ }) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "下一天"
                            )
                        }
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = onChartClick,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = PrimaryPink
                        ),
                        border = null
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "图表",
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "图表",
                            style = MaterialTheme.typography.bodySmall,
                            color = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundPeach
                )
            )
        },
        containerColor = BackgroundPeach
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 标签栏
            ScrollableTabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                containerColor = BackgroundPeach,
                contentColor = PrimaryPink,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    if (tabs.indexOf(selectedTab) < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                            color = PrimaryPink
                        )
                    }
                }
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Text(
                                text = tab,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (selectedTab == tab) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (selectedTab == tab) PrimaryPink else TextSecondary
                                )
                            )
                        }
                    )
                }
            }

            // 日期标题
            Text(
                text = "2026-02-13",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = PrimaryPink,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(16.dp)
            )

            // 统计摘要
            DailySummaryCard()

            Spacer(modifier = Modifier.height(16.dp))

            // 记录列表
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(sampleCalendarRecords) { record ->
                    CalendarRecordItem(record = record)
                }
            }
        }
    }
}

@Composable
private fun DailySummaryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryRow(
                label = "奶瓶喂养",
                count = "3次",
                detail = "共620ml（母乳3次, 620ml）"
            )
            SummaryRow(
                label = "吸奶器",
                count = "2次",
                detail = "共 350ml（左侧175ml, 右侧175ml）"
            )
            SummaryRow(
                label = "补剂",
                count = "1次",
                detail = ""
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, count: String, detail: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Text(
                text = count,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        if (detail.isNotEmpty()) {
            Text(
                text = detail,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

data class CalendarRecord(
    val time: String,
    val title: String,
    val detail: String,
    val icon: String,
    val iconBackground: Color
)

private val sampleCalendarRecords = listOf(
    CalendarRecord("16:14", "母乳", "总量: 180ml", "🍼", Color(0xFFE3F2FD)),
    CalendarRecord("13:09", "双侧吸奶", "总量: 120ml, 左侧: 60ml, 右侧: 60ml", "🔌", Color(0xFFE8F5E9)),
    CalendarRecord("12:22", "母乳", "总量: 220ml", "🍼", Color(0xFFE3F2FD)),
    CalendarRecord("08:35", "双侧吸奶", "总量: 230ml, 左侧: 115ml, 右侧: 115ml", "🔌", Color(0xFFE8F5E9)),
    CalendarRecord("07:39", "补剂", "补剂种类: 伊可新维生素AD 用量: 1粒", "💊", Color(0xFFFFF3E0)),
    CalendarRecord("05:29", "母乳", "总量: 220ml", "🍼", Color(0xFFE3F2FD))
)

@Composable
private fun CalendarRecordItem(record: CalendarRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { },
        verticalAlignment = Alignment.Top
    ) {
        // 时间
        Text(
            text = record.time,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.width(50.dp)
        )

        // 图标
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(record.iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = record.icon,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 内容
        Column {
            Text(
                text = record.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            )
            Text(
                text = record.detail,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary
                )
            )
        }
    }
}
