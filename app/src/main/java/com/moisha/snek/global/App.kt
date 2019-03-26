package com.moisha.snek.global

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class App : Application() {

    init {
        instance = this
    }

    companion object {

        private var instance: App? = null
        private var settings: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun setUser(id: Int) {
            instantiate()
            editor?.putInt("uId", id)
            editor?.apply()
        }

        fun getUser(): Int {
            instantiate()
            return settings?.getInt("uId", -1) ?: -1
        }

        fun isAuth(): Boolean {
            instantiate()
            return settings?.contains("uId") ?: false
        }

        fun logOff() {
            instantiate()
            editor?.clear()
            editor?.apply()
        }

        private fun instantiate() {
            settings = PreferenceManager.getDefaultSharedPreferences(App.applicationContext())
            editor = settings?.edit()
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        settings = PreferenceManager.getDefaultSharedPreferences(App.applicationContext())
        editor = settings?.edit()
    }
}