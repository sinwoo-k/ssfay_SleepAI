package com.example.sleephony.domain.repository

import com.example.sleephony.data.model.measurement.SleepBioDataRequest
import com.example.sleephony.data.model.measurement.SleepBioDataResult
import com.example.sleephony.data.model.measurement.SleepEndMeasurementResult

interface MeasurementRepository {
    suspend fun startMeasurement() : Result<String>
    suspend fun endMeasurement() : Result<SleepEndMeasurementResult>
    suspend fun sleepMeasurement(req: SleepBioDataRequest) : Result<SleepBioDataResult>
}