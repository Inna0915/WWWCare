package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.babycare.data.model.VaccineType
import com.babycare.ui.theme.*
import com.babycare.viewmodel.RecordViewModel
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineScreen(
    onBackClick: () -> Unit,
    onAddVaccineClick: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    // 从 ViewModel 获取疫苗记录
    val vaccineRecords by viewModel.getAllVaccineRecords()
        .collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "疫苗接种管理",
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
                actions = {
                    Button(
                        onClick = onAddVaccineClick,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPink
                        )
                    ) {
                        Text(
                            text = "+ 自费疫苗",
                            style = MaterialTheme.typography.bodySmall,
                            color = White
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
            // 宝宝信息卡片
            BabyInfoCard()

            // 免责声明
            Text(
                text = "以下为推荐接种疫苗时间，实际接种时间以接种站及医生建议为准\n《数据来源及免责声明》",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                ),
                modifier = Modifier.padding(16.dp)
            )

            // 疫苗列表
            if (vaccineRecords.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无疫苗记录，点击右上角添加",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary
                        ),
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else {
                VaccineListFromRecords(records = vaccineRecords)
            }
        }
    }
}

@Composable
private fun BabyInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(PrimaryPinkLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👶",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "桐桐",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "出生日期 2025-06-27",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
private fun VaccineListFromRecords(records: List<com.babycare.data.model.VaccineDetail>) {
    val dateFormatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

    // 按日期分组
    val groupedRecords = records.groupBy { record ->
        record.plannedDate?.let { dateFormatter.format(java.util.Date(it)) } ?: "未计划"
    }.toSortedMap()

    groupedRecords.forEach { (date, vaccines) ->
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // 分组标题
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 疫苗项目
            vaccines.forEach { vaccine ->
                VaccineRecordItemCard(vaccine = vaccine)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun VaccineRecordItemCard(vaccine: com.babycare.data.model.VaccineDetail) {
    val dateFormatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    val typeLabel = when (vaccine.vaccineType) {
        VaccineType.FREE -> "免费"
        VaccineType.PAID -> "自费"
    }
    val isCompleted = !vaccine.isPlanned

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 类型标签
                    val tagColor = if (vaccine.vaccineType == VaccineType.FREE) Color(0xFF4CAF50) else PrimaryPink
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(tagColor.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = typeLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = tagColor
                        )
                    }

                    Text(
                        text = vaccine.vaccineName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    if (vaccine.doseNumber.isNotEmpty()) {
                        Text(
                            text = vaccine.doseNumber,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                vaccine.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2
                    )
                }
            }

            // 完成状态
            if (isCompleted) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dateFormatter.format(java.util.Date(vaccine.plannedDate ?: 0)),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "已完成",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Gray300)
                )
            }
        }
    }
}

data class VaccineGroup(
    val title: String,
    val date: String,
    val vaccines: List<VaccineItem>
)

data class VaccineItem(
    val name: String,
    val dose: String,
    val type: String, // 免费/自费
    val description: String,
    val isCompleted: Boolean,
    val completedDate: String?
)

@Composable
private fun VaccineGroupSection(group: VaccineGroup) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 分组标题
        Text(
            text = "${group.title}  ${group.date}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 疫苗项目
        group.vaccines.forEach { vaccine ->
            VaccineItemCard(vaccine = vaccine)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun VaccineItemCard(vaccine: VaccineItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 类型标签
                    val tagColor = if (vaccine.type == "免费") Color(0xFF4CAF50) else PrimaryPink
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(tagColor.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = vaccine.type,
                            style = MaterialTheme.typography.labelSmall,
                            color = tagColor
                        )
                    }

                    Text(
                        text = vaccine.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    if (vaccine.dose.isNotEmpty()) {
                        Text(
                            text = vaccine.dose,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = vaccine.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2
                )
            }

            // 完成状态
            if (vaccine.isCompleted) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = vaccine.completedDate ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "已完成",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Gray300)
                )
            }
        }
    }
}
