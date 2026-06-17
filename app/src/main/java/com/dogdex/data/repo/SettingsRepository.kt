package com.dogdex.data.repo

import com.dogdex.data.local.AppSettings
import com.dogdex.data.local.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val prefs: SettingsPreferences,
) {
    val settings: Flow<AppSettings> = prefs.settings

    fun setDarkMode(enabled: Boolean) = prefs.setDarkMode(enabled)
    fun setMetricUnits(enabled: Boolean) = prefs.setMetricUnits(enabled)
}
