package com.example.sleephony.data.repository

import com.example.sleephony.data.datasource.remote.theme.ThemeApi
import com.example.sleephony.data.model.theme.ThemeListResult
import com.example.sleephony.data.model.theme.ThemeResult
import com.example.sleephony.domain.repository.ThemeRepository
import com.example.sleephony.utils.TokenProvider
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val api: ThemeApi,
    private val tokenProvider: TokenProvider
) : ThemeRepository {
    override suspend fun getThemeList() : Result<List<ThemeListResult>> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response = api.getThemeList(bearer)

            response.results
                ?: throw RuntimeException("테마 목록 정보가 없습니다.")
        }

    override suspend fun getTheme(themeId: Int): Result<ThemeResult> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response = api.getTheme(themeId, bearer)

            response.results
                ?: throw RuntimeException("테마 정보가 없습니다.")
        }
}