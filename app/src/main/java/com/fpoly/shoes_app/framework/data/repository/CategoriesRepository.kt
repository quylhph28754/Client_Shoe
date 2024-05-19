package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.CategoriesApi
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val provideCategoriesApi: CategoriesApi) {
    suspend fun getCategories() = provideCategoriesApi.getCategories()
}