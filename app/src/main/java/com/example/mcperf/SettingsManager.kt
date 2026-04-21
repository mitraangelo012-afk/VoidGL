package com.example.mcperf

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "mcperf_prefs"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class SettingsManager(private val context: Context) {

    companion object {
        val KEY_RES_SCALE = floatPreferencesKey("resolution_scale")
        val KEY_FPS_CAP = intPreferencesKey("fps_cap")
        val KEY_REDUCE_EFFECTS = booleanPreferencesKey("reduce_effects")
        val KEY_CPU_OPT = booleanPreferencesKey("cpu_optimization")
    }

    val prefs = context.dataStore.data

    suspend fun updateResolutionScale(value: Float) {
        context.dataStore.edit { it[KEY_RES_SCALE] = value }
    }

    suspend fun updateFpsCap(value: Int) {
        context.dataStore.edit { it[KEY_FPS_CAP] = value }
    }

    suspend fun updateReduceEffects(value: Boolean) {
        context.dataStore.edit { it[KEY_REDUCE_EFFECTS] = value }
    }

    suspend fun updateCpuOptimization(value: Boolean) {
        context.dataStore.edit { it[KEY_CPU_OPT] = value }
    }
}