package com.fpoly.shoes_app.framework.presentation.ui.forgot.otpConfirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirmResponse
import com.fpoly.shoes_app.framework.repository.OTPConfirmReponsitory
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPConfirmViewModel @Inject constructor(
    private val otpConfirmRepository: OTPConfirmReponsitory
) : ViewModel() {

    private val _otpConfirmResult =
        MutableStateFlow<Resource<OtpConfirmResponse>>(Resource.init(null))
    val otpConfirmResult: StateFlow<Resource<OtpConfirmResponse>> = _otpConfirmResult

    fun otpConfirm(id: String,otp:String) {
        viewModelScope.launch {
            try {
                val result = otpConfirmRepository.otpConfirm(id,otp)
                _otpConfirmResult.value = result
            } catch (e: Exception) {
                _otpConfirmResult.value =
                    Resource.error(null, "An error occurred during login: ${e.message}")
            }
        }
    }
}