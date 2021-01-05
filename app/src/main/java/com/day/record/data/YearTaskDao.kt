package com.day.record.data

import androidx.room.*

@Dao
interface YearTaskDao {
    @Query("SELECT * FROM yearTasks")
    suspend fun getAllYearTasks(): List<YearTask>

    @Query("SELECT * FROM yearTasks WHERE uid IN (:taskIds)")
    suspend fun loadAllYearTasksByIds(taskIds: IntArray): List<YearTask>

    @Query("SELECT * FROM yearTasks WHERE task LIKE :task")
    suspend fun findYearTaskByName(task: String): YearTask

    @Insert
    suspend fun insertAllYearTask(vararg yearTasks: YearTask)

    @Insert
    suspend fun insertYearTask(yearTask: YearTask)

    @Update
    suspend fun updateYearTasks(vararg yearTasks: YearTask)

    @Delete
    suspend fun deleteYearTasks(vararg yearTasks: YearTask)
}