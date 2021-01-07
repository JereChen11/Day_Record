package com.day.record.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Jere
 */
@Entity(tableName = "dayTasks")
data class DayTask(
        @ColumnInfo(name = "taskName") var taskName: String,
        @ColumnInfo(name = "isFinish") var isFinish: Boolean,
        @ColumnInfo(name = "date") var date: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
        set(value) {
            field = value
        }

}