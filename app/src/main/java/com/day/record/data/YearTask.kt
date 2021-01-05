package com.day.record.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yearTasks")
data class YearTask(
    @ColumnInfo(name = "task") val task: String,
    @ColumnInfo(name = "target") val target: Int,
    @ColumnInfo(name = "progress") var progress: Int,
    @ColumnInfo(name = "isFinish") var isFinish: Boolean,
    @ColumnInfo(name = "createDate") var createDate: String,
    @ColumnInfo(name = "finishDate") var finishDate: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
        set(value) {
            field = value
        }

}