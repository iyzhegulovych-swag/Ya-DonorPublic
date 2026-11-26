package com.example.manq.storage

import android.content.Context
import android.content.SharedPreferences

actual class KeyValueStore(private val prefs: SharedPreferences) {
    actual fun getString(key: String): String? = prefs.getString(key, null)
    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    actual fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}

/** Context для SharedPreferences кладе сюди MainActivity при старті. */
object AndroidStorage {
    lateinit var context: Context
}

actual fun createKeyValueStore(): KeyValueStore = KeyValueStore(AndroidStorage.context.getSharedPreferences("ya_donor", Context.MODE_PRIVATE))
