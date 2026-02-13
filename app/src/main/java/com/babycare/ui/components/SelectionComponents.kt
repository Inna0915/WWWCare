package com.babycare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

/**
 * 可选择的标签按钮
 */
@Composable
fun SelectableTag(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    selectedColor: Color = PrimaryPink,
    unselectedColor: Color = Gray100
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) selectedColor else unselectedColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon?.invoke()
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = if (isSelected) White else TextPrimary
                )
            )
        }
    }
}

/**
 * 标签组 - 一行多个选项
 */
@Composable
fun TagGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = PrimaryPink
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEach { option ->
            SelectableTag(
                text = option,
                isSelected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                modifier = Modifier.weight(1f),
                selectedColor = selectedColor
            )
        }
    }
}

/**
 * 多行标签组 - 换行排列
 */
@Composable
fun FlowTagGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = LightOrange
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val chunkedOptions = options.chunked(3)
        chunkedOptions.forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowOptions.forEach { option ->
                    SelectableTag(
                        text = option,
                        isSelected = option == selectedOption,
                        onClick = { onOptionSelected(option) },
                        modifier = Modifier.weight(1f),
                        selectedColor = selectedColor
                    )
                }
                // 填充剩余空间
                if (rowOptions.size < 3) {
                    repeat(3 - rowOptions.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/**
 * 颜色选择圆点
 */
@Composable
fun ColorSelector(
    color: Color,
    colorName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (isSelected) {
                        Modifier.border(3.dp, PrimaryPink, CircleShape)
                    } else Modifier
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Text(
                    text = "✓",
                    color = White,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Text(
            text = colorName,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary
        )
    }
}

/**
 * 颜色选择行（包含拍照按钮）
 */
@Composable
fun ColorSelectorRow(
    colors: List<Pair<Color, String>>,
    selectedColor: Color?,
    onColorSelected: (Color) -> Unit,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        // 拍照按钮
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(1.dp, LightOrange, CircleShape)
                    .clickable(onClick = onPhotoClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "拍照",
                    tint = LightOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 颜色选项
        colors.forEach { (color, name) ->
            ColorSelector(
                color = color,
                colorName = name,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) }
            )
        }
    }
}

/**
 * 奶粉品牌标签
 */
@Composable
fun BrandTag(
    brandName: String,
    stage: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(LightBlue, PrimaryPinkLight)
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = brandName,
            style = MaterialTheme.typography.bodyMedium,
            color = White
        )
        Text(
            text = stage,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = White
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(White.copy(alpha = 0.3f))
                .clickable(onClick = onClear),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "×",
                color = White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 情绪选择表情
 */
@Composable
fun MoodSelector(
    mood: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (icon, backgroundColor) = when (mood) {
        "like" -> "😊" to Color(0xFFFFE4E1)
        "neutral" -> "😐" to Color(0xFFF5F5F5)
        "dislike" -> "😢" to Color(0xFFE3F2FD)
        "allergy" -> "😫" to Color(0xFFFFF3E0)
        else -> "😐" to Gray100
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .then(
                    if (isSelected) {
                        Modifier.border(2.dp, PrimaryPink, CircleShape)
                    } else Modifier
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) PrimaryPink else TextSecondary
        )
    }
}
