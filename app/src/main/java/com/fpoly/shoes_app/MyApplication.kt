package com.fpoly.shoes_app

import android.app.Application
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
}