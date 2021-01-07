package com.day.record

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.day.record.data.AppDatabase

/**
 * @author Jere
 */
class MyApp : Application() {

    companion object {
        lateinit var context: Context
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "task_db"
        ).build()
    }
}