package com.cbu.mobil_dersi_projesi.data

import android.content.Context
import android.content.SharedPreferences

class AppSharedPreference(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val keyIsLogin = "isLogin"
    private val keyCurrentUserId = "currentUserId"

    fun isLogin() = sharedPreferences.getBoolean(keyIsLogin, false)

    fun setIsLogin(isLogin: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(keyIsLogin, isLogin)
        }.also {
            it.apply()
        }
    }

    fun getCurrentUserId() = sharedPreferences.getInt(keyCurrentUserId, 0)

    fun setCurrentUserId(userId: Int) {
        sharedPreferences.edit().apply {
            putInt(keyCurrentUserId, userId)
        }.also {
            it.apply()
        }
    }
}