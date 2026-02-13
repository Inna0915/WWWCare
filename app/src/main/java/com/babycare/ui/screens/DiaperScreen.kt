package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.data.model.DiaperType
import com.babycare.data.model.DiaperWeight
import com.babycare.ui.components.*
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaperScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // UI 状态
    var selectedDateTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedType by remember { mutableStateOf(DiaperType.BOTH) }
    var diaperWeight by remember { mutableStateOf(DiaperWeight.NORMAL) }
    var poopState by remember { mutableStateOf("普通软糊状") }
    var selectedColorName by remember { mutableStateOf("黄色") }
    var selectedColor by remember { mutableStateOf<Color?>(Color(0xFFFFEB3B)) }
    var reminderTime by remember { mutableStateOf("3小时0分") }
    var isReminderEnabled by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }

    // 保存状态
    var isSaving by remember { mutableStateOf(false) }

    // 对话框显示状态
    var showDateTimePicker by remember { mutableStateOf(false) }
    var showReminderPicker by remember { mutableStateOf(false) }

    // 时间格式化
    val diaperTime = formatDateTime(selectedDateTime)

    // 选项映射
    val typeOptions = listOf(
        "嘘嘘" to DiaperType.PEE,
        "便便" to DiaperType.POOP,
        "嘘嘘+便便" to DiaperType.BOTH
    )
    val weightOptions = listOf(
        "很轻" to DiaperWeight.LIGHT,
        "正常" to DiaperWeight.NORMAL,
        "很重" to DiaperWeight.HEAVY
    )
    val stateOptions = listOf(
        "普通软糊状", "较干", "成型",
        "颗粒状", "水样便", "水便分离",
        "蛋花汤状", "血便", "油性大便",
        "豆腐渣样", "泡沫状"
    )
    val colorOptions = listOf(
        Color(0xFFFFEB3B) to "黄色",
        Color(0xFF2E5018) to "墨绿色",
        Color(0xFF5D4037) to "棕色",
        Color(0xFF4CAF50) to "绿色",
        Color(0xFFE53935) to "红色"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "换尿布",
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
                    containerColor = BackgroundPink
                )
            )
        },
        containerColor = BackgroundPink
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 更换时间
            FormCard {
                FormInputItem(
                    label = "更换时间",
                    value = diaperTime,
                    onValueChange = {},
                    onClick = { showDateTimePicker = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 类型和状态卡片
            FormCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 类型选择
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "选择类型和状态",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        TagGroup(
                            options = typeOptions.map { it.first },
                            selectedOption = typeOptions.find { it.second == selectedType }?.first ?: "嘘嘘+便便",
                            onOptionSelected = { selected ->
                                typeOptions.find { it.first == selected }?.let {
                                    selectedType = it.second
                                }
                            },
                            selectedColor = LightOrange
                        )
                    }

                    // 尿布重量
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "尿布重量",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary
                            )
                        )
                        TagGroup(
                            options = weightOptions.map { it.first },
                            selectedOption = weightOptions.find { it.second == diaperWeight }?.first ?: "正常",
                            onOptionSelected = { selected ->
                                weightOptions.find { it.first == selected }?.let {
                                    diaperWeight = it.second
                                }
                            },
                            selectedColor = LightOrange
                        )
                    }

                    // 便便状态
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "便便状态",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary
                            )
                        )
                        FlowTagGroup(
                            options = stateOptions,
                            selectedOption = poopState,
                            onOptionSelected = { poopState = it },
                            selectedColor = LightOrange
                        )
                    }

                    // 便便颜色
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "便便颜色",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary
                            )
                        )
                        ColorSelectorRow(
                            colors = colorOptions,
                            selectedColor = selectedColor,
                            onColorSelected = { color ->
                                selectedColor = color
                                selectedColorName = colorOptions.find { it.first == color }?.second ?: ""
                            },
                            onPhotoClick = { /* TODO: 打开相机 */ }
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
                    viewModel.addDiaperRecord(
                        startTime = selectedDateTime,
                        type = selectedType,
                        weight = diaperWeight,
                        poopState = poopState,
                        poopColor = selectedColorName,
                        photoUri = null,
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
