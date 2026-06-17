package com.dogdex.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class AppSettings(
    val darkMode: Boolean = false,
    val metricUnits: Boolean = true,
)

/** Thin wrapper over SharedPreferences that also exposes the settings as a Flow so
 *  Compose can recompose the whole app when the user flips a toggle. */
class SettingsPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("dogdex_settings", Context.MODE_PRIVATE)

    private fun current() = AppSettings(
        darkMode = prefs.getBoolean(KEY_DARK, false),
        metricUnits = prefs.getBoolean(KEY_METRIC, true),
    )

    val settings: Flow<AppSettings> = callbackFlow {
        trySend(current())
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(current())
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun setDarkMode(enabled: Boolean) = prefs.edit().putBoolean(KEY_DARK, enabled).apply()
    fun setMetricUnits(enabled: Boolean) = prefs.edit().putBoolean(KEY_METRIC, enabled).apply()

    private companion object {
        const val KEY_DARK = "dark_mode"
        const val KEY_METRIC = "metric_units"
    }
}
