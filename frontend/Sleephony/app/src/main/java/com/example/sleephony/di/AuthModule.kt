package com.example.sleephony.di

import android.content.Context
import com.example.sleephony.data.datasource.remote.auth.KakaoAuthDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {
    // kakao sdk 호출용 DataSource
    @Provides
    fun provideKakaoAuthDataSource() = KakaoAuthDataSource()
}