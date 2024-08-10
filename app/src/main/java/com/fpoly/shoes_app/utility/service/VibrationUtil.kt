package com.fpoly.shoes_app.utility.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.utility.SharedPreferencesManager.getNotificationModeState
import com.fpoly.shoes_app.utility.SharedPreferencesManager.getSoundModeState
import com.fpoly.shoes_app.utility.SharedPreferencesManager.getVibrateModeState
import javax.inject.Singleton

@Singleton
object ServiceUtil {
    private const val CHANNEL_ID = "example_channel"
    private var notificationId = 0

    @RequiresApi(Build.VERSION_CODES.S)
    fun triggerVibration(context: Context) {
        if (getVibrateModeState()) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            if (vibrator.hasVibrator()) {
                val vibrationEffect =
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                Log.e("Vibration", "Device does not have a vibrator")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun playNotificationSound(context: Context, title: String, content: String) {

        if (getNotificationModeState()) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )
            // Build the notification
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            // Show the notification
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId++, builder.build())
            }
        }
    }

    fun playCustomSound(context: Context) {
        if (getSoundModeState()) {
            val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.levelup)
            mediaPlayer.start()
        }
    }
}