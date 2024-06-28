package com.fpoly.shoes_app.framework.presentation.ui.forgot.otpConfirm

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentOtpBinding
import com.fpoly.shoes_app.framework.data.module.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OPTConfirmFragment : BaseFragment<FragmentOtpBinding, OTPConfirmViewModel>(
    FragmentOtpBinding::inflate, OTPConfirmViewModel::class.java
) {
    private var countDownTimer: CountDownTimer? = null
    private var idUser: String = ""

    private fun startCountdownTimer() {
        binding.let { safeBinding ->
            safeBinding.btnSelect.isEnabled = true
            val countdownDuration = 120000
            countDownTimer = object : CountDownTimer(countdownDuration.toLong(), 1000) {
                @SuppressLint("DefaultLocale")
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = String.format(
                        "%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60
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
        countDownTimer?.cancel()
    }

    override fun setupViews() {
        idUser = sharedPreferences.getIdUser()
        startCountdownTimer()
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.otpConfirmResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        val otpConfirmResponse = result.data
                        if (otpConfirmResponse?.success == true) {
                            val navController = findNavController()
                            fragmentManager?.popBackStackImmediate(R.id.loginFragmentScreen, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            navController.navigate(
                                R.id.createNewPassFragment, null, NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }
                        StyleableToast.makeText(
                            requireContext(), strNullOrEmpty(otpConfirmResponse?.message), R.style.fail
                        ).show()
                        binding.edtOPT.error = strNullOrEmpty(otpConfirmResponse?.message)
                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("LoginFragment", "Login error: $errorMessage")
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.INIT -> {
                        binding.progressBar.visibility = View.GONE

                    }
                }
            }
        }

    }

    override fun setOnClick() {
        binding.btnSelect.setOnClickListener {
            Log.e("idUser",idUser)
            viewModel.otpConfirm(idUser!!, binding.edtOPT.text.toString().trim())
        }
        binding.countdownTimerTextView.setOnClickListener {
            countDownTimer?.cancel()
            startCountdownTimer()
        }
    }
}
