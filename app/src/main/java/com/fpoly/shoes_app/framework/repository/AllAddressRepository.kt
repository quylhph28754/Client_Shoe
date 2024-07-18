package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.AlladdressInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.CreateNewPassInterface
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPass
import javax.inject.Inject

class AllAddressRepository  @Inject constructor(
    private val apiService: AlladdressInterface
) {
    suspend fun allAddress(id:String) = apiService.allAddress(id)
}