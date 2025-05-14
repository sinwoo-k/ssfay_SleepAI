package com.example.sleephony.data.repository

import android.util.Log
import com.example.sleephony.data.datasource.remote.measurement.MeasurementApi
import com.example.sleephony.data.model.measurement.SleepBioDataRequest
import com.example.sleephony.data.model.measurement.SleepBioDataResult
import com.example.sleephony.data.model.measurement.SleepEndMeasurementRequest
import com.example.sleephony.data.model.measurement.SleepEndMeasurementResult
import com.example.sleephony.data.model.measurement.SleepStartMeasurementRequest
import com.example.sleephony.domain.repository.MeasurementRepository
import com.example.sleephony.utils.TokenProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class MeasurementRepositoryImpl @Inject constructor(
    private val api: MeasurementApi,
    private val tokenProvider: TokenProvider
) : MeasurementRepository {
    override suspend fun startMeasurement(): Result<String> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"

            val currentInstant = Clock.System.now()
            val currentZone = TimeZone.currentSystemDefault()
            val localDateTime = currentInstant.toLocalDateTime(currentZone)

            Log.d("DBG", "측정 시작시간 $localDateTime")

            val req = SleepStartMeasurementRequest(
                startedAt = "$localDateTime"
            )

            val response =  api.startMeasurement(bearer, req)

            Log.d("DBG",  "수면 측정 시작 메시지: ${response.results}")

            response.results
                ?: throw RuntimeException("측정 시작 시간 등록에 실패하였습니다.")
        }

    override suspend fun endMeasurement(): Result<SleepEndMeasurementResult> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"

            val currentInstant = Clock.System.now()
            val currentZone = TimeZone.currentSystemDefault()
            val localDateTime = currentInstant.toLocalDateTime(currentZone)

            Log.d("DBG", "측정 종료시간 $localDateTime")

            val req = SleepEndMeasurementRequest(
                endedAt = "$localDateTime"
            )

            val response = api.endMeasurement(bearer, req)

            Log.d("DBG",  "수면 측정 정보 ${response.results}")

            response.results
                ?: throw RuntimeException("측정 종료 시간 등록에 실패하였습니다.")
        }

    override suspend fun sleepMeasurement(req: SleepBioDataRequest): Result<SleepBioDataResult> =
      runCatching {
          val token = tokenProvider.getToken()
          val bearer = "Bearer $token"

          Log.d("DBG", "$req")
          val response = api.sleepBioData(bearer, req)

          response.results
              ?: throw RuntimeException("수면 단계 측정에 실패하였습니다.")
      }
}