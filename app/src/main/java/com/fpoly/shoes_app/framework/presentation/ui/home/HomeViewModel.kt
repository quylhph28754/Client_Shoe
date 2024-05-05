package com.fpoly.shoes_app.framework.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferencesManager
) : ViewModel() {
}