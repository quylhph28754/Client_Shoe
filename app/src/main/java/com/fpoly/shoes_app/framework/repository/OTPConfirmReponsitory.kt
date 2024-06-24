package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.OTPConfirmInterface
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirm
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirmResponse
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class OTPConfirmReponsitory @Inject constructor(
    private val apiService: OTPConfirmInterface
) {
    suspend fun otpConfirm(id: String,otp:String): Resource<OtpConfirmResponse> {
        return try {
            val response = apiService.OTPConfirm(OtpConfirm(id,otp))
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(null, response.message())
            }
        } catch (e: Exception) {
            Resource.error(null, "An error occurred during login: ${e.message}")
        }
    }
}