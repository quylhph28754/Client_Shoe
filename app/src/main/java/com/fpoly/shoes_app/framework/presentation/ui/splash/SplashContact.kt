package com.fpoly.shoes_app.framework.presentation.ui.splash

import com.fpoly.shoes_app.framework.domain.model.PageSplash

data class SplashUiState(
    val page: Int = 0,
    val pagesSplash: List<PageSplash>? = null
)