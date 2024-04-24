package com.fpoly.shoes_app.utility

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import javax.inject.Singleton

@Singleton
object SharedPreferencesManager {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: Editor

    private const val SPLASH_SCREEN_NOT_SHOW = "splash_screen_not_show"

    fun isSplashScreenSkipped(): Boolean =
        getBooleanDataByKey(SPLASH_SCREEN_NOT_SHOW)

    fun setSplashScreenSkipped(isSkipped: Boolean) {
        saveBooleanDataByKey(SPLASH_SCREEN_NOT_SHOW, isSkipped)
    }

    private fun saveStringDataByKey(key: String?, data: String?) {
        editor.putString(key, data).apply()
    }

    private fun getStringDataByKey(key: String?): String =
        sharedPreferences.getString(key, "") ?: ""

    private fun saveBooleanDataByKey(key: String?, data: Boolean) {
        editor.putBoolean(key, data).apply()
    }

    private fun getBooleanDataByKey(key: String?): Boolean =
        sharedPreferences.getBoolean(key, false)


}