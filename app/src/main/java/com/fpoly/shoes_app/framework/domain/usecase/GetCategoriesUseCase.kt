package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.CategoriesRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {
    suspend operator fun invoke() = categoriesRepository.getCategories()
}