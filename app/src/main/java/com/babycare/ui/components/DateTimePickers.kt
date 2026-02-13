package com.babycare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDate: Long = System.currentTimeMillis(),
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onConfirm(it)
                    }
                    onDismiss()
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

/**
 * 时间选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    initialMinute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "选择时间",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                TimePicker(state = timePickerState)

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onConfirm(timePickerState.hour, timePickerState.minute)
                            onDismiss()
                        }
                    ) {
                        Text("确定")
                    }
                }
            }
        }
    }
}

/**
 * 日期时间选择对话框（组合）
 */
@Composable
fun DateTimePickerDialog(
    initialDateTime: Long = System.currentTimeMillis(),
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(initialDateTime) }

    val calendar = Calendar.getInstance()

    if (showDatePicker) {
        DatePickerDialog(
            initialDate = initialDateTime,
            onDismiss = onDismiss,
            onConfirm = { date ->
                selectedDate = date
                showDatePicker = false
                showTimePicker = true
            }
        )
    }

    if (showTimePicker) {
        calendar.timeInMillis = initialDateTime
        TimePickerDialog(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE),
            onDismiss = {
                showTimePicker = false
                onDismiss()
            },
            onConfirm = { hour, minute ->
                val finalCalendar = Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onConfirm(finalCalendar.timeInMillis)
            }
        )
    }
}

/**
 * 持续时间选择对话框
 */
@Composable
fun DurationPickerDialog(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (hours: Int, minutes: Int) -> Unit
) {
    var hours by remember { mutableIntStateOf(initialHours) }
    var minutes by remember { mutableIntStateOf(initialMinutes) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "选择持续时间",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 小时选择
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "小时",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        NumberPicker(
                            value = hours,
                            onValueChange = { hours = it },
                            range = 0..23
                        )
                    }

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    // 分钟选择
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "分钟",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        NumberPicker(
                            value = minutes,
                            onValueChange = { minutes = it },
                            range = 0..59
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onConfirm(hours, minutes)
                            onDismiss()
                        }
                    ) {
                        Text("确定")
                    }
                }
            }
        }
    }
}

/**
 * 数字选择器（用于持续时间）
 */
@Composable
private fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                if (value < range.last) onValueChange(value + 1)
            }
        ) {
            Text("▲", style = MaterialTheme.typography.titleMedium)
        }

        Text(
            text = String.format("%02d", value),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        IconButton(
            onClick = {
                if (value > range.first) onValueChange(value - 1)
            }
        ) {
            Text("▼", style = MaterialTheme.typography.titleMedium)
        }
    }
}

/**
 * 提醒时间选择对话框
 */
@Composable
fun ReminderTimePickerDialog(
    initialHours: Int = 3,
    initialMinutes: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (hours: Int, minutes: Int) -> Unit
) {
    var hours by remember { mutableIntStateOf(initialHours) }
    var minutes by remember { mutableIntStateOf(initialMinutes) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "设置提醒时间",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "将在设定时间后发送提醒通知",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "小时",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        NumberPicker(
                            value = hours,
                            onValueChange = { hours = it },
                            range = 0..48
                        )
                    }

                    Text(
                        text = "小时",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "分钟",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        NumberPicker(
                            value = minutes,
                            onValueChange = { minutes = it },
                            range = 0..59
                        )
                    }

                    Text(
                        text = "分",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onConfirm(hours, minutes)
                            onDismiss()
                        }
                    ) {
                        Text("确定")
                    }
                }
            }
        }
    }
}

/**
 * 格式化日期时间显示
 */
fun formatDateTime(timestamp: Long, pattern: String = "yyyy-MM-dd HH:mm"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
}

fun formatDate(timestamp: Long, pattern: String = "yyyy-MM-dd"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
}

fun formatTime(timestamp: Long, pattern: String = "HH:mm"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
}

/**
 * 解析提醒时间字符串（如 "3小时0分"）
 */
fun parseReminderTime(timeStr: String): Pair<Int, Int> {
    val regex = "(\\d+)小时(\\d+)分".toRegex()
    val matchResult = regex.find(timeStr)
    return if (matchResult != null) {
        val (hours, minutes) = matchResult.destructured
        hours.toInt() to minutes.toInt()
    } else {
        3 to 0 // 默认3小时
    }
}

/**
 * 格式化提醒时间显示
 */
fun formatReminderTime(hours: Int, minutes: Int): String {
    return "${hours}小时${minutes}分"
}
