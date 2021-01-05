package com.day.record.data

import androidx.room.*

@Dao
interface DayTaskDao {
    @Query("SELECT * FROM dayTasks")
    suspend fun getAllDayTask(): List<DayTask>

    @Query("SELECT * FROM dayTasks WHERE task LIKE :task")
    suspend fun findDayTaskByName(task: String): DayTask

    @Insert
    suspend fun insertAllDayTasks(vararg dayTasks: DayTask)

    @Insert
    suspend fun insertDayTask(dayTasks: DayTask)

    @Update
    suspend fun updateDayTasks(vararg dayTasks: DayTask)

    @Delete
    suspend fun deleteDayTasks(vararg dayTasks: DayTask)
}