package com.babycare.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.babycare.R

/**
 * 通知帮助类
 * 管理通知渠道和通知显示
 */
class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID_DIAPER = "diaper_reminder"
        const val CHANNEL_ID_FEEDING = "feeding_reminder"
        const val CHANNEL_ID_SLEEP = "sleep_reminder"
        const val CHANNEL_ID_GENERAL = "general_reminder"

        const val NOTIFICATION_ID_DIAPER = 1001
        const val NOTIFICATION_ID_FEEDING = 1002
        const val NOTIFICATION_ID_SLEEP = 1003
        const val NOTIFICATION_ID_GENERAL = 1004
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    /**
     * 创建通知渠道（Android 8.0+ 需要）
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 换尿布提醒渠道
            val diaperChannel = NotificationChannel(
                CHANNEL_ID_DIAPER,
                "换尿布提醒",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "提醒更换尿布"
            }

            // 喂养提醒渠道
            val feedingChannel = NotificationChannel(
                CHANNEL_ID_FEEDING,
                "喂养提醒",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "提醒喂奶或辅食"
            }

            // 睡眠提醒渠道
            val sleepChannel = NotificationChannel(
                CHANNEL_ID_SLEEP,
                "睡眠提醒",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "提醒宝宝睡眠状态"
            }

            // 通用提醒渠道
            val generalChannel = NotificationChannel(
                CHANNEL_ID_GENERAL,
                "通用提醒",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "其他通用提醒"
            }

            notificationManager.createNotificationChannels(
                listOf(diaperChannel, feedingChannel, sleepChannel, generalChannel)
            )
        }
    }

    /**
     * 显示换尿布提醒
     */
    fun showDiaperReminder(recordId: String? = null) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_DIAPER)
            .setSmallIcon(R.drawable.ic_notification) // 需要创建图标
            .setContentTitle("该换尿布了")
            .setContentText("宝宝可能需要更换尿布了，请检查一下。")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            NOTIFICATION_ID_DIAPER + (recordId?.hashCode() ?: 0),
            notification
        )
    }

    /**
     * 显示喂养提醒
     */
    fun showFeedingReminder(recordType: String, recordId: String? = null) {
        val title = when (recordType) {
            "bottle" -> "该喂奶了"
            "breast" -> "该喂奶了"
            "food" -> "该吃辅食了"
            else -> "喂养提醒"
        }

        val content = when (recordType) {
            "bottle" -> "到时间给宝宝喂奶了。"
            "breast" -> "到时间给宝宝喂奶了。"
            "food" -> "到时间给宝宝吃辅食了。"
            else -> "提醒：到喂养时间了。"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_FEEDING)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            NOTIFICATION_ID_FEEDING + (recordId?.hashCode() ?: 0),
            notification
        )
    }

    /**
     * 显示睡眠提醒
     */
    fun showSleepReminder(recordId: String? = null) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_SLEEP)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("睡眠时间提醒")
            .setContentText("宝宝已经醒来一段时间了，可能需要休息。")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            NOTIFICATION_ID_SLEEP + (recordId?.hashCode() ?: 0),
            notification
        )
    }

    /**
     * 取消通知
     */
    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    /**
     * 取消所有通知
     */
    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }
}
