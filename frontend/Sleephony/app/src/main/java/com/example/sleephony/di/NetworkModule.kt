package com.example.sleephony.di

import com.example.sleephony.BuildConfig
import com.example.sleephony.data.datasource.remote.auth.AuthApi
import com.example.sleephony.data.datasource.remote.user.UserApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // retrofit 인스턴스 제공
    @Provides @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.SLEEPHONY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    // AuthApi 제공
    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    // UserApi 제공
    @Provides @Singleton
    fun providerUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)
}
