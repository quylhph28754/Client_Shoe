package com.fpoly.shoes_app.framework.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.usecase.GetCategoriesUseCase
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferencesManager,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        getDataCategories()
    }

    private fun getDataCategories() {
        val more = Category(
            image = "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
            name = "More"
        )
        viewModelScope.launch {
            val categories = getCategoriesUseCase.invoke().body() ?: emptyList()
            _uiState.update { state ->
                val categoriesUpdate =
                    if (categories.size == CATEGORIES_SIZE_EQUALS_8) categories.take(
                        CATEGORIES_SIZE_EQUALS_8
                    )
                    else if (categories.size > CATEGORIES_SIZE_EQUALS_8) categories.take(
                        CATEGORIES_SIZE_MORE_THAN_8
                    ) + more
                    else categories
                Log.d("123123", "$categoriesUpdate")
                state.copy(categories = categoriesUpdate)
            }
        }
    }

    private companion object {
        private const val CATEGORIES_SIZE_EQUALS_8 = 8
        private const val CATEGORIES_SIZE_MORE_THAN_8 = 7
    }
}