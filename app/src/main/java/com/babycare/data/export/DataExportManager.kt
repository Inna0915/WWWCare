package com.babycare.data.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.babycare.data.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * 数据导出管理器
 * 支持导出为 JSON 和 Excel 格式
 */
class DataExportManager(private val context: Context) {

    companion object {
        const val EXPORT_DIR = "BabyCareExports"
        const val JSON_FILE = "babycare_data.json"
        const val EXCEL_FILE = "babycare_records.xlsx"
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
     * 导出记录为 Excel
     * @param records 所有记录
     * @return 导出文件的 URI，失败返回 null
     */
    suspend fun exportToExcel(records: List<Record>): Uri? = withContext(Dispatchers.IO) {
        try {
            val workbook = XSSFWorkbook()

            // 创建汇总表
            createSummarySheet(workbook, records)

            // 按类型创建详细表
            createDetailSheets(workbook, records)

            // 保存文件
            val exportDir = File(context.getExternalFilesDir(null), EXPORT_DIR).apply {
                if (!exists()) mkdirs()
            }

            val file = File(exportDir, "export_${getTimestamp()}.xlsx")
            FileOutputStream(file).use { outputStream ->
                workbook.write(outputStream)
            }
            workbook.close()

            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 创建汇总表
     */
    private fun createSummarySheet(workbook: Workbook, records: List<Record>) {
        val sheet = workbook.createSheet("汇总")

        // 标题行
        val headerRow = sheet.createRow(0)
        val headers = listOf("ID", "类型", "开始时间", "结束时间", "时长(分钟)", "备注", "创建时间")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = createHeaderStyle(workbook)
            }
        }

        // 数据行
        records.forEachIndexed { index, record ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(record.id)
            row.createCell(1).setCellValue(getTypeDisplayName(record.type))
            row.createCell(2).setCellValue(formatDateTime(record.startTime))
            row.createCell(3).setCellValue(record.endTime?.let { formatDateTime(it) } ?: "")
            row.createCell(4).setCellValue(record.duration?.toDouble() ?: 0.0)
            row.createCell(5).setCellValue(record.note ?: "")
            row.createCell(6).setCellValue(formatDateTime(record.createdAt))
        }

        // 自动调整列宽
        headers.indices.forEach { sheet.autoSizeColumn(it) }
    }

    /**
     * 创建详细表
     */
    private fun createDetailSheets(workbook: Workbook, records: List<Record>) {
        // 喂养记录表
        val feedingRecords = records.filter {
            it.type == RecordType.BREAST_FEEDING || it.type == RecordType.BOTTLE_FEEDING
        }
        if (feedingRecords.isNotEmpty()) {
            createFeedingSheet(workbook, feedingRecords)
        }

        // 换尿布记录表
        val diaperRecords = records.filter { it.type == RecordType.DIAPER }
        if (diaperRecords.isNotEmpty()) {
            createDiaperSheet(workbook, diaperRecords)
        }

        // 睡眠记录表
        val sleepRecords = records.filter { it.type == RecordType.SLEEP }
        if (sleepRecords.isNotEmpty()) {
            createSleepSheet(workbook, sleepRecords)
        }

        // 生长发育记录表
        val growthRecords = records.filter { it.type == RecordType.GROWTH }
        if (growthRecords.isNotEmpty()) {
            createGrowthSheet(workbook, growthRecords)
        }
    }

    private fun createFeedingSheet(workbook: Workbook, records: List<Record>) {
        val sheet = workbook.createSheet("喂养记录")
        val headerRow = sheet.createRow(0)
        listOf("时间", "类型", "奶量(ml)", "时长(分钟)", "备注").forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = createHeaderStyle(workbook)
            }
        }

        records.forEachIndexed { index, record ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(formatDateTime(record.startTime))
            row.createCell(1).setCellValue(getTypeDisplayName(record.type))
            row.createCell(2).setCellValue("") // 奶量需要详情
            row.createCell(3).setCellValue(record.duration?.toDouble() ?: 0.0)
            row.createCell(4).setCellValue(record.note ?: "")
        }
    }

    private fun createDiaperSheet(workbook: Workbook, records: List<Record>) {
        val sheet = workbook.createSheet("换尿布记录")
        val headerRow = sheet.createRow(0)
        listOf("时间", "类型", "状态", "颜色", "备注").forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = createHeaderStyle(workbook)
            }
        }

        records.forEachIndexed { index, record ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(formatDateTime(record.startTime))
            row.createCell(1).setCellValue(getTypeDisplayName(record.type))
            row.createCell(2).setCellValue("") // 状态需要详情
            row.createCell(3).setCellValue("") // 颜色需要详情
            row.createCell(4).setCellValue(record.note ?: "")
        }
    }

    private fun createSleepSheet(workbook: Workbook, records: List<Record>) {
        val sheet = workbook.createSheet("睡眠记录")
        val headerRow = sheet.createRow(0)
        listOf("开始时间", "结束时间", "时长(分钟)", "入睡方式", "睡眠质量", "备注").forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = createHeaderStyle(workbook)
            }
        }

        records.forEachIndexed { index, record ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(formatDateTime(record.startTime))
            row.createCell(1).setCellValue(record.endTime?.let { formatDateTime(it) } ?: "")
            row.createCell(2).setCellValue(record.duration?.toDouble() ?: 0.0)
            row.createCell(3).setCellValue("") // 入睡方式需要详情
            row.createCell(4).setCellValue("") // 睡眠质量需要详情
            row.createCell(5).setCellValue(record.note ?: "")
        }
    }

    private fun createGrowthSheet(workbook: Workbook, records: List<Record>) {
        val sheet = workbook.createSheet("生长发育")
        val headerRow = sheet.createRow(0)
        listOf("日期", "身高(cm)", "体重(kg)", "头围(cm)", "脚长(cm)", "备注").forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = createHeaderStyle(workbook)
            }
        }

        records.forEachIndexed { index, record ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(formatDateTime(record.startTime))
            row.createCell(1).setCellValue("") // 身高需要详情
            row.createCell(2).setCellValue("") // 体重需要详情
            row.createCell(3).setCellValue("") // 头围需要详情
            row.createCell(4).setCellValue("") // 脚长需要详情
            row.createCell(5).setCellValue(record.note ?: "")
        }
    }

    private fun createHeaderStyle(workbook: Workbook): CellStyle {
        return workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.LIGHT_BLUE.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            setFont(workbook.createFont().apply {
                bold = true
            })
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
