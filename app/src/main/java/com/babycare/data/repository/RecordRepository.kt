package com.babycare.data.repository

import com.babycare.data.dao.RecordDao
import com.babycare.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * 记录数据仓库
 * 提供统一的数据操作接口，封装数据库访问逻辑
 */
class RecordRepository(private val recordDao: RecordDao) {

    // ==================== 基础记录操作 ====================

    fun getAllRecords(): Flow<List<Record>> = recordDao.getAllRecords()

    fun getRecordsByType(type: RecordType): Flow<List<Record>> = recordDao.getRecordsByType(type)

    fun getRecordsByDate(timestamp: Long): Flow<List<Record>> = recordDao.getRecordsByDate(timestamp)

    fun getRecordsBetween(startTime: Long, endTime: Long): Flow<List<Record>> =
        recordDao.getRecordsBetween(startTime, endTime)

    suspend fun getRecordById(id: String): Record? = recordDao.getRecordById(id)

    suspend fun deleteRecord(record: Record) = recordDao.deleteRecord(record)

    // ==================== 母乳喂养 ====================

    suspend fun addBreastFeedingRecord(
        startTime: Long,
        endTime: Long?,
        leftDuration: Int,
        rightDuration: Int,
        side: BreastSide,
        order: String?,
        note: String?
    ): String {
        val duration = ((endTime ?: startTime) - startTime) / 60000
        val record = Record(
            type = RecordType.BREAST_FEEDING,
            startTime = startTime,
            endTime = endTime,
            duration = duration.toInt(),
            note = note
        )
        recordDao.insertRecord(record)

        val detail = BreastFeedingDetail(
            recordId = record.id,
            leftDuration = leftDuration,
            rightDuration = rightDuration,
            side = side,
            order = order
        )
        recordDao.insertBreastFeedingDetail(detail)
        return record.id
    }

    suspend fun getBreastFeedingDetail(recordId: String): BreastFeedingDetail? =
        recordDao.getBreastFeedingDetail(recordId)

    // ==================== 奶瓶喂养 ====================

    suspend fun addBottleFeedingRecord(
        startTime: Long,
        amount: Int,
        feedingType: BottleFeedingType,
        brand: String?,
        stage: String?,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.BOTTLE_FEEDING,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = BottleFeedingDetail(
            recordId = record.id,
            amount = amount,
            feedingType = feedingType,
            brand = brand,
            stage = stage
        )
        recordDao.insertBottleFeedingDetail(detail)
        return record.id
    }

    suspend fun getBottleFeedingDetail(recordId: String): BottleFeedingDetail? =
        recordDao.getBottleFeedingDetail(recordId)

    suspend fun getTotalBottleAmountByDate(timestamp: Long): Int =
        recordDao.getTotalBottleAmountByDate(timestamp) ?: 0

    // ==================== 换尿布 ====================

    suspend fun addDiaperRecord(
        startTime: Long,
        type: DiaperType,
        weight: DiaperWeight,
        poopState: String?,
        poopColor: String?,
        photoUri: String?,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.DIAPER,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = DiaperDetail(
            recordId = record.id,
            type = type,
            weight = weight,
            poopState = poopState,
            poopColor = poopColor,
            photoUri = photoUri
        )
        recordDao.insertDiaperDetail(detail)
        return record.id
    }

    suspend fun getDiaperDetail(recordId: String): DiaperDetail? =
        recordDao.getDiaperDetail(recordId)

    suspend fun getDiaperCountByDate(timestamp: Long): Int =
        recordDao.getDiaperCountByDate(timestamp)

    // ==================== 睡眠 ====================

