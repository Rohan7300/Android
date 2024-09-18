package com.notify.testproject

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.NOTIFICATION_RECEIVED") {
            // Handle the notification
            context?.let {
                val customNotification = createCustomNotification(it)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                NotificationManagerCompat.from(it).notify(1, customNotification)
            }
        }
    }

    private fun createCustomNotification(context: Context): Notification {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, "FOREGROUND_SERVICE_CHANNEL_ID")
            .setContentTitle("Custom Notification")
            .setContentText("You have received a new notification")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
    }
}