package com.example.mathsus_ia.data

import android.content.Context
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

private const val PREFS_NAME = "mathsus_device"
private const val KEY_DEVICE_ID = "device_id"

/**
 * A random UUID generated once per install and persisted locally.
 * Not a real account/identity — only used to group anonymous usage events.
 */
object DeviceIdentity {
    fun getOrCreateDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val existing = prefs.getString(KEY_DEVICE_ID, null)
        if (existing != null) return existing
        val newId = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_DEVICE_ID, newId).apply()
        return newId
    }

    fun localeCountry(): String = Locale.getDefault().country.ifBlank { "unknown" }

    fun timezoneId(): String = TimeZone.getDefault().id
}
