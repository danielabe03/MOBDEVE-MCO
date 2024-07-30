package com.mobdeve.s12.abe.daniel.mco3

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)

    fun saveUserSession(userId: Int) {
        val editor = prefs.edit()
        editor.putInt("USER_ID", userId)
        editor.apply()
    }

    fun getUserSession(): Int {
        return prefs.getInt("USER_ID", -1)
    }

    fun clearUserSession() {
        val editor = prefs.edit()
        editor.remove("USER_ID")
        editor.apply()
    }
}
