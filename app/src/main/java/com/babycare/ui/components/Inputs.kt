package com.babycare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

/**
 * 表单输入项 - 左侧标签，右侧值/输入框
 */
@Composable
fun FormInputItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    hint: String = "",
    unit: String = "",
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(clickableModifier)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (onClick != null || readOnly) {
                // 选择型输入
                Text(
                    text = value.takeIf { it.isNotEmpty() } ?: hint,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (value.isNotEmpty()) TextPrimary else TextHint
                )
                if (onClick != null) {
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextHint
                    )
                }
            } else {
                // 直接输入型
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    hint = hint,
                    unit = unit,
                    keyboardType = keyboardType
                )
            }
        }
    }
}

/**
 * 基础文本输入框
 */
@Composable
private fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    unit: String,
    keyboardType: KeyboardType
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.widthIn(min = 60.dp, max = 150.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = TextPrimary
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextHint
                        )
                    }
                    innerTextField()
                }
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            }
        }
    )
}

/**
 * 卡片容器 - 白色圆角卡片
 */
@Composable
fun FormCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .padding(vertical = 8.dp),
        content = content
    )
}

/**
 * 分隔线
 */
@Composable
fun FormDivider(
    modifier: Modifier = Modifier,
    color: Color = Gray200
) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 16.dp),
        color = color,
        thickness = 0.5.dp
    )
}

/**
 * 备注输入区域
 */
@Composable
fun NoteInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "选填",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "备注",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(White)
                .padding(16.dp)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextHint
                )
            }
            androidx.compose.foundation.text.BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    lineHeight = 24.sp
                ),
                maxLines = 4
            )
        }
    }
}

/**
 * 设置提醒行
 */
@Composable
fun ReminderRow(
    reminderTime: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "设置提醒",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onTimeClick),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = reminderTime,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isEnabled) TextPrimary else TextHint
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isEnabled) TextSecondary else TextHint
                )
            }

            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    checkedTrackColor = PrimaryPink,
                    uncheckedThumbColor = White,
                    uncheckedTrackColor = Gray300
                )
            )
        }
    }
}
