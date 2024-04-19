package com.fpoly.shoes_app.framework.presentation.ui.second

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
}