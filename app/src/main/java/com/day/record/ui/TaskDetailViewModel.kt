package com.day.record.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.record.MyApp
import com.day.record.data.DayTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailViewModel : ViewModel() {

    val dayTaskListMld: MutableLiveData<List<DayTask>> = MutableLiveData()

    val monthTasksDataMld: MutableLiveData<Map<String, List<DayTask>>> = MutableLiveData()

    fun getAllTaskByDateList(dateList: List<String>) {
        viewModelScope.launch(Dispatchers.Main) {
            val taskMapData: MutableMap<String, List<DayTask>> = HashMap()
            withContext(Dispatchers.IO) {
                for (date in dateList) {
                    val dayTaskList = MyApp.appDatabase.dayTaskDao().findDayTaskByDate(date)
                    taskMapData[date] = dayTaskList
                }
            }
            monthTasksDataMld.postValue(taskMapData)
        }
    }

    fun getTaskByDate(date: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val taskList = withContext(Dispatchers.IO) {
                MyApp.appDatabase.dayTaskDao().findDayTaskByDate(date)
            }
            dayTaskListMld.postValue(taskList)
        }
    }
}