package hr.algebra.formula1data.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import hr.algebra.formula1data.R
import hr.algebra.formula1data.activity.MainActivity

object NotificationHelper {

    private const val REMINDER_CHANNEL_ID = "f1_channel_id"
    private const val REMINDER_CHANNEL_NAME = "F1 Updates"
    private const val REMINDER_NOTIFICATION_ID = 1

    private const val SYNC_CHANNEL_ID = "season_sync_channel"
    private const val SYNC_CHANNEL_NAME = "Season Sync Notifications"

    fun showNotification(context: Context) {
        Log.d("NotificationHelper", "Showing reminder notification...")

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID, REMINDER_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for new F1 data updates"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("F1Data Reminder")
            .setContentText("Check the latest F1 data!")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(REMINDER_NOTIFICATION_ID, notification)
    }

    fun showSyncNotification(context: Context, message: String, syncedYear: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SYNC_CHANNEL_ID, SYNC_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Background sync updates for F1 season data"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent with extra to open the app and pass year
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("synced_year", syncedYear)
        }

        val pendingIntent = PendingIntent.getActivity(
            context, syncedYear.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle("Season Synced")
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
