package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface SetUpInterface {
    @Multipart
    @POST("updateuser/{id}")
    suspend fun setUpAccount(
        @Path("id") id: String,
        @Part imageAccount: MultipartBody.Part?,
        @Part("fullName") fullName: RequestBody,
        @Part("nameAccount") nameAccount: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("birthDay") birthDay: RequestBody,
        @Part("grender") grender: RequestBody
    ): Response<SetUpAccountResponse>
}