package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.atomic.AtomicInteger

class FCMLis : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        //Called when a new token for the  Firebase project is generated.
        super.onNewToken(token)
        Log.d("MyFirebase", "onNewToken: " + token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // will get the notification data in remoteMessage
        super.onMessageReceived(remoteMessage)
        Log.d(
            "MyFirebase",
            "onMessageReceived: FROM: " + remoteMessage.from + " \n DATA: " + remoteMessage.data
        )
        sendNotification(this, remoteMessage) // to render the notification
    }


    fun sendNotification(context: Context, remoteMessage: RemoteMessage) {

        val channelId = "testnews" // require for OS 8 and above

        // Create an explicit intent for an Activity in your app
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val extras = Bundle()
        extras.putString(notify_title, remoteMessage.notification?.title)
        extras.putString(notify_url, remoteMessage.data.get("pageUrl"))
        intent.putExtras(extras)
        intent.action = Intent.ACTION_VIEW

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                NotificationID.iD,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
       // creating the notifications body
        val notificationBuilder =
            NotificationCompat.Builder(context.getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.smallicon)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.white))
                .setVibrate(longArrayOf(500, 500, 500))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)

        val notificationManager = NotificationManagerCompat.from(context) // rendering the notifications

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "testnews",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NotificationID.iD, notificationBuilder.build()) // display the notifications tray
    }

    companion object {
        const val notify_title: String = "notify_title"
        const val notify_url: String = "notify_url"
    }

}

internal object NotificationID {
    private val c = AtomicInteger(100)
    val iD: Int
        get() = c.incrementAndGet()
}