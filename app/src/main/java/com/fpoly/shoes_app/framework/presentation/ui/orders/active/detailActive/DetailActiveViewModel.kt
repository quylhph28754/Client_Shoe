package com.fpoly.shoes_app.framework.presentation.ui.orders.active.detailActive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.history.confirmTake.ConfirmTakeModel
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUpResponse
import com.fpoly.shoes_app.framework.repository.ConfirmTakeRepository
import com.fpoly.shoes_app.framework.repository.SignUpRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
@HiltViewModel
class DetailActiveViewModel @Inject constructor(
    private val comfirmTakeRepository: ConfirmTakeRepository,
) : ViewModel() {
    private val _signUpResult = MutableStateFlow<Resource<ConfirmTakeModel>>(Resource.init(null))
    val confirmTakeResult: StateFlow<Resource<ConfirmTakeModel>> = _signUpResult

    fun confirmTakeDetailVM(id: String) {
        viewModelScope.launch {
            _signUpResult.value = Resource.loading(null)
            try {
                val response = comfirmTakeRepository.confirmTakrRepon(id)
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        _signUpResult.value = Resource.success(signUpResponse)
                    } else {
                        _signUpResult.value = Resource.error(null, "Sign-up response is null")
                    }
                } else {
                    _signUpResult.value = Resource.error(null, "Sign-up failed")
                }
            } catch (e: HttpException) {
                _signUpResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _signUpResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}