    suspend fun addSleepRecord(
        startTime: Long,
        endTime: Long?,
        sleepMethod: String?,
        quality: SleepQuality?,
        note: String?
    ): String {
        val duration = if (endTime != null) ((endTime - startTime) / 60000).toInt() else null
        val record = Record(
            type = RecordType.SLEEP,
            startTime = startTime,
            endTime = endTime,
            duration = duration,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = SleepDetail(
            recordId = record.id,
            sleepMethod = sleepMethod,
            quality = quality
        )
        recordDao.insertSleepDetail(detail)
        return record.id
    }

    suspend fun getSleepDetail(recordId: String): SleepDetail? =
        recordDao.getSleepDetail(recordId)

    // ==================== 辅食 ====================

    suspend fun addFoodRecord(
        startTime: Long,
        foodType: String,
        texture: FoodTexture?,
        amount: Int?,
        unit: String,
        feedback: FoodFeedback?,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.FOOD,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = FoodDetail(
            recordId = record.id,
            foodType = foodType,
            texture = texture,
            amount = amount,
            unit = unit,
            feedback = feedback
        )
        recordDao.insertFoodDetail(detail)
        return record.id
    }

    // ==================== 生长发育 ====================

    suspend fun addGrowthRecord(
        startTime: Long,
        height: Float?,
        weight: Float?,
        headCircumference: Float?,
        footLength: Float?,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.GROWTH,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = GrowthDetail(
            recordId = record.id,
            height = height,
            weight = weight,
            headCircumference = headCircumference,
            footLength = footLength
        )
        recordDao.insertGrowthDetail(detail)
        return record.id
    }

    fun getAllGrowthRecords(): Flow<List<GrowthDetailWithRecord>> =
        recordDao.getAllGrowthRecords()

    // ==================== 体温 ====================

    suspend fun addTemperatureRecord(
        startTime: Long,
        temperature: Float,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.TEMPERATURE,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = TemperatureDetail(
            recordId = record.id,
            temperature = temperature
        )
        recordDao.insertTemperatureDetail(detail)
        return record.id
    }

    // ==================== 疫苗 ====================

    suspend fun addVaccineRecord(
        startTime: Long,
        vaccineName: String,
        vaccineType: VaccineType,
        doseNumber: String,
        description: String?,
        isPlanned: Boolean,
        plannedDate: Long?,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.VACCINE,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = VaccineDetail(
            recordId = record.id,
            vaccineName = vaccineName,
            vaccineType = vaccineType,
            doseNumber = doseNumber,
            description = description,
            isPlanned = isPlanned,
            plannedDate = plannedDate
        )
        recordDao.insertVaccineDetail(detail)
        return record.id
    }

    fun getAllVaccineRecords(): Flow<List<VaccineDetail>> =
        recordDao.getAllVaccineRecords()

    // ==================== 用药 ====================

    suspend fun addMedicationRecord(
        startTime: Long,
        medicineName: String,
        dosage: String,
        unit: String,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.MEDICATION,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = MedicationDetail(
            recordId = record.id,
            medicineName = medicineName,
            dosage = dosage,
            unit = unit
        )
        recordDao.insertMedicationDetail(detail)
        return record.id
    }

    // ==================== 补剂 ====================

    suspend fun addSupplementRecord(
        startTime: Long,
        supplementName: String,
        dosage: String,
        unit: String,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.SUPPLEMENT,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = SupplementDetail(
            recordId = record.id,
            supplementName = supplementName,
            dosage = dosage,
            unit = unit
        )
        recordDao.insertSupplementDetail(detail)
        return record.id
    }

    // ==================== 吸奶器 ====================

    suspend fun addPumpRecord(
        startTime: Long,
        leftAmount: Int?,
        rightAmount: Int?,
        totalAmount: Int?,
        leftDuration: Int?,
        rightDuration: Int?,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.PUMP,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = PumpDetail(
            recordId = record.id,
            leftAmount = leftAmount,
            rightAmount = rightAmount,
            totalAmount = totalAmount ?: (leftAmount ?: 0) + (rightAmount ?: 0),
            leftDuration = leftDuration,
            rightDuration = rightDuration
        )
        recordDao.insertPumpDetail(detail)
        return record.id
    }

    suspend fun getTotalPumpAmountByDate(timestamp: Long): Int =
        recordDao.getTotalPumpAmountByDate(timestamp) ?: 0

    // ==================== 活动 ====================

    suspend fun addActivityRecord(
        startTime: Long,
        endTime: Long?,
        activityType: String?,
        note: String?
    ): String {
        val duration = if (endTime != null) ((endTime - startTime) / 60000).toInt() else null
        val record = Record(
            type = RecordType.ACTIVITY,
            startTime = startTime,
            endTime = endTime,
            duration = duration,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = ActivityDetail(
            recordId = record.id,
            activityType = activityType
        )
        recordDao.insertActivityDetail(detail)
        return record.id
    }

    // ==================== 随手记 ====================

    suspend fun addNoteRecord(
        startTime: Long,
        content: String,
        photoUri: String?,
        note: String?
    ): String {
        val record = Record(
            type = RecordType.NOTE,
            startTime = startTime,
            note = note
        )
        recordDao.insertRecord(record)

        val detail = NoteDetail(
            recordId = record.id,
            content = content,
            photoUri = photoUri
        )
        recordDao.insertNoteDetail(detail)
        return record.id
    }

    // ==================== 统计数据 ====================

    /**
     * 获取今日概览统计
     */
    suspend fun getTodayOverview(timestamp: Long): DailyOverview {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dayStart = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val dayEnd = calendar.timeInMillis

        return DailyOverview(
            breastFeedingCount = recordDao.getCountByTypeAndDate(RecordType.BREAST_FEEDING, timestamp) +
                                 recordDao.getCountByTypeAndDate(RecordType.BOTTLE_FEEDING, timestamp),
            sleepDuration = recordDao.getTotalDurationByType(RecordType.SLEEP, dayStart, dayEnd) ?: 0,
            diaperCount = recordDao.getDiaperCountByDate(timestamp),
            supplementCount = recordDao.getCountByTypeAndDate(RecordType.SUPPLEMENT, timestamp)
        )
    }
}

/**
 * 每日概览数据
 */
data class DailyOverview(
    val breastFeedingCount: Int,
    val sleepDuration: Int,
    val diaperCount: Int,
    val supplementCount: Int
)
