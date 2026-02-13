package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.ui.components.PrimarySaveButton
import com.babycare.ui.components.SecondaryButton
import com.babycare.ui.components.StartTimerButton
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // 计时器状态
    var startTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    var isTiming by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isSaving by remember { mutableStateOf(false) }

    // 计时器逻辑
    LaunchedEffect(isTiming) {
        while (isTiming) {
            delay(1000L)
            elapsedSeconds++
        }
    }

    val hours = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60

    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    // 格式化开始时间显示
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val startTimeDisplay = timeFormatter.format(Date(startTimestamp))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "睡眠",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    if (!isTiming) {
                        OutlinedButton(
                            onClick = { /* 新增记录 */ },
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = White
                            ),
                            border = null
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "+",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "新增记录",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundPurple
                )
            )
        },
        containerColor = BackgroundPurple
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 开始时间
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "开始时间",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextSecondary
                    )
                )
                Text(
                    text = startTimeDisplay,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
                IconButton(
                    onClick = { /* TODO: 打开时间选择器 */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "编辑",
                        modifier = Modifier.size(18.dp),
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 计时器显示
            Text(
                text = timeText,
                style = MaterialTheme.typography.displayLarge.copy(
                    color = TextPrimary,
                    letterSpacing = 4.sp
                )
            )

            Spacer(modifier = Modifier.height(64.dp))

            // 月亮图标
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(BackgroundPurple),
                contentAlignment = Alignment.Center
            ) {
                // 简化的月亮图标
                Text(
                    text = "🌙",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 120.sp
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 按钮组
            if (isTiming) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SecondaryButton(
                        onClick = {
                            isTiming = false
                            elapsedSeconds = 0
                        },
                        text = "放弃",
                        modifier = Modifier.weight(1f)
                    )
                    StartTimerButton(
                        onClick = {
                            isTiming = false
                            isSaving = true
                            // 保存记录
                            val endTimestamp = startTimestamp + (elapsedSeconds * 1000L)
                            viewModel.addSleepRecord(
                                startTime = startTimestamp,
                                endTime = endTimestamp,
                                sleepMethod = null, // TODO: 添加入睡方式选择
                                quality = null, // TODO: 添加睡眠质量选择
                                note = null
                            ) { recordId ->
                                isSaving = false
                                elapsedSeconds = 0
                                onSaveSuccess()
                            }
                        },
                        text = "结束",
                        modifier = Modifier.weight(1f),
                        isTiming = true
                    )
                }
            } else {
                StartTimerButton(
                    onClick = {
                        startTimestamp = System.currentTimeMillis()
                        isTiming = true
                    },
                    text = "开始",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
