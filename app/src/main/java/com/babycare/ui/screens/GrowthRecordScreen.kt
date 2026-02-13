package com.babycare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.ui.components.*
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthRecordScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // UI 状态
    var selectedDateTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var headCircumference by remember { mutableStateOf("") }
    var footLength by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    // 时间格式化
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val recordDate = dateFormatter.format(Date(selectedDateTime))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "生长发育",
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

            // 输入表单
            FormCard {
                // 记录日期
                FormInputItem(
                    label = "记录日期",
                    value = recordDate,
                    onValueChange = {},
                    onClick = {
                        // TODO: 打开日期选择器，更新 selectedDateTime
                    }
                )
                FormDivider()

                // 身高
                FormInputItem(
                    label = "身高",
                    value = height,
                    onValueChange = { height = it },
                    hint = "",
                    unit = "cm"
                )
                FormDivider()

                // 体重
                FormInputItem(
                    label = "体重",
                    value = weight,
                    onValueChange = { weight = it },
                    hint = "",
                    unit = "kg"
                )
                FormDivider()

                // 头围
                FormInputItem(
                    label = "头围",
                    value = headCircumference,
                    onValueChange = { headCircumference = it },
                    hint = "",
                    unit = "cm"
                )
                FormDivider()

                // 脚长
                FormInputItem(
                    label = "脚长",
                    value = footLength,
                    onValueChange = { footLength = it },
                    hint = "",
                    unit = "cm"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 备注
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "备注",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FormCard {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (note.isEmpty()) {
                            Text(
                                text = "身高/体重/头围3种信息可以不用全部填写哦",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextHint
                                )
                            )
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = note,
                            onValueChange = { note = it },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary
                            ),
                            maxLines = 3
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 保存按钮
            PrimarySaveButton(
                onClick = {
                    // 至少需要填写一项
                    if (height.isEmpty() && weight.isEmpty() &&
                        headCircumference.isEmpty() && footLength.isEmpty()) {
                        return@PrimarySaveButton
                    }
                    isSaving = true
                    viewModel.addGrowthRecord(
                        startTime = selectedDateTime,
                        height = height.toFloatOrNull(),
                        weight = weight.toFloatOrNull(),
                        headCircumference = headCircumference.toFloatOrNull(),
                        footLength = footLength.toFloatOrNull(),
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
}
