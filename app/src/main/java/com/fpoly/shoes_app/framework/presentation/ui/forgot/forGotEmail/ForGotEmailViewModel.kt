package com.fpoly.shoes_app.framework.presentation.ui.forgot.forGotEmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMailResponse
import com.fpoly.shoes_app.framework.repository.ForgotMailRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForGotEmailViewModel @Inject constructor(
    private val forGotEmailRepository: ForgotMailRepository
) : ViewModel() {

    private val _forGotEmailResult =
        MutableStateFlow<Resource<ForgotMailResponse>>(Resource.init(null))
    val forGotEmailResult: StateFlow<Resource<ForgotMailResponse>> = _forGotEmailResult

    fun forGotEmail(username: String) {
        viewModelScope.launch {
            try {
                val result = forGotEmailRepository.forgotMail(username)
                _forGotEmailResult.value = result
            } catch (e: Exception) {
                _forGotEmailResult.value =
                    Resource.error(null, "An error occurred during login: ${e.message}")
            }
        }
    }
}