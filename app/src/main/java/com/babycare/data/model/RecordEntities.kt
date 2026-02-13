package com.babycare.data.model

import androidx.room.*

/**
 * 记录类型枚举
 */
enum class RecordType {
    BREAST_FEEDING,      // 母乳喂养
    BOTTLE_FEEDING,      // 奶瓶喂养
    DIAPER,              // 换尿布
    SLEEP,               // 睡眠
    FOOD,                // 辅食
    GROWTH,              // 生长发育
    TEMPERATURE,         // 体温
    VACCINE,             // 疫苗
    MEDICATION,          // 用药
    SUPPLEMENT,          // 补剂
    PUMP,                // 吸奶器
    ACTIVITY,            // 活动
    NOTE                 // 随手记
}

/**
 * 基础记录表 - 所有记录共享的字段
 */
@Entity(tableName = "records")
data class Record(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),

    @ColumnInfo(name = "type")
    val type: RecordType,

    @ColumnInfo(name = "start_time")
    val startTime: Long,  // 时间戳（毫秒）

    @ColumnInfo(name = "end_time")
    val endTime: Long? = null,

    @ColumnInfo(name = "duration")
    val duration: Int? = null,  // 持续时间（分钟）

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "note")
    val note: String? = null,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

enum class SyncStatus {
    SYNCED,     // 已同步
    PENDING,    // 待同步
    FAILED      // 同步失败
}

enum class Gender {
    BOY, GIRL
}

// ==================== 母乳喂养详情 ====================
@Entity(
    tableName = "breast_feeding_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BreastFeedingDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "left_duration")
    val leftDuration: Int = 0,  // 左侧时长（分钟）

    @ColumnInfo(name = "right_duration")
    val rightDuration: Int = 0,  // 右侧时长（分钟）

    @ColumnInfo(name = "side")
    val side: BreastSide = BreastSide.LEFT,  // 喂养位置

    @ColumnInfo(name = "order")
    val order: String? = null  // 先后顺序（如：先左后右）
)

enum class BreastSide {
    LEFT, RIGHT, BOTH
}

// ==================== 奶瓶喂养详情 ====================
@Entity(
    tableName = "bottle_feeding_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BottleFeedingDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "amount")
    val amount: Int,  // 奶量（ml）

    @ColumnInfo(name = "feeding_type")
    val feedingType: BottleFeedingType = BottleFeedingType.FORMULA,  // 喂养类型

    @ColumnInfo(name = "brand")
    val brand: String? = null,  // 奶粉品牌

    @ColumnInfo(name = "stage")
    val stage: String? = null   // 奶粉段数
)

enum class BottleFeedingType {
    BREAST_MILK,    // 母乳
    MIXED,          // 混合
    FORMULA,        // 奶粉
    WATER_MILK,     // 水奶
    WATER,          // 水
    OTHER           // 其他
}

// ==================== 换尿布详情 ====================
@Entity(
    tableName = "diaper_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DiaperDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "type")
    val type: DiaperType = DiaperType.BOTH,  // 类型

    @ColumnInfo(name = "weight")
    val weight: DiaperWeight = DiaperWeight.NORMAL,  // 重量

    @ColumnInfo(name = "poop_state")
    val poopState: String? = null,  // 便便状态

    @ColumnInfo(name = "poop_color")
    val poopColor: String? = null,  // 便便颜色

    @ColumnInfo(name = "photo_uri")
    val photoUri: String? = null   // 照片URI
)

enum class DiaperType {
    PEE, POOP, BOTH
}

enum class DiaperWeight {
    LIGHT, NORMAL, HEAVY
}

// ==================== 睡眠详情 ====================
@Entity(
    tableName = "sleep_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SleepDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "sleep_method")
    val sleepMethod: String? = null,  // 入睡方式（抱睡/拍睡/自主入睡）

    @ColumnInfo(name = "quality")
    val quality: SleepQuality? = null  // 睡眠质量
)

enum class SleepQuality {
    EXCELLENT, GOOD, NORMAL, POOR
}

