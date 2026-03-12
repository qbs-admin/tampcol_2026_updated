package com.qbrains.tampcolapp.data.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import androidx.core.app.NotificationCompat
//import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.ui.DashBoardActivity
import com.qbrains.tampcolapp.ui.component.HarinaApplications

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
//        if (PreferenceManager(context = this).getDeviceToken().isNullOrEmpty()) {
//            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//                it.token
//            }.addOnFailureListener {
//
//            }
//        }
        (application as HarinaApplications).setFirebaseAppId(p0)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        println("GET____MESSAGE_____" + remoteMessage.data)
        val data: Map<String, String> = remoteMessage.data
        var title: String = data.getValue("title")
        var message: String = data.getValue("message")
        var intent: Intent? = null


        intent = Intent(applicationContext, DashBoardActivity::class.java)
//            intent.putExtra(NOTIFICATION,KEY_NOTIFICATION)


        val pi: PendingIntent = PendingIntent.getActivity(applicationContext, 101, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var channel: NotificationChannel?
            val audiAt = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()
            channel = NotificationChannel("100", "MyChannel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }


        var bigTextStyle: NotificationCompat.BigTextStyle? =
            NotificationCompat.BigTextStyle()
        bigTextStyle?.setBigContentTitle(title)
        bigTextStyle?.bigText(message)
        val builder = NotificationCompat.Builder(applicationContext, "100")
            .setContentTitle(title)
            .setContentText(message)
//            .setLargeIcon(R.mipmap.ic_launcher)
            // .setCustomBigContentView(bigView)

            .setAutoCancel(true)
            //.setLargeIcon(ge)
//            .setContentText(data.getValue(NOTIFICATION_MESSAGE))
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setStyle(bigTextStyle)
            //.setContentText(body)
            .setContentIntent(pi)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        notificationManager.notify(101, builder.build())
    }
}