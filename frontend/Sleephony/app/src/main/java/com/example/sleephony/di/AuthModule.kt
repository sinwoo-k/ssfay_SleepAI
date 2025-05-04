package com.example.sleephony.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.example.sleephony.BuildConfig
import com.example.sleephony.data.datasource.GoogleAuthDataSource
import com.example.sleephony.data.datasource.remote.auth.KakaoAuthDataSource

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    /* -------- 공통 의존성 -------- */
    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext ctx: Context
    ): CredentialManager = CredentialManager.create(ctx)

    /* -------- Kakao -------- */
    @Provides
    @Singleton
    fun provideKakaoAuthDataSource(
        @ApplicationContext ctx: Context
    ): KakaoAuthDataSource = KakaoAuthDataSource()

    /* -------- Google -------- */
    @Provides
    @Singleton
    fun provideGoogleAuthDataSource(
        cm: CredentialManager,
        @ApplicationContext ctx: Context
    ): GoogleAuthDataSource = GoogleAuthDataSource(
        credentialManager = cm,
        context = ctx,
        webClientId = BuildConfig.GOOGLE_OAUTH_CLIENT_ID,
        ioDispatcher = Dispatchers.IO
    )
}
