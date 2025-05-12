package com.example.sleephony.data.datasource.remote.measurement

import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.measurement.SleepBioDataRequest
import com.example.sleephony.data.model.measurement.SleepBioDataResult
import com.example.sleephony.data.model.measurement.SleepEndMeasurementRequest
import com.example.sleephony.data.model.measurement.SleepEndMeasurementResult
import com.example.sleephony.data.model.measurement.SleepStartMeasurementRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MeasurementApi {
    // 수면 측정 시작 시간 저장
    @POST("sleep/start-measurement")
    suspend fun startMeasurement(
        @Header("Authorization") bearer: String,
        @Body req: SleepStartMeasurementRequest
    ) : ApiResponse<String>

    // 수면 측정 종료 시간 저장
    @POST("sleep/end-measurement")
    suspend fun endMeasurement(
        @Header("Authorization") bearer: String,
        @Body req: SleepEndMeasurementRequest
    ) : ApiResponse<SleepEndMeasurementResult>

    // 수면 측정 단계
    @POST("sleep/bio-data")
    suspend fun sleepBioData(
        @Header("Authorization") bearer: String,
        @Body req: SleepBioDataRequest
    ) : ApiResponse<SleepBioDataResult>
}