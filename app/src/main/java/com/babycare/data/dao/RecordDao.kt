package com.babycare.data.dao

import androidx.room.*
import com.babycare.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * 记录数据访问对象
 */
@Dao
interface RecordDao {
    // ==================== 基础记录操作 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record): Long

    @Update
    suspend fun updateRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("SELECT * FROM records WHERE id = :id")
    suspend fun getRecordById(id: String): Record?

    @Query("SELECT * FROM records ORDER BY start_time DESC")
    fun getAllRecords(): Flow<List<Record>>

    @Query("SELECT * FROM records WHERE type = :type ORDER BY start_time DESC")
    fun getRecordsByType(type: RecordType): Flow<List<Record>>

    @Query("SELECT * FROM records WHERE DATE(start_time/1000, 'unixepoch') = DATE(:timestamp/1000, 'unixepoch') ORDER BY start_time DESC")
    fun getRecordsByDate(timestamp: Long): Flow<List<Record>>

    @Query("SELECT * FROM records WHERE start_time BETWEEN :startTime AND :endTime ORDER BY start_time DESC")
    fun getRecordsBetween(startTime: Long, endTime: Long): Flow<List<Record>>

    @Query("SELECT * FROM records WHERE type IN (:types) ORDER BY start_time DESC")
    fun getRecordsByTypes(types: List<RecordType>): Flow<List<Record>>

    // ==================== 统计查询 ====================

    @Query("SELECT COUNT(*) FROM records WHERE type = :type AND DATE(start_time/1000, 'unixepoch') = DATE(:timestamp/1000, 'unixepoch')")
    suspend fun getCountByTypeAndDate(type: RecordType, timestamp: Long): Int

    @Query("SELECT SUM(duration) FROM records WHERE type = :type AND start_time BETWEEN :startTime AND :endTime")
    suspend fun getTotalDurationByType(type: RecordType, startTime: Long, endTime: Long): Int?

    @Query("SELECT * FROM records WHERE type = :type ORDER BY start_time DESC LIMIT 1")
    suspend fun getLatestRecordByType(type: RecordType): Record?

    // ==================== 母乳喂养详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreastFeedingDetail(detail: BreastFeedingDetail)

    @Query("SELECT * FROM breast_feeding_details WHERE record_id = :recordId")
    suspend fun getBreastFeedingDetail(recordId: String): BreastFeedingDetail?

    // ==================== 奶瓶喂养详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBottleFeedingDetail(detail: BottleFeedingDetail)

    @Query("SELECT * FROM bottle_feeding_details WHERE record_id = :recordId")
    suspend fun getBottleFeedingDetail(recordId: String): BottleFeedingDetail?

    @Query("SELECT SUM(amount) FROM bottle_feeding_details INNER JOIN records ON bottle_feeding_details.record_id = records.id WHERE records.type = 'BOTTLE_FEEDING' AND DATE(records.start_time/1000, 'unixepoch') = DATE(:timestamp/1000, 'unixepoch')")
    suspend fun getTotalBottleAmountByDate(timestamp: Long): Int?

    // ==================== 换尿布详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaperDetail(detail: DiaperDetail)

    @Query("SELECT * FROM diaper_details WHERE record_id = :recordId")
    suspend fun getDiaperDetail(recordId: String): DiaperDetail?

    @Query("SELECT COUNT(*) FROM diaper_details INNER JOIN records ON diaper_details.record_id = records.id WHERE records.type = 'DIAPER' AND DATE(records.start_time/1000, 'unixepoch') = DATE(:timestamp/1000, 'unixepoch')")
    suspend fun getDiaperCountByDate(timestamp: Long): Int

    // ==================== 睡眠详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleepDetail(detail: SleepDetail)

    @Query("SELECT * FROM sleep_details WHERE record_id = :recordId")
    suspend fun getSleepDetail(recordId: String): SleepDetail?

    // ==================== 辅食详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodDetail(detail: FoodDetail)

    @Query("SELECT * FROM food_details WHERE record_id = :recordId")
    suspend fun getFoodDetail(recordId: String): FoodDetail?

    // ==================== 生长发育详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrowthDetail(detail: GrowthDetail)

    @Query("SELECT * FROM growth_details WHERE record_id = :recordId")
    suspend fun getGrowthDetail(recordId: String): GrowthDetail?

    @Query("SELECT * FROM growth_details INNER JOIN records ON growth_details.record_id = records.id ORDER BY records.start_time DESC")
    fun getAllGrowthRecords(): Flow<List<GrowthDetailWithRecord>>

    // ==================== 体温详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemperatureDetail(detail: TemperatureDetail)

    @Query("SELECT * FROM temperature_details WHERE record_id = :recordId")
    suspend fun getTemperatureDetail(recordId: String): TemperatureDetail?

    // ==================== 疫苗详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccineDetail(detail: VaccineDetail)

    @Query("SELECT * FROM vaccine_details WHERE record_id = :recordId")
    suspend fun getVaccineDetail(recordId: String): VaccineDetail?

    @Query("SELECT * FROM vaccine_details ORDER BY planned_date ASC")
    fun getAllVaccineRecords(): Flow<List<VaccineDetail>>

    // ==================== 用药详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationDetail(detail: MedicationDetail)

    @Query("SELECT * FROM medication_details WHERE record_id = :recordId")
    suspend fun getMedicationDetail(recordId: String): MedicationDetail?

    // ==================== 补剂详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplementDetail(detail: SupplementDetail)

    @Query("SELECT * FROM supplement_details WHERE record_id = :recordId")
    suspend fun getSupplementDetail(recordId: String): SupplementDetail?

    // ==================== 吸奶器详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPumpDetail(detail: PumpDetail)

    @Query("SELECT * FROM pump_details WHERE record_id = :recordId")
    suspend fun getPumpDetail(recordId: String): PumpDetail?

    @Query("SELECT SUM(total_amount) FROM pump_details INNER JOIN records ON pump_details.record_id = records.id WHERE DATE(records.start_time/1000, 'unixepoch') = DATE(:timestamp/1000, 'unixepoch')")
    suspend fun getTotalPumpAmountByDate(timestamp: Long): Int?

    // ==================== 活动详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityDetail(detail: ActivityDetail)

    @Query("SELECT * FROM activity_details WHERE record_id = :recordId")
    suspend fun getActivityDetail(recordId: String): ActivityDetail?

    // ==================== 随手记详情 ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteDetail(detail: NoteDetail)

    @Query("SELECT * FROM note_details WHERE record_id = :recordId")
    suspend fun getNoteDetail(recordId: String): NoteDetail?

    // ==================== 批量删除 ====================

    @Query("DELETE FROM records WHERE id = :recordId")
    suspend fun deleteRecordById(recordId: String)
}

// ==================== 关联查询数据类 ====================

data class GrowthDetailWithRecord(
    @Embedded val growthDetail: GrowthDetail,
    @Relation(
        parentColumn = "record_id",
        entityColumn = "id"
    )
    val record: Record
)
