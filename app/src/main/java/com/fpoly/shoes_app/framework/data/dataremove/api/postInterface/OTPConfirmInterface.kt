package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirm
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OTPConfirmInterface {
    @POST("authenticationotp")
    suspend fun OTPConfirm(@Body otpConfirmRequest: OtpConfirm): Response<OtpConfirmResponse>
}