package com.vikas.posedetection.data.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(private val context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)

    fun get(id: String): String {
        return when(val value = preferences.getString(id, "")) {
            null -> ""
            else -> value
        }
    }

    fun put(id: String, value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(id, value)
        editor.apply()
    }

    fun clear() {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}