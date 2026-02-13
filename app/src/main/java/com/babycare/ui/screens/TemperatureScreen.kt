package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.ui.components.FormCard
import com.babycare.ui.components.FormInputItem
import com.babycare.ui.components.NoteInput
import com.babycare.ui.components.PrimarySaveButton
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // UI 状态
    var temperature by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var note by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var showReferenceDialog by remember { mutableStateOf(false) }

    // 时间格式化
    val timeFormatter = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val measureTime = timeFormatter.format(Date(selectedDateTime))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "体温",
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 体温输入框
            FormCard {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.foundation.text.BasicTextField(
                            value = temperature,
                            onValueChange = { temperature = it },
                            textStyle = MaterialTheme.typography.displayMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.width(100.dp),
                            singleLine = true
                        )
                        Text(
                            text = "°C",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = TextSecondary
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Gray100)
                                .clickable { showReferenceDialog = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = TextSecondary
                                )
                                Text(
                                    text = "参考",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 表单
            FormCard {
                FormInputItem(
                    label = "测温时间",
                    value = measureTime,
                    onValueChange = {},
                    onClick = {
                        // TODO: 打开时间选择器，更新 selectedDateTime
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 备注
            NoteInput(
                value = note,
                onValueChange = { note = it },
                hint = "选填"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 保存按钮
            PrimarySaveButton(
                onClick = {
                    val temp = temperature.toFloatOrNull()
                    if (temp == null || temp < 35 || temp > 42) return@PrimarySaveButton
                    isSaving = true
                    viewModel.addTemperatureRecord(
                        startTime = selectedDateTime,
                        temperature = temp,
                        note = note.takeIf { it.isNotEmpty() }
                    ) { recordId ->
                        isSaving = false
                        onSaveSuccess()
                    }
                },
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    // 体温参考弹窗
    if (showReferenceDialog) {
        TemperatureReferenceDialog(
            onDismiss = { showReferenceDialog = false }
        )
    }
}

@Composable
private fun TemperatureReferenceDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // 标题
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "体温参考",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "单位: °C",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "以下温度计示意图为3-36个月儿童的体温（耳温）参考值",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 温度计示意图
                TemperatureScale()

                Spacer(modifier = Modifier.height(16.dp))

                // 参考表格
                TemperatureReferenceTable()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "数据仅供参考，若体温异常，建议带宝宝及时就诊，以医生诊断为准。",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 关闭按钮
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Gray200)
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "×",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TemperatureScale() {
    Column {
        // 温度条
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF81C784), // 绿色-正常
                            Color(0xFFFFB74D), // 橙色-发烧
                            Color(0xFFE57373)  // 红色-高烧
                        )
                    )
                )
        )

        // 刻度标签
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("35", "36", "37", "38", "39", "40", "41", "42").forEach { temp ->
                Text(
                    text = temp,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 状态标签
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatusLabel("正常", Color(0xFF81C784))
            StatusLabel("发烧", Color(0xFFFFB74D))
            StatusLabel("高烧", Color(0xFFE57373))
        }

        // 临界值
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "35.4",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = "37.6",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = "38.5",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun StatusLabel(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

@Composable
private fun TemperatureReferenceTable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gray100)
    ) {
        // 表头
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray200)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            TableCell("年龄范围", 1f, isHeader = true)
            TableCell("正常体温", 1f, isHeader = true, textColor = Color(0xFF81C784))
            TableCell("发烧", 1f, isHeader = true, textColor = Color(0xFFFFB74D))
            TableCell("高烧", 1f, isHeader = true, textColor = Color(0xFFE57373))
        }

        // 数据行
        val data = listOf(
            Triple("0-3个月", "≥35.8 - ≤37.4", ">37.4"),
            Triple("3-36个月", "≥35.4 - ≤37.6", "≤38.5"),
            Triple("36个月以上", "≥35.4 - ≤37.7", "≤39.4")
        )

        data.forEach { (age, normal, high) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                TableCell(age, 1f)
                TableCell(normal, 1f)
                TableCell(
                    if (age == "0-3个月") "无" else ">37.6 - $high",
                    1f
                )
                TableCell(
                    if (age == "0-3个月") high else ">$high",
                    1f
                )
            }
            if (age != "36个月以上") {
                HorizontalDivider(color = Gray300, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
    textColor: Color = TextPrimary
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = if (isHeader) {
            MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
        } else {
            MaterialTheme.typography.bodySmall
        },
        color = textColor
    )
}
