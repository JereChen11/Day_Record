package com.day.record.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.record.MyApp
import com.day.record.data.entity.DayTask
import com.day.record.data.entity.YearTask
import com.day.record.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Jere
 */
class CrudTaskViewModel : ViewModel() {

    var crudTaskResultMld: MutableLiveData<Boolean> = MutableLiveData()

    fun insertTask(yearTask: YearTask) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                MyApp.appDatabase.yearTaskDao().insertYearTask(yearTask)

                val dayTask = DayTask(
                    yearTask.task,
                    false,
                    Utils.getCurrentDate()
                )
                MyApp.appDatabase.dayTaskDao().insertDayTask(dayTask)
            }
            crudTaskResultMld.postValue(true)
        }
    }

    fun updateTask(oldYearTask: YearTask, newYearTask: YearTask) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                MyApp.appDatabase.yearTaskDao().updateYearTasks(newYearTask)

                //update today's dayTask(rename), and not handle other days dayTask
                val dayTask = MyApp.appDatabase.dayTaskDao()
                    .findDayTaskByNameAndDate(oldYearTask.task, Utils.getCurrentDate())
                dayTask.taskName = newYearTask.task
                MyApp.appDatabase.dayTaskDao().updateDayTasks(dayTask)
            }
            crudTaskResultMld.postValue(true)
        }
    }

    fun deleteTask(oldYearTask: YearTask, newYearTask: YearTask) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                MyApp.appDatabase.yearTaskDao().deleteYearTasks(newYearTask)

                //delete today's dayTask, and not handle other days dayTask
                val dayTask =
                    MyApp.appDatabase.dayTaskDao()
                        .findDayTaskByNameAndDate(oldYearTask.task, Utils.getCurrentDate())
                MyApp.appDatabase.dayTaskDao().deleteDayTasks(dayTask)
            }
            crudTaskResultMld.postValue(true)
        }
    }
}