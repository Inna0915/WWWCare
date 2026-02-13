package com.babycare.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

/**
 * 提醒工作器
 * 使用 WorkManager 在指定时间后显示提醒通知
 */
class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_TAG = "baby_care_reminder"
        const val KEY_RECORD_ID = "record_id"
        const val KEY_RECORD_TYPE = "record_type"
        const val KEY_REMINDER_TYPE = "reminder_type"
        const val KEY_DELAY_MINUTES = "delay_minutes"

        /**
         * 调度提醒
         * @param context 上下文
         * @param recordId 记录ID（用于取消）
         * @param recordType 记录类型
         * @param reminderType 提醒类型（diaper, feeding, sleep等）
         * @param delayMinutes 延迟时间（分钟）
         */
        fun scheduleReminder(
            context: Context,
            recordId: String,
            recordType: String,
            reminderType: String,
            delayMinutes: Int
        ) {
            val inputData = Data.Builder()
                .putString(KEY_RECORD_ID, recordId)
                .putString(KEY_RECORD_TYPE, recordType)
                .putString(KEY_REMINDER_TYPE, reminderType)
                .putInt(KEY_DELAY_MINUTES, delayMinutes)
                .build()

            val reminderWork = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(delayMinutes.toLong(), TimeUnit.MINUTES)
                .setInputData(inputData)
                .addTag(WORK_TAG)
                .addTag("${WORK_TAG}_$recordId")
                .build()

            WorkManager.getInstance(context).enqueue(reminderWork)
        }

        /**
         * 取消指定记录的所有提醒
         */
        fun cancelReminder(context: Context, recordId: String) {
            WorkManager.getInstance(context)
                .cancelAllWorkByTag("${WORK_TAG}_$recordId")
        }

        /**
         * 取消所有提醒
         */
        fun cancelAllReminders(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
        }
    }

    override suspend fun doWork(): Result {
        val recordId = inputData.getString(KEY_RECORD_ID)
        val reminderType = inputData.getString(KEY_REMINDER_TYPE) ?: return Result.failure()

        val notificationHelper = NotificationHelper(applicationContext)

        // 根据提醒类型显示对应通知
        when (reminderType) {
            "diaper" -> {
                notificationHelper.showDiaperReminder(recordId)
            }
            "feeding" -> {
                val recordType = inputData.getString(KEY_RECORD_TYPE) ?: "bottle"
                notificationHelper.showFeedingReminder(recordType, recordId)
            }
            "sleep" -> {
                notificationHelper.showSleepReminder(recordId)
            }
        }

        return Result.success()
    }
}

/**
 * 提醒调度器
 * 提供简化的提醒调度接口
 */
class ReminderScheduler(private val context: Context) {

    /**
     * 调度换尿布提醒
     * @param recordId 记录ID
     * @param hours 延迟小时数（默认3小时）
     * @param minutes 延迟分钟数
     */
    fun scheduleDiaperReminder(
        recordId: String,
        hours: Int = 3,
        minutes: Int = 0
    ) {
        val totalMinutes = hours * 60 + minutes
        ReminderWorker.scheduleReminder(
            context = context,
            recordId = recordId,
            recordType = "diaper",
            reminderType = "diaper",
            delayMinutes = totalMinutes
        )
    }

    /**
     * 调度喂养提醒
     * @param recordId 记录ID
     * @param recordType 喂养类型（bottle, breast, food）
     * @param hours 延迟小时数（默认3小时）
     * @param minutes 延迟分钟数
     */
    fun scheduleFeedingReminder(
        recordId: String,
        recordType: String,
        hours: Int = 3,
        minutes: Int = 0
    ) {
        val totalMinutes = hours * 60 + minutes
        ReminderWorker.scheduleReminder(
            context = context,
            recordId = recordId,
            recordType = recordType,
            reminderType = "feeding",
            delayMinutes = totalMinutes
        )
    }

    /**
     * 调度睡眠提醒
     * @param recordId 记录ID
     * @param hours 延迟小时数（默认2小时）
     * @param minutes 延迟分钟数
     */
    fun scheduleSleepReminder(
        recordId: String,
        hours: Int = 2,
        minutes: Int = 0
    ) {
        val totalMinutes = hours * 60 + minutes
        ReminderWorker.scheduleReminder(
            context = context,
            recordId = recordId,
            recordType = "sleep",
            reminderType = "sleep",
            delayMinutes = totalMinutes
        )
    }

    /**
     * 取消指定记录的提醒
     */
    fun cancelReminder(recordId: String) {
        ReminderWorker.cancelReminder(context, recordId)
    }

    /**
     * 取消所有提醒
     */
    fun cancelAllReminders() {
        ReminderWorker.cancelAllReminders(context)
    }
}
