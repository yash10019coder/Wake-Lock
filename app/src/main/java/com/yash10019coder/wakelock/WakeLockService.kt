package com.yash10019coder.wakelock

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock


class WakeLockService : Service() {
    private var wakeLock: WakeLock? = null
    private lateinit var notificationHelper: NotificationHelper
    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        notificationHelper.showNotification("WakeLock", "WakeLock is running...", 1)
        val powerManager: PowerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakeLockTag")
        wakeLock?.acquire()
        startForegroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            stopSelf()
            return START_NOT_STICKY
        }

        return START_STICKY
    }

    private fun startForegroundNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MyServiceChannelID",
                "MyServiceChannelName",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val stopIntent = Intent(this, WakeLockService::class.java)
        stopIntent.action = "STOP"
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val cancelNotificationAction = Notification.Action.Builder(
            R.drawable.baseline_cancel_24,
            "Cancel",
            stopPendingIntent
        ).build()

        val notification = Notification.Builder(this, "MyServiceChannelID")
            .setContentTitle("My Service")
            .setContentText("Service is running...")
            .setSmallIcon(R.mipmap.ic_launcher)  // replace with your icon
            .addAction(cancelNotificationAction)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationHelper.showNotification("WakeLock", "WakeLock is running onDestroy...", 1)
        wakeLock?.run {
            if (isHeld) {
                notificationHelper.showNotification(
                    "WakeLock",
                    "WakeLock is running onDestroy...isHeld",
                    1
                )
                release()
            }
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
