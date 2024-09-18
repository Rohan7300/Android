package com.notify.testproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FirebaseNotificationService : FirebaseMessagingService() {
    val TAG = "FBNotification"


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "FCMMessage Data1 ${message.messageId}")
        Log.d(TAG, "FCMMessage Data2 ${message.data} ")
        Log.d(TAG, "FCMMessage Data3 ${message.messageType} ")
        Log.d(TAG, "FCMMessage Data4 ${message.notification} ")
        Log.d(TAG, "FCMMessage Data5 ${message.rawData} ")
        Log.d(TAG, "FCMMessage Data6 ${message.sentTime} ")
        val title = message.notification?.title ?: "Notification Title"
        val messageBody = message.notification?.body ?: "Notification Message"
        val actionToperform = message.data["alertType"] ?: "undefined"
        var actionID = "0.0"
        var tokenUrl = ""
        var notificationId = "0"
        if (message.data["actionId"] != null) {
            actionID = message.data["actionId"].toString()
        }
        if (message.data["url"] != null) {
            tokenUrl = message.data["url"].toString()
        }
        if (message.data["notificationId"] != null) {
            notificationId = message.data["notificationId"].toString()
        }

        Log.d(TAG, "FCMMessage MessageBody ${message.notification?.body} ")
        Log.d(TAG, "FCMMessage AlertType ${message.data["alertType"]} ")
        showCustomNotification(
            title,
            messageBody,
            actionToperform,
            actionID,
            tokenUrl,
            notificationId
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM Refreshed token: $token")
        var userID = 0
    }

    private fun showCustomNotification(
        title: String,
        message: String,
        actionToPerform: String,
        actionID: String,
        tokenUrl: String,
        notificationID: String
    ) {

        var pendingIntent: PendingIntent? = null

        val builder = NotificationCompat.Builder(this, "ForegroundServiceChannel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("title")
            .setContentText("message")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVibrate(longArrayOf(2000, 2000, 2000, 2000, 2000))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setCustomContentView(getCustomDesign("title", "message"))
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(50, builder.build())
    }

    private fun getCustomDesign(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.notify.testproject", R.layout.notification_layout)

        remoteViews.setTextViewText(R.id.descriptionXX, message)
        remoteViews.setImageViewResource(R.id.icons, R.drawable.ic_launcher_background)
        return remoteViews
    }

}