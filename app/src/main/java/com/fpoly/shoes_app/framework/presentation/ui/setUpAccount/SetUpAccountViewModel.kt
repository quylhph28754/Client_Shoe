package com.fpoly.shoes_app.framework.presentation.ui.setUpAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import com.fpoly.shoes_app.framework.repository.SetUpAccountRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SetUpAccountViewModel  @Inject constructor(
    private val setUpAccountRepository: SetUpAccountRepository,
) : ViewModel() {
    private val _setUpResult = MutableStateFlow<Resource<SetUpAccountResponse>>(Resource.init(null))
    val setUpResult: StateFlow<Resource<SetUpAccountResponse>> = _setUpResult

    fun setUp(id: String,setUpAccount: SetUpAccount) {
        viewModelScope.launch {
            _setUpResult.value = Resource.loading(null)
            try {
                val response = setUpAccountRepository.signUp(id,setUpAccount)
                if (response.isSuccessful) {
                    val setUpResponse = response.body()
                    if (setUpResponse != null) {
                        _setUpResult.value = Resource.success(setUpResponse)
                    } else {
                        _setUpResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _setUpResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _setUpResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _setUpResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}