package com.fpoly.shoes_app.framework.presentation.ui.forgot

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentOPTBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OPTFragment : Fragment() {
    private var _binding: FragmentOPTBinding? = null
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
        _binding = FragmentOPTBinding.inflate(inflater, container, false)
//        binding.btnSelect.setOnClickListener {
//            sendOTP("0334325232")
        sendVerificationEmail("quymstle125@gmail.com")

        startCountdownTimer()
        binding.btnSelect.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.createNewPassFragment, null, navOptions)
        }
//        }
        return binding.root
    }

    private fun startCountdownTimer() {
        binding.let { safeBinding ->
            // Thời gian đếm ngược là 2 phút (120000 mili giây)
            safeBinding.btnSelect.isEnabled = true
            val countdownDuration =  6 * 1000
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


    fun sendVerificationEmail(email: String) {
        auth.createUserWithEmailAndPassword(email, "123456")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                // Email verification sent successfully
                                Toast.makeText(
                                    requireContext(),
                                    "Verification SUS",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                // Failed to send email verification
                                Toast.makeText(
                                    requireContext(),
                                    "Verification Fail",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // Email is invalid or not found
                    } else if (exception is FirebaseAuthUserCollisionException) {
                        // Email already exists
                    } else {
                        // Other errors
                    }
                }
            }
    }

    private fun sendOTP(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+84$phoneNumber",
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(
                        requireContext(),
                        "Verification failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(s, p1)
                    verificationId = s
                }
            }
        )
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