// ==================== 辅食详情 ====================
@Entity(
    tableName = "food_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "food_type")
    val foodType: String,  // 辅食类型（如：米粉+猪肉）

    @ColumnInfo(name = "texture")
    val texture: FoodTexture? = null,  // 性状

    @ColumnInfo(name = "amount")
    val amount: Int? = null,  // 食量

    @ColumnInfo(name = "unit")
    val unit: String = "ml",  // 单位

    @ColumnInfo(name = "feedback")
    val feedback: FoodFeedback? = null  // 宝宝反馈
)

enum class FoodTexture {
    THIN,      // 稀滑
    PUREE,     // 泥状
    THICK      // 粘稠
}

enum class FoodFeedback {
    LIKE, DISLIKE, NEUTRAL, ALLERGY
}

// ==================== 生长发育详情 ====================
@Entity(
    tableName = "growth_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GrowthDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "height")
    val height: Float? = null,  // 身高（cm）

    @ColumnInfo(name = "weight")
    val weight: Float? = null,  // 体重（kg）

    @ColumnInfo(name = "head_circumference")
    val headCircumference: Float? = null,  // 头围（cm）

    @ColumnInfo(name = "foot_length")
    val footLength: Float? = null  // 脚长（cm）
)

// ==================== 体温详情 ====================
@Entity(
    tableName = "temperature_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TemperatureDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "temperature")
    val temperature: Float  // 体温（摄氏度）
)

// ==================== 疫苗详情 ====================
@Entity(
    tableName = "vaccine_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VaccineDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "vaccine_name")
    val vaccineName: String,  // 疫苗名称

    @ColumnInfo(name = "vaccine_type")
    val vaccineType: VaccineType = VaccineType.FREE,  // 免费/自费

    @ColumnInfo(name = "dose_number")
    val doseNumber: String,  // 接种次数（如：第1/3针）

    @ColumnInfo(name = "description")
    val description: String? = null,  // 预防疾病说明

    @ColumnInfo(name = "is_planned")
    val isPlanned: Boolean = false,  // 是否为计划内疫苗

    @ColumnInfo(name = "planned_date")
    val plannedDate: Long? = null  // 计划接种日期
)

enum class VaccineType {
    FREE, PAID
}

// ==================== 用药详情 ====================
@Entity(
    tableName = "medication_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicationDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,  // 药品名称

    @ColumnInfo(name = "dosage")
    val dosage: String,  // 用量

    @ColumnInfo(name = "unit")
    val unit: String = "ml"  // 单位
)

// ==================== 补剂详情 ====================
@Entity(
    tableName = "supplement_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SupplementDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "supplement_name")
    val supplementName: String,  // 补剂名称（如：Ddrops维生素D3）

    @ColumnInfo(name = "dosage")
    val dosage: String,  // 用量

    @ColumnInfo(name = "unit")
    val unit: String = "滴"  // 单位
)

// ==================== 吸奶器详情 ====================
@Entity(
    tableName = "pump_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PumpDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "left_amount")
    val leftAmount: Int? = null,  // 左侧奶量（ml）

    @ColumnInfo(name = "right_amount")
    val rightAmount: Int? = null,  // 右侧奶量（ml）

    @ColumnInfo(name = "total_amount")
    val totalAmount: Int? = null,  // 总量（ml）

    @ColumnInfo(name = "left_duration")
    val leftDuration: Int? = null,  // 左侧时长（分钟）

    @ColumnInfo(name = "right_duration")
    val rightDuration: Int? = null  // 右侧时长（分钟）
)

// ==================== 活动详情 ====================
@Entity(
    tableName = "activity_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActivityDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "activity_type")
    val activityType: String? = null  // 活动类型（如： tummy time）
)

// ==================== 随手记详情 ====================
@Entity(
    tableName = "note_details",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NoteDetail(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "content")
    val content: String,  // 内容

    @ColumnInfo(name = "photo_uri")
    val photoUri: String? = null  // 图片URI
)
