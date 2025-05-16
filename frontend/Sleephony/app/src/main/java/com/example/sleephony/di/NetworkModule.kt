package com.example.sleephony.di

import com.example.sleephony.BuildConfig
import com.example.sleephony.data.datasource.remote.auth.AuthApi
import com.example.sleephony.data.datasource.remote.measurement.MeasurementApi
import com.example.sleephony.data.datasource.remote.report.ReportApi
import com.example.sleephony.data.datasource.remote.theme.ThemeApi
import com.example.sleephony.data.datasource.remote.user.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)   // 커넥트 최대 30초
            .writeTimeout(60, TimeUnit.SECONDS)     // 쓰기 최대 60초
            .readTimeout(60, TimeUnit.SECONDS)      // 읽기 최대 60초
            .retryOnConnectionFailure(true)
            .build()

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

    // ThemeApi 제공
    @Provides @Singleton
    fun providerThemeApi(retrofit: Retrofit): ThemeApi =
        retrofit.create(ThemeApi::class.java)

    // MeasurementApi 제공
    @Provides @Singleton
    fun providerMeasurementApi(retrofit: Retrofit): MeasurementApi =
        retrofit.create(MeasurementApi::class.java)
        
    // ReportApi 제공
    @Provides @Singleton
    fun provideReportApi(retrofit: Retrofit): ReportApi =
        retrofit.create(ReportApi::class.java)

}
