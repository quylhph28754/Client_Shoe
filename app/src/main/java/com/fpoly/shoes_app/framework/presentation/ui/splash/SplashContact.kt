package com.fpoly.shoes_app.framework.presentation.ui.splash

import com.fpoly.shoes_app.framework.domain.model.PageSplash

data class SplashUiState(
    val pagesSplash: List<PageSplash>? = null,
    val page: Int = 0,
    val isNavigateToNextScreen: Boolean = false,
    val textButton: String? = null
)