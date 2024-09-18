package com.notify.testproject

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {

    private val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = buildNotification()
        startForeground(1, notification)

        // Do your background work here
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notifications = notificationManager.activeNotifications

        // Check if the notification is for our app
/*        for (notification in notifications) {
            if (notification.packageName == packageName) {
                // Post a custom notification

            }
        }*/
        val customNotification = NotificationCompat.Builder(this, "$CHANNEL_ID")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentTitle("Custom Notification")
            .setContentText("This is a custom notification")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(2, customNotification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    private fun buildNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Service is running in the foreground")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your own icon here
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
