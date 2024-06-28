package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.newPass.NewPass
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPassResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CreateNewPassInterface {
    @POST("resetpassword/{id}")
    suspend fun resetPassword(@Path("id") id: String, @Body newPasswordRequest: NewPass): Response<NewPassResponse>
}
