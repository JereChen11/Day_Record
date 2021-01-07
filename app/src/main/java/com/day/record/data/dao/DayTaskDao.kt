package com.day.record.data.dao

import androidx.room.*
import com.day.record.data.entity.DayTask

/**
 * @author Jere
 */
@Dao
interface DayTaskDao {
    @Query("SELECT * FROM dayTasks")
    suspend fun getAllDayTask(): List<DayTask>

    @Query("SELECT * FROM dayTasks WHERE taskName LIKE :taskName")
    suspend fun findDayTaskByName(taskName: String): DayTask

    @Query("SELECT * FROM dayTasks WHERE date LIKE :date")
    suspend fun findDayTaskByDate(date: String): List<DayTask>

    @Query("SELECT * FROM dayTasks WHERE taskName LIKE :taskName AND date LIKE :date")
    suspend fun findDayTaskByNameAndDate(taskName: String, date: String): DayTask

    @Insert
    suspend fun insertAllDayTasks(vararg dayTasks: DayTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayTask(dayTask: DayTask)

    @Update
    suspend fun updateDayTasks(vararg dayTasks: DayTask)

    @Delete
    suspend fun deleteDayTasks(vararg dayTasks: DayTask)
}