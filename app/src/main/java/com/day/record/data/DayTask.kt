package com.day.record.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dayTasks")
data class DayTask(
        @ColumnInfo(name = "task") val task: String,
        @ColumnInfo(name = "isFinish") var isFinish: Boolean,
        @ColumnInfo(name = "date") var date: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
        set(value) {
            field = value
        }

}