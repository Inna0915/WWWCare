package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

data class QuickMenuItem(
    val icon: String,
    val label: String,
    val backgroundColor: Color,
    val onClick: () -> Unit
)

@Composable
fun QuickAddMenu(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onItemClick: (String) -> Unit
) {
    if (!isVisible) return

    val menuItems = listOf(
        // 第一行
        listOf(
            QuickMenuItem("🌱", "身高体重", Color(0xFFE8F5E9)) { onItemClick("growth") },
            QuickMenuItem("🌡️", "体温", Color(0xFFFFEBEE)) { onItemClick("temperature") },
            QuickMenuItem("💊", "用药", Color(0xFFE3F2FD)) { onItemClick("medication") },
            QuickMenuItem("👋", "黄疸", Color(0xFFFFF9C4)) { onItemClick("jaundice") }
        ),
        // 第二行
        listOf(
            QuickMenuItem("📷", "随手拍", Color(0xFFE0F7FA)) { onItemClick("photo") },
            QuickMenuItem("✏️", "随手记", Color(0xFFF3E5F5)) { onItemClick("note") },
            QuickMenuItem("💧", "补剂", Color(0xFFFFF3E0)) { onItemClick("supplement") },
            QuickMenuItem("🥣", "辅食喂养", Color(0xFFE8F5E9)) { onItemClick("food") }
        ),
        // 第三行
        listOf(
            QuickMenuItem("💉", "疫苗", Color(0xFFE0F2F1)) { onItemClick("vaccine") },
            QuickMenuItem("🍼", "奶瓶喂养", Color(0xFFE3F2FD)) { onItemClick("bottle") },
            QuickMenuItem("🤱", "左侧母乳", Color(0xFFFFE4E1)) { onItemClick("breast_left") },
            QuickMenuItem("🤱", "右侧母乳", Color(0xFFFFE4E1)) { onItemClick("breast_right") }
        ),
        // 第四行
        listOf(
            QuickMenuItem("🦆", "活动", Color(0xFFFFF9C4)) { onItemClick("activity") },
            QuickMenuItem("🔌", "吸奶器", Color(0xFFE8F5E9)) { onItemClick("pump") },
            QuickMenuItem("😴", "睡眠", Color(0xFFF3E5F5)) { onItemClick("sleep") },
            QuickMenuItem("👶", "换尿布", Color(0xFFFFE0B2)) { onItemClick("diaper") }
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss)
    ) {
        // 底部菜单卡片
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(White)
                .clickable(enabled = false) { }
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 关闭按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        tint = TextSecondary
                    )
                }
            }

            // 菜单网格
            menuItems.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowItems.forEach { item ->
                        QuickMenuButton(
                            icon = item.icon,
                            label = item.label,
                            backgroundColor = item.backgroundColor,
                            onClick = item.onClick
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun QuickMenuButton(
    icon: String,
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = backgroundColor.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
