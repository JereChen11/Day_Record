package com.day.record.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.day.record.data.dao.DayTaskDao
import com.day.record.data.dao.YearTaskDao
import com.day.record.data.entity.DayTask
import com.day.record.data.entity.YearTask

/**
 * @author Jere
 */
@Database(entities = [YearTask::class, DayTask::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun yearTaskDao(): YearTaskDao

    abstract fun dayTaskDao(): DayTaskDao

}