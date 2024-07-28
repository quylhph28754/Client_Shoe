package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.CreateNewPassInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ForgotMailInterface
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPass
import javax.inject.Inject

class CreateNewPassRepository  @Inject constructor(
    private val apiService: CreateNewPassInterface
) {
    suspend fun newPass(newPass: NewPass) = apiService.resetPassword(newPass)
}