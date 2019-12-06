package com.rgo47.daggertest.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(val context: Context) {

    var sp: SharedPreferences = context.getSharedPreferences("rgo47", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sp.edit()

    fun getString(key: String, defaultValue: String): String {
        return sp.getString(key, defaultValue)!!
    }

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

}