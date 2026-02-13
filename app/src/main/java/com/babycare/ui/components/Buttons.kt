package com.babycare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

/**
 * 主保存按钮 - 粉红色渐变圆角按钮
 */
@Composable
fun PrimarySaveButton(
    onClick: () -> Unit,
    text: String = "保存",
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(25.dp),
                spotColor = PrimaryPink.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(25.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(PrimaryPinkLight, PrimaryPink)
                )
            )
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Gray300
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )
            )
        }
    }
}

/**
 * 开始/计时按钮
 */
@Composable
fun StartTimerButton(
    onClick: () -> Unit,
    text: String = "开始",
    modifier: Modifier = Modifier,
    isTiming: Boolean = false
) {
    val buttonColor = if (isTiming) WarningOrange else PrimaryPink

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(25.dp),
                spotColor = buttonColor.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(25.dp))
            .background(buttonColor)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )
            )
        }
    }
}

/**
 * 次要按钮（放弃、取消等）
 */
@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = White
        ),
        border = null
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        )
    }
}

/**
 * 取消/确定 双按钮组
 */
@Composable
fun ConfirmButtonGroup(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    cancelText: String = "取消",
    confirmText: String = "确定",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = White
            )
        ) {
            Text(
                text = cancelText,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = TextPrimary
                )
            )
        }

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SuccessGreen
            )
        ) {
            Text(
                text = confirmText,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = White
                )
            )
        }
    }
}

/**
 * 新增记录按钮（右上角用）
 */
@Composable
fun AddRecordButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(36.dp),
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
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Text(
                text = "新增记录",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}
