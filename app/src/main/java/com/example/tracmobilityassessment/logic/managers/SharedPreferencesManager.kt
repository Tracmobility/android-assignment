package com.example.tracmobilityassessment.logic.managers

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class SharedPreferencesManager(private val mContext: Context) {
    fun getString(key: String): String? {
        val shared: SharedPreferences =
            mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        return shared.getString(key, "")
    }

    fun saveString(key: String, value: String?) {
        val shared: SharedPreferences =
            mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor2: Editor = shared.edit()
        editor2.putString(key, value)
        editor2.apply()
    }
}