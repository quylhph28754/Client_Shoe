package com.fpoly.shoes_app.framework.presentation.ui.signUp

import androidx.lifecycle.ViewModel
import com.fpoly.shoes_app.framework.data.dataremove.api.post.SignUpApi
import com.fpoly.shoes_app.framework.domain.model.login.SignUpResult
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.framework.retrofitGeneral.RetrofitGeneral
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class SignUpViewModel: ViewModel() {

    private val signUpApi: SignUpApi = RetrofitGeneral.retrofitInstance.create(SignUpApi::class.java)

    fun signUp(username: String, password: String): Flow<SignUpResult> = flow {
        try {
            val response = signUpApi.signUp(SignUp(username, password))
            if (response.isSuccessful) {
                val signUpResponse = response.body()
                if (signUpResponse != null) {
                    emit(SignUpResult.Success(signUpResponse))
                } else {
                    emit(SignUpResult.Error("Login response is null"))
                }
            } else {
                emit(SignUpResult.Error("Login failed"))
            }
        } catch (e: HttpException) {
            emit(SignUpResult.Error("HTTP Error: ${e.message()}"))
        } catch (e: Exception) {
            emit(SignUpResult.Error("Network error: ${e.message}"))
        }
    }
}