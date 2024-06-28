package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface SetUpInterface {
    @POST("updateuser/{id}")
    suspend fun setUpAccount(
        @Path("id") id: String,
        @Body signUpRequest: SetUpAccount
    ): Response<SetUpAccountResponse>
}