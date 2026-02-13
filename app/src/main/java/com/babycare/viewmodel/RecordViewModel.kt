package com.babycare.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.babycare.data.BabyDatabase
import com.babycare.data.dao.GrowthDetailWithRecord
import com.babycare.data.model.*
import com.babycare.data.repository.DailyOverview
import com.babycare.data.repository.RecordRepository
import com.babycare.worker.ReminderScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 记录 ViewModel
 * 连接 UI 和数据层，处理业务逻辑
 */
class RecordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecordRepository
    private val reminderScheduler = ReminderScheduler(application)

    // 状态流
    private val _allRecords = MutableStateFlow<List<Record>>(emptyList())
    val allRecords: StateFlow<List<Record>> = _allRecords.asStateFlow()

    private val _todayOverview = MutableStateFlow<DailyOverview?>(null)
    val todayOverview: StateFlow<DailyOverview?> = _todayOverview.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val database = BabyDatabase.getDatabase(application)
        repository = RecordRepository(database.recordDao())

        // 加载所有记录
        viewModelScope.launch {
            repository.getAllRecords().collect { records ->
                _allRecords.value = records
            }
        }

        // 加载今日概览
        loadTodayOverview()
    }

    // ==================== 加载数据 ====================

    fun loadTodayOverview() {
        viewModelScope.launch {
            _isLoading.value = true
            val today = System.currentTimeMillis()
            _todayOverview.value = repository.getTodayOverview(today)
            _isLoading.value = false
        }
    }

    fun getRecordsByType(type: RecordType): Flow<List<Record>> {
        return repository.getRecordsByType(type)
    }

    // ==================== 添加记录 ====================

    fun addBreastFeedingRecord(
        startTime: Long,
        endTime: Long?,
        leftDuration: Int,
        rightDuration: Int,
        side: BreastSide,
        order: String?,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addBreastFeedingRecord(
                startTime, endTime, leftDuration, rightDuration, side, order, note
            )
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addBottleFeedingRecord(
        startTime: Long,
        amount: Int,
        feedingType: BottleFeedingType,
        brand: String?,
        stage: String?,
        note: String?,
        enableReminder: Boolean = false,
        reminderHours: Int = 3,
        reminderMinutes: Int = 0,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addBottleFeedingRecord(
                startTime, amount, feedingType, brand, stage, note
            )
            if (enableReminder) {
                val typeStr = when (feedingType) {
                    BottleFeedingType.BREAST_MILK -> "breast"
                    BottleFeedingType.MIXED -> "bottle"
                    BottleFeedingType.FORMULA -> "bottle"
                    BottleFeedingType.WATER_MILK -> "bottle"
                    else -> "bottle"
                }
                reminderScheduler.scheduleFeedingReminder(id, typeStr, reminderHours, reminderMinutes)
            }
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addDiaperRecord(
        startTime: Long,
        type: DiaperType,
        weight: DiaperWeight,
        poopState: String?,
        poopColor: String?,
        photoUri: String?,
        note: String?,
        enableReminder: Boolean = false,
        reminderHours: Int = 3,
        reminderMinutes: Int = 0,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addDiaperRecord(
                startTime, type, weight, poopState, poopColor, photoUri, note
            )
            // 如果启用提醒，调度换尿布提醒
            if (enableReminder) {
                reminderScheduler.scheduleDiaperReminder(id, reminderHours, reminderMinutes)
            }
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addSleepRecord(
        startTime: Long,
        endTime: Long?,
        sleepMethod: String?,
        quality: SleepQuality?,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addSleepRecord(startTime, endTime, sleepMethod, quality, note)
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addFoodRecord(
        startTime: Long,
        foodType: String,
        texture: FoodTexture?,
        amount: Int?,
        unit: String,
        feedback: FoodFeedback?,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addFoodRecord(
                startTime, foodType, texture, amount, unit, feedback, note
            )
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addGrowthRecord(
        startTime: Long,
        height: Float?,
        weight: Float?,
        headCircumference: Float?,
        footLength: Float?,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addGrowthRecord(
                startTime, height, weight, headCircumference, footLength, note
            )
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addTemperatureRecord(
        startTime: Long,
        temperature: Float,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addTemperatureRecord(startTime, temperature, note)
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addVaccineRecord(
        startTime: Long,
        vaccineName: String,
        vaccineType: VaccineType,
        doseNumber: String,
        description: String?,
        isPlanned: Boolean,
        plannedDate: Long?,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addVaccineRecord(
                startTime, vaccineName, vaccineType, doseNumber,
                description, isPlanned, plannedDate, note
            )
            onComplete(id)
        }
    }

    fun addSupplementRecord(
        startTime: Long,
        supplementName: String,
        dosage: String,
        unit: String,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addSupplementRecord(startTime, supplementName, dosage, unit, note)
            loadTodayOverview()
            onComplete(id)
        }
    }

    fun addPumpRecord(
        startTime: Long,
        leftAmount: Int?,
        rightAmount: Int?,
        totalAmount: Int?,
        leftDuration: Int?,
        rightDuration: Int?,
        note: String?,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.addPumpRecord(
                startTime, leftAmount, rightAmount, totalAmount,
                leftDuration, rightDuration, note
            )
            loadTodayOverview()
            onComplete(id)
        }
    }

    // ==================== 删除记录 ====================

    fun deleteRecord(record: Record) {
        viewModelScope.launch {
            repository.deleteRecord(record)
            loadTodayOverview()
        }
    }

    // ==================== 获取详情 ====================

    suspend fun getDiaperDetail(recordId: String): DiaperDetail? {
        return repository.getDiaperDetail(recordId)
    }

    suspend fun getBottleFeedingDetail(recordId: String): BottleFeedingDetail? {
        return repository.getBottleFeedingDetail(recordId)
    }

    fun getAllGrowthRecords(): Flow<List<GrowthDetailWithRecord>> {
        return repository.getAllGrowthRecords()
    }

    fun getAllVaccineRecords(): Flow<List<VaccineDetail>> {
        return repository.getAllVaccineRecords()
    }
}
