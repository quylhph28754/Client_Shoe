package com.fpoly.shoes_app.framework.repository

import android.util.Log
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ForgotMailInterface
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMailResponse
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class ForgotMailRepository @Inject constructor(
    private val apiService: ForgotMailInterface
) {
    suspend fun forgotMail(username: String): Resource<ForgotMailResponse> {
        return try {
            val response = apiService.forgotMail(ForgotMail(username))
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Log.e("error",response.code().toString())
                Resource.error(null, response.message())
            }
        } catch (e: Exception) {
            Resource.error(null, "An error occurred during login: ${e.message}")
        }
    }
}