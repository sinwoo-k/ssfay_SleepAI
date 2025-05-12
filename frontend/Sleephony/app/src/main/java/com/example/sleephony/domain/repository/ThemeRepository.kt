package com.example.sleephony.domain.repository

import com.example.sleephony.data.model.theme.ThemeListResult
import com.example.sleephony.data.model.theme.ThemeResult

interface ThemeRepository {
    suspend fun getThemeList(): Result<List<ThemeListResult>>
    suspend fun getTheme(themeId : Int) :Result<ThemeResult>
}