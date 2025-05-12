package com.example.sleephony.di

import com.example.sleephony.data.repository.AuthRepositoryImpl
import com.example.sleephony.data.repository.ThemeRepositoryImpl
import com.example.sleephony.data.repository.UserRepositoryImpl
import com.example.sleephony.domain.repository.AuthRepository
import com.example.sleephony.domain.repository.ThemeRepository
import com.example.sleephony.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ) : AuthRepository

    @Binds @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ) : UserRepository

    @Binds @Singleton
    abstract fun bindThemeRepository(
        impl : ThemeRepositoryImpl
    ) : ThemeRepository
}