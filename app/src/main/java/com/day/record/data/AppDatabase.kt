package com.day.record.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [YearTask::class, DayTask::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun yearTaskDao(): YearTaskDao

    abstract fun dayTaskDao(): DayTaskDao

}