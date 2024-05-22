package com.fpoly.shoes_app.framework.data.dataremove.api.post

import com.fpoly.shoes_app.framework.domain.model.login.Login
import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface LoginApi {
    @POST("signin")
    suspend fun signIn(@Body loginRequest: Login): Response<LoginResponse>
}
interface SignUpApi {
    @POST("register")
    suspend fun signUp(@Body signUpRequest: SignUp): Response<SignUpResponse>
}