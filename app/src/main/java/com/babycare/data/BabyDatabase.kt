package com.babycare.data

import android.content.Context
import androidx.room.*
import com.babycare.data.dao.RecordDao
import com.babycare.data.model.*

/**
 * 应用程序数据库
 */
@Database(
    entities = [
        Record::class,
        BreastFeedingDetail::class,
        BottleFeedingDetail::class,
        DiaperDetail::class,
        SleepDetail::class,
        FoodDetail::class,
        GrowthDetail::class,
        TemperatureDetail::class,
        VaccineDetail::class,
        MedicationDetail::class,
        SupplementDetail::class,
        PumpDetail::class,
        ActivityDetail::class,
        NoteDetail::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecordTypeConverter::class)
abstract class BabyDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: BabyDatabase? = null

        fun getDatabase(context: Context): BabyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BabyDatabase::class.java,
                    "baby_care_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * 类型转换器 - 用于枚举和复杂类型的存储
 */
class RecordTypeConverter {
    @TypeConverter
    fun fromRecordType(type: RecordType): String = type.name

    @TypeConverter
    fun toRecordType(name: String): RecordType = RecordType.valueOf(name)

    @TypeConverter
    fun fromBreastSide(side: BreastSide): String = side.name

    @TypeConverter
    fun toBreastSide(name: String): BreastSide = BreastSide.valueOf(name)

    @TypeConverter
    fun fromBottleFeedingType(type: BottleFeedingType): String = type.name

    @TypeConverter
    fun toBottleFeedingType(name: String): BottleFeedingType = BottleFeedingType.valueOf(name)

    @TypeConverter
    fun fromDiaperType(type: DiaperType): String = type.name

    @TypeConverter
    fun toDiaperType(name: String): DiaperType = DiaperType.valueOf(name)

    @TypeConverter
    fun fromDiaperWeight(weight: DiaperWeight): String = weight.name

    @TypeConverter
    fun toDiaperWeight(name: String): DiaperWeight = DiaperWeight.valueOf(name)

    @TypeConverter
    fun fromSleepQuality(quality: SleepQuality?): String? = quality?.name

    @TypeConverter
    fun toSleepQuality(name: String?): SleepQuality? = name?.let { SleepQuality.valueOf(it) }

    @TypeConverter
    fun fromFoodTexture(texture: FoodTexture?): String? = texture?.name

    @TypeConverter
    fun toFoodTexture(name: String?): FoodTexture? = name?.let { FoodTexture.valueOf(it) }

    @TypeConverter
    fun fromFoodFeedback(feedback: FoodFeedback?): String? = feedback?.name

    @TypeConverter
    fun toFoodFeedback(name: String?): FoodFeedback? = name?.let { FoodFeedback.valueOf(it) }

    @TypeConverter
    fun fromVaccineType(type: VaccineType): String = type.name

    @TypeConverter
    fun toVaccineType(name: String): VaccineType = VaccineType.valueOf(name)

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name

    @TypeConverter
    fun toSyncStatus(name: String): SyncStatus = SyncStatus.valueOf(name)
}
