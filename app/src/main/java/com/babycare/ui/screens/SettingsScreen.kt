package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onBabyInfoClick: () -> Unit = {},
    onDataBackupClick: () -> Unit = {},
    onDataRestoreClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    viewModel: RecordViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "设置",
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
                    containerColor = White
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
            Spacer(modifier = Modifier.height(16.dp))

            // 宝宝信息卡片
            BabyInfoCard(onClick = onBabyInfoClick)

            Spacer(modifier = Modifier.height(16.dp))

            // 数据管理
            SettingsSection(title = "数据管理") {
                SettingsItem(
                    icon = Icons.Default.Backup,
                    iconBackgroundColor = LightBlue,
                    title = "数据备份",
                    subtitle = "备份到本地或云端",
                    onClick = onDataBackupClick
                )
                Divider(color = Gray200, thickness = 0.5.dp)
                SettingsItem(
                    icon = Icons.Default.Restore,
                    iconBackgroundColor = LightGreen,
                    title = "数据恢复",
                    subtitle = "从备份恢复数据",
                    onClick = onDataRestoreClick
                )
                Divider(color = Gray200, thickness = 0.5.dp)
                SettingsItem(
                    icon = Icons.Default.FileDownload,
                    iconBackgroundColor = LightOrange,
                    title = "导出数据",
                    subtitle = "导出为Excel或JSON",
                    onClick = onExportClick
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 提醒设置
            SettingsSection(title = "提醒设置") {
                var notificationsEnabled by remember { mutableStateOf(true) }
                var soundEnabled by remember { mutableStateOf(true) }
                var vibrationEnabled by remember { mutableStateOf(true) }

                SwitchSettingItem(
                    icon = Icons.Default.Notifications,
                    iconBackgroundColor = PrimaryPink,
                    title = "开启提醒",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
                Divider(color = Gray200, thickness = 0.5.dp)
                SwitchSettingItem(
                    icon = Icons.Default.VolumeUp,
                    iconBackgroundColor = LightBlue,
                    title = "提醒声音",
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it },
                    enabled = notificationsEnabled
                )
                Divider(color = Gray200, thickness = 0.5.dp)
                SwitchSettingItem(
                    icon = Icons.Default.Vibration,
                    iconBackgroundColor = LightGreen,
                    title = "震动提醒",
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it },
                    enabled = notificationsEnabled
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 应用设置
            SettingsSection(title = "应用设置") {
                var darkModeEnabled by remember { mutableStateOf(false) }

                SwitchSettingItem(
                    icon = Icons.Default.DarkMode,
                    iconBackgroundColor = Color(0xFF5C6BC0),
                    title = "深色模式",
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it }
                )
                Divider(color = Gray200, thickness = 0.5.dp)
                SettingsItem(
                    icon = Icons.Default.Language,
                    iconBackgroundColor = Color(0xFF26A69A),
                    title = "语言",
                    subtitle = "简体中文",
                    onClick = { /* 切换语言 */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 关于
            SettingsSection(title = "关于") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    iconBackgroundColor = Gray400,
                    title = "关于我们",
                    subtitle = "版本 1.0.0",
                    onClick = onAboutClick
                )
                Divider(color = Gray200, thickness = 0.5.dp)
                SettingsItem(
                    icon = Icons.Default.Star,
                    iconBackgroundColor = Color(0xFFFFB300),
                    title = "评分鼓励",
                    subtitle = "去应用商店评分",
                    onClick = { /* 打开应用商店 */ }
                )
                Divider(color = Gray200, thickness = 0.5.dp)
                SettingsItem(
                    icon = Icons.Default.Share,
                    iconBackgroundColor = Color(0xFF42A5F5),
                    title = "分享给朋友",
                    subtitle = "让更多人使用",
                    onClick = { /* 分享应用 */ }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 清除数据按钮（红色警示按钮）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedButton(
                    onClick = { /* 清除所有数据 */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE53935)
                    )
                ) {
                    Text("清除所有数据")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun BabyInfoCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(LightOrange.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👶",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 信息
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "桐桐",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "女宝宝 · 8个月12天",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    )
                )
                Text(
                    text = "出生日期 2025-06-27",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextHint
                    )
                )
            }

            // 箭头
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "编辑",
                tint = Gray400
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    iconBackgroundColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 图标
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBackgroundColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconBackgroundColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 文字
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )
        }

        // 箭头
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Gray400
        )
    }
}

@Composable
private fun SwitchSettingItem(
    icon: ImageVector,
    iconBackgroundColor: Color,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 图标
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBackgroundColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconBackgroundColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 文字
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = if (enabled) TextPrimary else TextSecondary
            ),
            modifier = Modifier.weight(1f)
        )

        // 开关
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryPink,
                checkedTrackColor = PrimaryPink.copy(alpha = 0.5f)
            )
        )
    }
}
