package com.babycare.data.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.babycare.data.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * 数据导出管理器
 * 支持导出为 JSON 和 CSV 格式
 */
class DataExportManager(private val context: Context) {

    companion object {
        const val EXPORT_DIR = "BabyCareExports"
        const val JSON_FILE = "babycare_data.json"
        const val CSV_FILE = "babycare_records.csv"
    }

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    /**
     * 导出所有数据为 JSON
     * @param records 所有记录列表
     * @return 导出文件的 URI，失败返回 null
     */
    suspend fun exportToJson(records: List<Record>): Uri? = withContext(Dispatchers.IO) {
        try {
            val exportData = ExportData(
                exportTime = System.currentTimeMillis(),
                appVersion = "1.0.0",
                records = records.map { record ->
                    RecordExport(
                        id = record.id,
                        type = record.type.name,
                        startTime = record.startTime,
                        endTime = record.endTime,
                        duration = record.duration,
                        note = record.note,
                        createdAt = record.createdAt
                    )
                }
            )

            val jsonString = gson.toJson(exportData)

            val exportDir = File(context.getExternalFilesDir(null), EXPORT_DIR).apply {
                if (!exists()) mkdirs()
            }

            val file = File(exportDir, "export_${getTimestamp()}.json")
            FileWriter(file).use { writer ->
                writer.write(jsonString)
            }

            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 导出记录为 CSV 格式
     * @param records 所有记录
     * @return 导出文件的 URI，失败返回 null
     */
    suspend fun exportToCsv(records: List<Record>): Uri? = withContext(Dispatchers.IO) {
        try {
            val exportDir = File(context.getExternalFilesDir(null), EXPORT_DIR).apply {
                if (!exists()) mkdirs()
            }

            val file = File(exportDir, "export_${getTimestamp()}.csv")
            FileWriter(file).use { writer ->
                // 写入 CSV 头部
                writer.appendLine("ID,类型,开始时间,结束时间,时长(分钟),备注,创建时间")

                // 写入数据
                records.forEach { record ->
                    writer.appendLine(
                        "${record.id}," +
                        "${getTypeDisplayName(record.type)}," +
                        "${formatDateTime(record.startTime)}," +
                        "${record.endTime?.let { formatDateTime(it) } ?: ""}," +
                        "${record.duration ?: ""}," +
                        "${record.note?.replace(",", ";") ?: ""}," +
                        "${formatDateTime(record.createdAt)}"
                    )
                }
            }

            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 分享导出的文件
     */
    fun shareExportedFile(uri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "*/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "分享数据"))
    }

    /**
     * 获取所有导出文件
     */
    fun getExportedFiles(): List<File> {
        val exportDir = File(context.getExternalFilesDir(null), EXPORT_DIR)
        return exportDir.listFiles()?.toList() ?: emptyList()
    }

    /**
     * 删除导出文件
     */
    fun deleteExportedFile(file: File): Boolean {
        return file.delete()
    }

    private fun getTimestamp(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    }

    private fun formatDateTime(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    private fun getTypeDisplayName(type: RecordType): String {
        return when (type) {
            RecordType.BREAST_FEEDING -> "母乳喂养"
            RecordType.BOTTLE_FEEDING -> "奶瓶喂养"
            RecordType.DIAPER -> "换尿布"
            RecordType.SLEEP -> "睡眠"
            RecordType.FOOD -> "辅食"
            RecordType.GROWTH -> "生长发育"
            RecordType.TEMPERATURE -> "体温"
            RecordType.VACCINE -> "疫苗"
            RecordType.MEDICATION -> "用药"
            RecordType.SUPPLEMENT -> "补剂"
            RecordType.PUMP -> "吸奶器"
            RecordType.ACTIVITY -> "活动"
            RecordType.NOTE -> "随手记"
        }
    }
}

/**
 * 导出数据结构
 */
data class ExportData(
    val exportTime: Long,
    val appVersion: String,
    val records: List<RecordExport>
)

data class RecordExport(
    val id: String,
    val type: String,
    val startTime: Long,
    val endTime: Long?,
    val duration: Int?,
    val note: String?,
    val createdAt: Long
)

/**
 * 导出结果
 */
sealed class ExportResult {
    data class Success(val uri: Uri, val fileName: String) : ExportResult()
    data class Error(val message: String) : ExportResult()
}
