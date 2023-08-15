package com.example.space

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

const val notificationID=1
const val channelId = "channel1"
const val titleExtra="tittleExtra"
const val messageExtra ="messageExtra"
class Notification :BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Notification", "Alarm triggered!")
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID,notification)
    }
}