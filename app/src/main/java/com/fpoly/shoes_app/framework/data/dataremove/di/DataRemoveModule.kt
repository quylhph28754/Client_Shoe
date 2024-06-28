package com.fpoly.shoes_app.framework.data.dataremove.di

import com.fpoly.shoes_app.framework.data.dataremove.api.CategoriesApi
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.CreateNewPassInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ForgotMailInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.LoginInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.OTPConfirmInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SetUpInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SignUpInterface
import com.fpoly.shoes_app.utility.BASE_URL
import com.fpoly.shoes_app.utility.SET_TIME_OUT_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataRemoveModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().connectTimeout(SET_TIME_OUT_API, TimeUnit.SECONDS)
                    .writeTimeout(SET_TIME_OUT_API, TimeUnit.SECONDS)
                    .readTimeout(SET_TIME_OUT_API, TimeUnit.SECONDS)
                    .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
                    .addInterceptor(httpLoggingInterceptor).build()
            ).build()

    @Provides
    @Singleton
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi =
        retrofit.create(CategoriesApi::class.java)

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginInterface =
        retrofit.create(LoginInterface::class.java)

    @Provides
    @Singleton
    fun provideForgotmailApi(retrofit: Retrofit): ForgotMailInterface =
        retrofit.create(ForgotMailInterface::class.java)

    @Provides
    @Singleton
    fun provideOTPConfirmApi(retrofit: Retrofit): OTPConfirmInterface =
        retrofit.create(OTPConfirmInterface::class.java)

    @Provides
    @Singleton
    fun provideSignUpmApi(retrofit: Retrofit): SignUpInterface =
        retrofit.create(SignUpInterface::class.java)

    @Provides
    @Singleton
    fun provideSetUpmApi(retrofit: Retrofit): SetUpInterface =
        retrofit.create(SetUpInterface::class.java)
@Provides
    @Singleton
    fun provideNewPassWordmApi(retrofit: Retrofit): CreateNewPassInterface =
        retrofit.create(CreateNewPassInterface::class.java)

}