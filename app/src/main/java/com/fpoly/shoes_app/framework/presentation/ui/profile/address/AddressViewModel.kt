package com.fpoly.shoes_app.framework.presentation.ui.profile.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPass
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPassResponse
import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import com.fpoly.shoes_app.framework.repository.AllAddressRepository
import com.fpoly.shoes_app.framework.repository.CreateNewPassRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel

class AddressViewModel @Inject constructor(

private val allAddressRepository: AllAddressRepository
) : ViewModel() {
    private val _allAddressResult = MutableStateFlow<Resource<AllAddressResponse>>(Resource.init(null))
    val allAddressResult: StateFlow<Resource<AllAddressResponse>> = _allAddressResult

    fun allAddress(id:String ) {
        viewModelScope.launch {
            _allAddressResult.value = Resource.loading(null)
            try {
                val response = allAddressRepository.allAddress(id)
                if (response.isSuccessful) {
                    val allAddressResponse = response.body()
                    if (allAddressResponse != null) {
                        _allAddressResult.value = Resource.success(allAddressResponse)
                    } else {
                        _allAddressResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _allAddressResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _allAddressResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _allAddressResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}