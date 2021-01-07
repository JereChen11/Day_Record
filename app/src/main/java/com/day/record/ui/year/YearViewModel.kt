package com.day.record.ui.year

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.record.MyApp
import com.day.record.data.entity.YearTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Jere
 */
class YearViewModel : ViewModel() {

    val yearTaskListLd: MutableLiveData<List<YearTask>> = MutableLiveData()

    fun getAllYearTasks() {
        viewModelScope.launch(Dispatchers.Main) {
            val taskList = withContext(Dispatchers.IO) {
                MyApp.appDatabase.yearTaskDao().getAllYearTasks()
            }
            yearTaskListLd.postValue(taskList)
        }
    }

}