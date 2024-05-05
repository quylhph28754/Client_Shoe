package com.fpoly.shoes_app.framework.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.PageSplash
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> get() = _uiState

    private val _singleEvent = MutableStateFlow<SplashSingleEvent?>(null)
    val singleEvent: Flow<SplashSingleEvent> get() = _singleEvent.filterNotNull()

    init {
        getData()
        checkSplashScreenStatus()
    }

    private fun getData() {
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

    private fun checkSplashScreenStatus() {
        if (sharedPreferences.isSplashScreenSkipped())
            navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        Log.d("123123", "navigateToNextScreen")
        _singleEvent.value = SplashSingleEvent.NavigateToNextScreen
    }

    fun getPage(currentPage: Int, totalPages: Int) {
        val textButton = if (currentPage >= totalPages - 1) GET_STARED else NEXT
        _uiState.update { state ->
            state.copy(
                page = currentPage,
                textButton = textButton
            )
        }
    }

    fun nextPage(currentPage: Int, totalPages: Int) {
        val isLastPage = currentPage >= totalPages - 1
        if (isLastPage) {
            navigateToNextScreen()
            sharedPreferences.setSplashScreenSkipped(true)
        } else {
            _uiState.update { state ->
                state.copy(
                    page = currentPage + 1
                )
            }
        }
    }

    private companion object {
        const val NEXT = "Tiếp tục"
        const val GET_STARED = "Bắt đầu"
    }
}