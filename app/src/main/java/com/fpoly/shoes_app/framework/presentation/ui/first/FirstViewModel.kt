package com.fpoly.shoes_app.framework.presentation.ui.first

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
}