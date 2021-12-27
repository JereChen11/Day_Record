package com.day.record.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author Jere
 */
@Entity(tableName = "yearTasks")
data class YearTask(
    @ColumnInfo(name = "task") val task: String,
    @ColumnInfo(name = "target") val target: Int,
    @ColumnInfo(name = "progress") var progress: Int,
    @ColumnInfo(name = "isFinish") var isFinish: Boolean,
    @ColumnInfo(name = "createDate") var createDate: String,
    @ColumnInfo(name = "finishDate") var finishDate: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}