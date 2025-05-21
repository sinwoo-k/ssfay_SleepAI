package com.example.sleephony.data.datasource.remote.theme

import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.theme.ThemeListResult
import com.example.sleephony.data.model.theme.ThemeResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ThemeApi {
    @GET("themes")
    suspend fun getThemeList(
        @Header("Authorization") bearer: String
    ) : ApiResponse<List<ThemeListResult>>

    @GET("themes/{themeId}")
    suspend fun getTheme(
        @Path("themeId") themeId : Int,
        @Header("Authorization") bearer: String
    ) : ApiResponse<ThemeResult>
}