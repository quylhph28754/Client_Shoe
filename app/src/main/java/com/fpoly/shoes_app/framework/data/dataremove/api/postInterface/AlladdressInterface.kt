package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.newPass.NewPass
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPassResponse
import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AlladdressInterface {
    @POST("findaddress/{id}")
    suspend fun allAddress(@Path("id") id: String): Response<AllAddressResponse>

}