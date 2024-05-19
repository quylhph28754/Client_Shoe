package com.fpoly.shoes_app.framework.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        getDataCategories()
    }

    private fun getDataCategories() {
        val list = arrayListOf(
            Category(
                "1",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "aaaaa"
            ),
            Category(
                "2",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "bbbbbbbbbb"
            ),
            Category(
                "3",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "vvvvvvv"
            ),
            Category(
                "4",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "ccccccc"
            ),
            Category(
                "5",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "dddddd"
            ),
            Category(
                "6",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "dddddd"
            ),
            Category(
                "7",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "dddddd"
            ),

        )
        _uiState.update { state ->
            val more = Category(
                "8",
                "https://i.pinimg.com/564x/e7/65/04/e7650458fe434cd647eafb289a569fe2.jpg",
                "More"
            )
            val categories =
                if (list.size == CATEGORIES_SIZE_EQUALS_8) list.take(CATEGORIES_SIZE_EQUALS_8)
                else if (list.size > CATEGORIES_SIZE_EQUALS_8) list.take(CATEGORIES_SIZE_MORE_THAN_8) + more
                else list
            state.copy(categories = categories)
        }
    }

    private companion object {
        private const val CATEGORIES_SIZE_EQUALS_8 = 8
        private const val CATEGORIES_SIZE_MORE_THAN_8 = 7
    }
}