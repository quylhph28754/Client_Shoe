package com.fpoly.shoes_app.framework.data.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE)

    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()

    @Provides
    fun provideSharedPreferencesManager(
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ): SharedPreferencesManager =
        SharedPreferencesManager.apply {
            this.sharedPreferences = sharedPreferences
            this.editor = editor
        }
}