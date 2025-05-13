package com.example.sleephony.data.datasource.remote.measurement

import com.example.sleephony.data.model.ApiResponse
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
}