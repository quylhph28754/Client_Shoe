// LoginViewModel.kt
package com.fpoly.shoes_app.framework.presentation.ui.login

import androidx.lifecycle.ViewModel
import com.fpoly.shoes_app.framework.data.dataremove.api.post.LoginApi
import com.fpoly.shoes_app.framework.domain.model.login.Login
import com.fpoly.shoes_app.framework.domain.model.login.LoginResult
import com.fpoly.shoes_app.framework.retrofitGeneral.RetrofitGeneral
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    private val loginApi: LoginApi = RetrofitGeneral.retrofitInstance.create(LoginApi::class.java)

    fun signIn(username: String, password: String): Flow<LoginResult> = flow {
        try {
            val response = loginApi.signIn(Login(username, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    emit(LoginResult.Success(loginResponse))
                } else {
                    emit(LoginResult.Error("Login response is null"))
                }
            } else {
                emit(LoginResult.Error("Login failed"))
            }
        } catch (e: HttpException) {
            emit(LoginResult.Error("HTTP Error: ${e.message()}"))
        } catch (e: Exception) {
            emit(LoginResult.Error("Network error: ${e.message}"))
        }
    }
}