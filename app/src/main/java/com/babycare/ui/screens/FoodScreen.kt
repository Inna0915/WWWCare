package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.data.model.FoodFeedback
import com.babycare.data.model.FoodTexture
import com.babycare.ui.components.*
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // UI 状态
    var selectedDateTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedFoodType by remember { mutableStateOf("") }
    var selectedTexture by remember { mutableStateOf<FoodTexture?>(null) }
    var foodAmount by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("ml") }
    var duration by remember { mutableStateOf("") }
    var selectedFeedback by remember { mutableStateOf<FoodFeedback?>(null) }
    var note by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    var showFoodTypeDialog by remember { mutableStateOf(false) }
    var showTextureDialog by remember { mutableStateOf(false) }

    // 时间格式化
    val timeFormatter = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val startTime = timeFormatter.format(Date(selectedDateTime))

    val units = listOf("ml", "勺", "g")
    val textureOptions = listOf(
        "稀滑" to FoodTexture.THIN,
        "泥状" to FoodTexture.PUREE,
        "粘稠" to FoodTexture.THICK
    )
    val feedbackOptions = listOf(
        Triple(FoodFeedback.LIKE, "喜欢", "😊"),
        Triple(FoodFeedback.NEUTRAL, "一般", "😐"),
        Triple(FoodFeedback.DISLIKE, "不喜欢", "😢"),
        Triple(FoodFeedback.ALLERGY, "过敏", "😫")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "辅食",
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
                    containerColor = BackgroundGreen
                )
            )
        },
        containerColor = BackgroundGreen
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 表单卡片
            FormCard {
                FormInputItem(
                    label = "开始时间",
                    value = startTime,
                    onValueChange = {},
                    onClick = {
                        // TODO: 打开时间选择器，更新 selectedDateTime
                    }
                )
                FormDivider()

                FormInputItem(
                    label = "辅食类型",
                    value = selectedFoodType,
                    onValueChange = {},
                    hint = "选择辅食",
                    onClick = { showFoodTypeDialog = true }
                )
                FormDivider()

                FormInputItem(
                    label = "性状",
                    value = textureOptions.find { it.second == selectedTexture }?.first ?: "",
                    onValueChange = {},
                    hint = "选择性状",
                    onClick = { showTextureDialog = true }
                )
                FormDivider()

                FormInputItem(
                    label = "食量",
                    value = foodAmount,
                    onValueChange = { foodAmount = it },
                    hint = "输入食量"
                )
                FormDivider()

                // 食量单位
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "食量单位",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        units.forEach { unit ->
                            val isSelected = unit == selectedUnit
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) PrimaryPink else Gray100
                                    )
                                    .clickable { selectedUnit = unit }
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = unit,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) White else TextPrimary
                                )
                            }
                        }
                    }
                }
                FormDivider()

                FormInputItem(
                    label = "持续时间(选填)",
                    value = duration,
                    onValueChange = {},
                    onClick = { /* 打开时间选择 */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 宝宝反馈
            FormCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    feedbackOptions.forEach { (feedback, label, _) ->
                        MoodSelector(
                            mood = label,
                            label = label,
                            isSelected = selectedFeedback == feedback,
                            onClick = { selectedFeedback = feedback }
                        )
                    }
                }
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
                    if (selectedFoodType.isEmpty()) return@PrimarySaveButton
                    isSaving = true
                    viewModel.addFoodRecord(
                        startTime = selectedDateTime,
                        foodType = selectedFoodType,
                        texture = selectedTexture,
                        amount = foodAmount.toIntOrNull(),
                        unit = selectedUnit,
                        feedback = selectedFeedback,
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 辅食类型选择弹窗
    if (showFoodTypeDialog) {
        FoodTypeSelectionDialog(
            onDismiss = { showFoodTypeDialog = false },
            onConfirm = { selectedFoodType = it }
        )
    }

    // 性状选择弹窗
    if (showTextureDialog) {
        TextureSelectionDialog(
            options = textureOptions,
            selectedTexture = selectedTexture,
            onDismiss = { showTextureDialog = false },
            onConfirm = { selectedTexture = it }
        )
    }
}

@Composable
private fun FoodTypeSelectionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val foodTypes = listOf(
        "谷物", "肉类", "蔬菜", "水果",
        "果汁", "汤", "其它", "胡萝卜",
        "米粉", "土豆泥土豆泥"
    )

    var selectedType by remember { mutableStateOf("") }
    var customFood by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // 自定义输入
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Gray100)
                        .padding(12.dp)
                ) {
                    if (customFood.isEmpty()) {
                        Text(
                            text = "+ 添加其它辅食",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextHint
                        )
                    }
                    androidx.compose.foundation.text.BasicTextField(
                        value = customFood,
                        onValueChange = { customFood = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 选项网格
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    foodTypes.chunked(2).forEach { rowTypes ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowTypes.forEach { type ->
                                SelectableTag(
                                    text = type,
                                    isSelected = selectedType == type,
                                    onClick = { selectedType = type },
                                    modifier = Modifier.weight(1f),
                                    selectedColor = PrimaryPink
                                )
                            }
                            if (rowTypes.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 按钮组
                ConfirmButtonGroup(
                    onCancel = onDismiss,
                    onConfirm = {
                        val result = customFood.takeIf { it.isNotEmpty() } ?: selectedType
                        if (result.isNotEmpty()) {
                            onConfirm(result)
                        }
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun TextureSelectionDialog(
    options: List<Pair<String, FoodTexture>>,
    selectedTexture: FoodTexture?,
    onDismiss: () -> Unit,
    onConfirm: (FoodTexture) -> Unit
) {
    var tempSelection by remember { mutableStateOf(selectedTexture) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "选择性状",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 选项
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    options.forEach { (label, texture) ->
                        SelectableTag(
                            text = label,
                            isSelected = tempSelection == texture,
                            onClick = { tempSelection = texture },
                            modifier = Modifier.fillMaxWidth(),
                            selectedColor = PrimaryPink
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 按钮组
                ConfirmButtonGroup(
                    onCancel = onDismiss,
                    onConfirm = {
                        tempSelection?.let { onConfirm(it) }
                        onDismiss()
                    }
                )
            }
        }
    }
}
