package com.fpoly.shoes_app.framework.presentation.ui.home

import com.fpoly.shoes_app.framework.domain.model.Category


data class HomeUiState(
    val categories: List<Category>? = emptyList()
)