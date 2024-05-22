package com.fpoly.shoes_app.framework.presentation.ui.forgot

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentOtpBinding
import com.google.firebase.auth.FirebaseAuth

class OPTFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private var verificationId: String? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var countDownTimer: CountDownTimer? = null
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left) // Optional: You may need to define this animation as well
        .setPopExitAnim(R.anim.slide_out_right) // Optional: You may need to define this animation as well
        .build()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
//        binding.btnSelect.setOnClickListener {
//            sendOTP("0334325232")

        startCountdownTimer()
        binding.btnSelect.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.createNewPassFragment, null, navOptions)
        }
//        }
        return binding.root
    }

    private fun startCountdownTimer() {
        binding.let { safeBinding ->
            // Thời gian đếm ngược là 2 phút (120000 mili giây)
            safeBinding.btnSelect.isEnabled = true
            val countdownDuration = 6 * 1000
            // Tạo một CountDownTimer với thời gian đếm ngược là 2 phút
            countDownTimer = object : CountDownTimer(countdownDuration.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Format thời gian đếm ngược thành định dạng phút:giây
                    val timeLeft = String.format(
                        "%02d:%02d",
                        millisUntilFinished / 60000,
                        (millisUntilFinished / 1000) % 60
                    )
                    safeBinding.countdownTimerTextView.text = timeLeft
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    safeBinding.countdownTimerTextView.text = "00:00"
                    safeBinding.btnSelect.isEnabled = false
                }
            }
            countDownTimer?.start()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
