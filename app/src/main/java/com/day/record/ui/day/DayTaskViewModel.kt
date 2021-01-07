package com.day.record.ui.day

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.record.MyApp
import com.day.record.data.entity.DayTask
import com.day.record.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Jere
 */
class DayTaskViewModel : ViewModel() {

    val dayTaskListLd: MutableLiveData<List<DayTask>> = MutableLiveData()

    fun getCurrentDateAllTasks() {
        viewModelScope.launch(Dispatchers.Main) {
            val taskList = withContext(Dispatchers.IO) {
                MyApp.appDatabase.dayTaskDao().findDayTaskByDate(Utils.getCurrentDate())
            }
            dayTaskListLd.postValue(taskList)
        }
    }

    fun updateTask(isFinish: Boolean, dayTask: DayTask) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                MyApp.appDatabase.dayTaskDao().updateDayTasks(dayTask)

                //update yearTask progress
                val yearTask = MyApp.appDatabase.yearTaskDao().findYearTaskByName(dayTask.taskName)
                if (isFinish) {
                    yearTask.progress++
                } else {
                    yearTask.progress--
                }
                if (yearTask.progress == yearTask.target) {
                    yearTask.isFinish = true
                    yearTask.finishDate = Utils.getCurrentDate()
                }
                MyApp.appDatabase.yearTaskDao().updateYearTasks(yearTask)
            }
        }
    }

    fun resetDayTask() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                val yearTaskList = MyApp.appDatabase.yearTaskDao().getAllYearTasks()
                val newCurrentDate = Utils.getCurrentDate()
                for (yearTask in yearTaskList) {
                    val dayTask = DayTask(
                        yearTask.task,
                        false,
                        newCurrentDate
                    )
                    MyApp.appDatabase.dayTaskDao().insertDayTask(dayTask)
                }
            }
            getCurrentDateAllTasks()
        }
    }
}