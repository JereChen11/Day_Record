package com.day.record.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.record.MyApp
import com.day.record.data.DayTask
import com.day.record.data.YearTask
import com.day.record.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTaskViewModel : ViewModel() {

    var insertTaskResultMld: MutableLiveData<Boolean> = MutableLiveData()

    fun insertTask(yearTask: YearTask) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                MyApp.appDatabase.yearTaskDao().insertYearTask(yearTask)

                val dayTask = DayTask(yearTask.task, false, Utils.getCurrentDate())
                MyApp.appDatabase.dayTaskDao().insertDayTask(dayTask)
            }
            insertTaskResultMld.postValue(true)
        }
    }
}