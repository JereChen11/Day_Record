package com.day.record.ui

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.day.record.MyApp.Companion.context


class AlarmReceiver : BroadcastReceiver() {
    private val TAG = "AlarmReceiver"

    companion object {
        const val TITLE_KEY = "TITLE"
        const val CONTENT_KEY = "CONTENT"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.e(TAG, "onReceive: ")

        p1?.apply {
            sendNotification(
                getStringExtra(TITLE_KEY) ?: "Day Record",
                getStringExtra(CONTENT_KEY) ?: "Did you check in today?"
            )
        }

        //display that alarm is ringing
        Toast.makeText(context, "Alarm Ringing...!!!", Toast.LENGTH_LONG).show()
    }

    private fun sendNotification(title: String, content: String) {
        val channelId = "notify_channel_id"
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_menu_day)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .apply {
                setContentIntent(pendingIntent)
            }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                "lalala",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}