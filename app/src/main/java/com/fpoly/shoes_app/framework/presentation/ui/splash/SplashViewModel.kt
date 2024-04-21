package com.fpoly.shoes_app.framework.presentation.ui.splash

import androidx.lifecycle.ViewModel
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.PageSplash
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SplashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> get() = _uiState

    init {
        val list = arrayListOf(
            PageSplash(
                "1",
                "Welcome to",
                "Lorem ipsum dolor sit amet, consetetur \n sadipscing elitr, sed diam nonumy",
                R.drawable.img1
            ),
            PageSplash(
                "2",
                "Buy Quality \n Dairy Products",
                "Lorem ipsum dolor sit amet, consetetur \n sadipscing elitr, sed diam nonumy",
                R.drawable.img2
            ),
            PageSplash(
                "3",
                "Buy Premium\n Quality Fruits",
                "Lorem ipsum dolor sit amet, consetetur \n sadipscing elitr, sed diam nonumy",
                R.drawable.img3
            ),
            PageSplash(
                "4",
                "Get Discounts \n On All Products",
                "Lorem ipsum dolor sit amet, consetetur \n sadipscing elitr, sed diam nonumy",
                R.drawable.img4
            ),
        )
        _uiState.update { state ->
            state.copy(pagesSplash = list)
        }
    }

    fun getPage(currentPage: Int) {
        _uiState.update { state ->
            state.copy(page = currentPage)
        }
    }

    fun nextPage(currentPage: Int) {
        _uiState.update { state ->
            state.copy(page = currentPage + 1)
        }
    }
}