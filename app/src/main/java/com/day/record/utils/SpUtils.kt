package com.day.record.utils

import android.content.Context
import android.content.SharedPreferences
import com.day.record.MyApp

class SpUtils() {
    private lateinit var sp: SharedPreferences
    private val spName: String = "record_sp"

    private val dateKey: String = "date_key"

    constructor(context: Context): this() {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    fun setDate(date: String) {
        sp.edit().putString(dateKey, date).apply()
    }

    fun getDate(): String? {
        return sp.getString(dateKey, "2021-01-04")
    }


    companion object {

        private lateinit var instance: SpUtils

        fun getInstance() : SpUtils {
            if (!Companion::instance.isInitialized) {
                synchronized(SpUtils::class.java) {
                    if (!Companion::instance.isInitialized) {
                        instance = SpUtils(MyApp.context)
                    }
                }
            }
            return instance
        }

    }
}