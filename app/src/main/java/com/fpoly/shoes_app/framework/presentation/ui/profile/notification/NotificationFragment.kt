package com.fpoly.shoes_app.framework.presentation.ui.profile.notification

import android.content.Context
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.fpoly.shoes_app.databinding.FragmentEditProfileBinding
import com.fpoly.shoes_app.databinding.FragmentNotificationBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding, NotificationViewModel>(
    FragmentNotificationBinding::inflate, NotificationViewModel::class.java
) {
    private lateinit var audioManager: AudioManager
    private fun muteApp() {
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.ADJUST_MUTE,
            0
        )
    }
    private fun unmuteApp() {
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.ADJUST_UNMUTE,
            0
        )
    }

    override fun setupPreViews() {
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

    }
//    private fun playSuccessSound() {
//        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val ringtone: Ringtone = RingtoneManager.getRingtone(requireContext(), notification)
//        ringtone.play()    }
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun triggerVibration() {
//        val vibrator = vibratorManager.defaultVibrator
//        if (vibrator.hasVibrator()) {
//            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
//            vibrator.vibrate(vibrationEffect)
//        } else {
//            Log.e("Vibration", "Device does not have a vibrator")
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun setupViews() {


    }

    override fun bindViewModel() {
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun setOnClick() {
        binding.notification.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            service?.playNotificationSound(requireContext())
        }
    }
        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                service?.playCustomSound(requireContext())
            }
        }
        binding.switchVibrate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                service.triggerVibration(requireContext())
                sharedPreferences.saveVibrateModeState(true)
            } else {
                sharedPreferences.saveVibrateModeState(false)
            }
        }

    }
}