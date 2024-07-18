package com.fpoly.shoes_app.utility.service

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.fpoly.shoes_app.R
import javax.inject.Singleton

@Singleton
object ServiceUtil {
    @RequiresApi(Build.VERSION_CODES.S)
    fun triggerVibration(context: Context) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        if (vibrator.hasVibrator()) {
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            Log.e("Vibration", "Device does not have a vibrator")
        }
    }
    fun playNotificationSound(context: Context) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone: Ringtone = RingtoneManager.getRingtone(context, notification)
        ringtone.play()
    }
    fun playCustomSound(context: Context) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.levelup)
        mediaPlayer.start()
    }
}