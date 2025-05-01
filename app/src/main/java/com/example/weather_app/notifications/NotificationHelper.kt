package com.example.weather_app.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weather_app.MainActivity
import com.example.weather_app.R

object NotificationHelper {

    private const val CHANNEL_ID = "1"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Main Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Some description"
        }
        val manager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, title: String, content: String) {

        val i = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 200, i, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.b21000_fog_light_large2x)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(context).notify(500, builder.build())
    }
}