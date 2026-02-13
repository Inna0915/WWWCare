package com.babycare.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.data.export.DataExportManager
import com.babycare.data.export.ExportResult
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataExportScreen(
    onBackClick: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val exportManager = remember { DataExportManager(context) }

    val records by viewModel.allRecords.collectAsState()

    var isExporting by remember { mutableStateOf(false) }
    var exportResult by remember { mutableStateOf<ExportResult?>(null) }
    var showResultDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "数据导出",
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

            // 数据概览
            DataOverviewCard(records.size)

            Spacer(modifier = Modifier.height(16.dp))

            // 导出选项
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "选择导出格式",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // JSON 导出选项
                    ExportOptionItem(
                        icon = Icons.Default.Code,
                        iconColor = Color(0xFF4CAF50),
                        title = "导出为 JSON",
                        description = "适合数据备份和迁移",
                        onClick = {
                            scope.launch {
                                isExporting = true
                                val uri = exportManager.exportToJson(records)
                                isExporting = false
                                exportResult = if (uri != null) {
                                    ExportResult.Success(uri, "export.json")
                                } else {
                                    ExportResult.Error("导出失败")
                                }
                                showResultDialog = true
                            }
                        }
                    )

                    Divider(color = Gray200, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                    // CSV 导出选项
                    ExportOptionItem(
                        icon = Icons.Default.TableChart,
                        iconColor = Color(0xFF2196F3),
                        title = "导出为 CSV",
                        description = "适合查看和分析数据",
                        onClick = {
                            scope.launch {
                                isExporting = true
                                val uri = exportManager.exportToCsv(records)
                                isExporting = false
                                exportResult = if (uri != null) {
                                    ExportResult.Success(uri, "export.csv")
                                } else {
                                    ExportResult.Error("导出失败")
                                }
                                showResultDialog = true
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 历史导出文件
            ExportHistorySection(exportManager)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 导出中加载框
    if (isExporting) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("正在导出") },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("请稍候...")
                }
            },
            confirmButton = { }
        )
    }

    // 导出结果对话框
    if (showResultDialog) {
        when (val result = exportResult) {
            is ExportResult.Success -> {
                SuccessDialog(
                    fileName = result.fileName,
                    onDismiss = { showResultDialog = false },
                    onShare = {
                        exportManager.shareExportedFile(result.uri)
                        showResultDialog = false
                    }
                )
            }
            is ExportResult.Error -> {
                AlertDialog(
                    onDismissRequest = { showResultDialog = false },
                    title = { Text("导出失败") },
                    text = { Text(result.message) },
                    confirmButton = {
                        TextButton(onClick = { showResultDialog = false }) {
                            Text("确定")
                        }
                    }
                )
            }
            null -> { }
        }
    }
}

@Composable
private fun DataOverviewCard(recordCount: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPink.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Storage,
                    contentDescription = null,
                    tint = PrimaryPink,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "数据概览",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    )
                )
                Text(
                    text = "$recordCount 条记录",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun ExportOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Gray400
        )
    }
}

@Composable
private fun ExportHistorySection(exportManager: DataExportManager) {
    val files = remember { exportManager.getExportedFiles() }

    if (files.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "历史导出文件",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                files.take(5).forEach { file ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (file.name.endsWith(".json"))
                                Icons.Default.Code else Icons.Default.TableChart,
                            contentDescription = null,
                            tint = if (file.name.endsWith(".json")) Color(0xFF4CAF50) else Color(0xFF2196F3),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = file.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${file.length() / 1024} KB",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary
                                )
                            )
                        }

                        IconButton(
                            onClick = { exportManager.shareExportedFile(Uri.fromFile(file)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "分享",
                                tint = PrimaryPink
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessDialog(
    fileName: String,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(48.dp)
            )
        },
        title = { Text("导出成功") },
        text = {
            Text("文件 $fileName 已保存到导出目录")
        },
        confirmButton = {
            Button(
                onClick = onShare,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("分享")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}
