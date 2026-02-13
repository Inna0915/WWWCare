package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onRecordClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 宝宝头像
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryPinkLight)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "桐桐",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = "7个月18天",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary
                                )
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* 通知 */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "通知",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundBlue
                )
            )
        },
        containerColor = BackgroundBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 今日概览卡片
            TodayOverviewCard()

            Spacer(modifier = Modifier.height(16.dp))

            // 进行中的记录
            OngoingRecordCard(
                title = "🍼 母乳喂养",
                duration = "15:23",
                onClick = { onRecordClick("breastfeeding") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 今日时间轴
            Text(
                text = "今日记录",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // 记录列表
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(sampleRecords) { record ->
                    RecordTimelineItem(record = record)
                }
            }
        }
    }
}

@Composable
private fun TodayOverviewCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "今日概览",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OverviewItem(count = "5次", label = "喂奶", icon = "🍼")
                OverviewItem(count = "4h", label = "睡眠", icon = "😴")
                OverviewItem(count = "6次", label = "换尿布", icon = "👶")
                OverviewItem(count = "1次", label = "补剂", icon = "💊")
            }
        }
    }
}

@Composable
private fun OverviewItem(count: String, label: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary
            )
        )
    }
}

@Composable
private fun OngoingRecordCard(
    title: String,
    duration: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryPinkLight.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryPink),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⏱️",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "正在计时",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary
                        )
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
            Text(
                text = duration,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPink
                )
            )
        }
    }
}

data class RecordItem(
    val time: String,
    val title: String,
    val detail: String,
    val icon: String,
    val iconBackground: Color
)

private val sampleRecords = listOf(
    RecordItem("16:14", "母乳", "总量: 180ml", "🍼", Color(0xFFE3F2FD)),
    RecordItem("13:09", "双侧吸奶", "总量: 120ml, 左侧: 60ml, 右侧: 60ml", "🔌", Color(0xFFE8F5E9)),
    RecordItem("12:22", "母乳", "总量: 220ml", "🍼", Color(0xFFE3F2FD)),
    RecordItem("08:35", "双侧吸奶", "总量: 230ml, 左侧: 115ml, 右侧: 115ml", "🔌", Color(0xFFE8F5E9)),
    RecordItem("07:39", "补剂", "补剂种类: 伊可新维生素AD 用量: 1粒", "💊", Color(0xFFFFF3E0)),
    RecordItem("05:29", "母乳", "总量: 220ml", "🍼", Color(0xFFE3F2FD))
)

@Composable
private fun RecordTimelineItem(record: RecordItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 时间
        Text(
            text = record.time,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
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
