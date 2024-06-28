package com.fpoly.shoes_app.framework.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.usecase.GetCategoriesUseCase
import com.fpoly.shoes_app.utility.Resource
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
        flow {
            emit(Resource.loading(null))
            val result = getCategoriesUseCase.invoke()
            emit(result)
        }.onEach { resource ->
            _uiState.update { state ->
                when (resource.status) {
                    Status.LOADING -> state.copy(isLoading = true)

                    Status.SUCCESS -> state.copy(
                        categories = updateCategoriesList(resource.data?.body()),
                        isLoading = false
                    )

                    Status.ERROR -> state.copy(
                        isLoading = false,
                        errorMessage = resource.message
                    )

                    Status.INIT ->state.copy(
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateCategoriesList(categories: List<Category>?): List<Category> {
        val more = Category(
            image = "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
            name = "More"
        )
        return when {
            categories == null -> emptyList()
            categories.size == CATEGORIES_SIZE_EQUALS_8 -> categories.take(CATEGORIES_SIZE_EQUALS_8)
            categories.size > CATEGORIES_SIZE_EQUALS_8 -> categories.take(
                CATEGORIES_SIZE_MORE_THAN_8
            ) + more

            else -> categories
        }
    }

    private companion object {
        private const val CATEGORIES_SIZE_EQUALS_8 = 8
        private const val CATEGORIES_SIZE_MORE_THAN_8 = 7
    }
}