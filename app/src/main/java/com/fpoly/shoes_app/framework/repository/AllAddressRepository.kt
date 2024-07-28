package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.AlladdressInterface
import javax.inject.Inject

class AllAddressRepository  @Inject constructor(
    private val apiService: AlladdressInterface
) {
    suspend fun allAddress(id:String) = apiService.allAddress(id)
}