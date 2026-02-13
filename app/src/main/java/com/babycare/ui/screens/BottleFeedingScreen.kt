package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.data.model.BottleFeedingType
import com.babycare.ui.components.*
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleFeedingScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // UI 状态
    var selectedDateTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedType by remember { mutableStateOf(BottleFeedingType.FORMULA) }
    var selectedAmount by remember { mutableStateOf(220) } // ml
    var selectedBrand by remember { mutableStateOf<Pair<String, String>?>("爱他美·卓傲" to "2段") }
    var reminderTime by remember { mutableStateOf("3小时0分") }
    var isReminderEnabled by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }

    // 保存状态
    var isSaving by remember { mutableStateOf(false) }

    // 对话框显示状态
    var showDateTimePicker by remember { mutableStateOf(false) }
    var showReminderPicker by remember { mutableStateOf(false) }

    // 时间格式化
    val feedingTime = formatDateTime(selectedDateTime)

    // 喂养类型映射
    val typeOptions = listOf(
        "母乳" to BottleFeedingType.BREAST_MILK,
        "母乳+奶粉" to BottleFeedingType.MIXED,
        "奶粉" to BottleFeedingType.FORMULA,
        "水奶" to BottleFeedingType.WATER_MILK,
        "水" to BottleFeedingType.WATER,
        "其他" to BottleFeedingType.OTHER
    )
    val feedingTypes = typeOptions.map { it.first }
    val amountOptions = listOf(180, 200, 220, 240, 260)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "奶瓶喂养",
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
            // 顶部奶量选择器
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 奶瓶图标和喂养指南标签
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "🍼",
                                style = MaterialTheme.typography.displaySmall
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LightBlue.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "喂养指南",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LightBlue
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 奶量选择（简化版滚轮）
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        amountOptions.forEach { amount ->
                            val isSelected = amount == selectedAmount
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) LightBlue.copy(alpha = 0.3f)
                                        else Color.Transparent
                                    )
                                    .clickable { selectedAmount = amount }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${amount}ml",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) TextPrimary else TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 表单卡片
            FormCard {
                // 喂养时间
                FormInputItem(
                    label = "喂养时间",
                    value = feedingTime,
                    onValueChange = {},
                    onClick = { showDateTimePicker = true }
                )
                FormDivider()

                // 喂养类型
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "喂养类型",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = TextPrimary
                        )
                    )
                    FlowTagGroup(
                        options = feedingTypes,
                        selectedOption = typeOptions.find { it.second == selectedType }?.first ?: "奶粉",
                        onOptionSelected = { selected ->
                            typeOptions.find { it.first == selected }?.let {
                                selectedType = it.second
                            }
                        },
                        selectedColor = LightBlue
                    )
                }
                FormDivider()

                // 奶粉品牌
                if (selectedType == "奶粉" || selectedType == "母乳+奶粉") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "奶粉品牌",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextPrimary
                            )
                        )
                        selectedBrand?.let { (brand, stage) ->
                            BrandTag(
                                brandName = brand,
                                stage = stage,
                                isSelected = true,
                                onClick = { /* 选择品牌 */ },
                                onClear = { selectedBrand = null }
                            )
                        } ?: Text(
                            text = "选择品牌 >",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextHint
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 设置提醒
            FormCard {
                ReminderRow(
                    reminderTime = reminderTime,
                    isEnabled = isReminderEnabled,
                    onToggle = { isReminderEnabled = it },
                    onTimeClick = { showReminderPicker = true }
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
                    isSaving = true
                    val (hours, minutes) = parseReminderTime(reminderTime)
                    viewModel.addBottleFeedingRecord(
                        startTime = selectedDateTime,
                        amount = selectedAmount,
                        feedingType = selectedType,
                        brand = selectedBrand?.first,
                        stage = selectedBrand?.second,
                        note = note.takeIf { it.isNotEmpty() },
                        enableReminder = isReminderEnabled,
                        reminderHours = hours,
                        reminderMinutes = minutes
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 日期时间选择器
    if (showDateTimePicker) {
        DateTimePickerDialog(
            initialDateTime = selectedDateTime,
            onDismiss = { showDateTimePicker = false },
            onConfirm = { selectedDateTime = it }
        )
    }

    // 提醒时间选择器
    if (showReminderPicker) {
        val (initialHours, initialMinutes) = parseReminderTime(reminderTime)
        ReminderTimePickerDialog(
            initialHours = initialHours,
            initialMinutes = initialMinutes,
            onDismiss = { showReminderPicker = false },
            onConfirm = { hours, minutes ->
                reminderTime = formatReminderTime(hours, minutes)
            }
        )
    }
}
