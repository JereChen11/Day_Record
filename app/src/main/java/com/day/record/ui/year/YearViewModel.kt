package com.day.record.ui.year

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.record.MyApp
import com.day.record.data.YearTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class YearViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Year Fragment"
    }
    val text: LiveData<String> = _text

    val yearTaskListLd: MutableLiveData<List<YearTask>> = MutableLiveData()

    fun getAllYearTasks() {
        viewModelScope.launch(Dispatchers.Main) {
            val taskList = withContext(Dispatchers.IO) {
                MyApp.appDatabase.yearTaskDao().getAllYearTasks()
            }
            yearTaskListLd.postValue(taskList)
        }
    }

    fun updateYearTask(yearTask: YearTask) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                MyApp.appDatabase.yearTaskDao().updateYearTasks(yearTask)
            }

        }
    }
}