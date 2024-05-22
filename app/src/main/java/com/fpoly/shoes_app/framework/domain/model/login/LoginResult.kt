package com.fpoly.shoes_app.framework.domain.model.login

import com.fpoly.shoes_app.framework.domain.model.signUp.SignUpResponse

sealed class LoginResult {
    data class Success(val loginResponse: LoginResponse) : LoginResult()
    data class Error(val errorMessage: String) : LoginResult()
}
sealed class SignUpResult {
    data class Success(val signUpResponse: SignUpResponse) : SignUpResult()
    data class Error(val errorMessage: String) : SignUpResult()
}