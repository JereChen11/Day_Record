package com.day.record.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.day.record.R
import com.day.record.data.DayTask
import com.day.record.databinding.ActivityTaskDetailBinding
import com.day.record.databinding.RcyItemTaskDetailNoTaskViewBinding
import com.day.record.databinding.RcyItemTaskDetailViewBinding
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView

class TaskDetailActivity : AppCompatActivity(), CalendarView.OnCalendarSelectListener {

    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var taskDetailViewModel: TaskDetailViewModel
    private var dayTaskList: List<DayTask> = ArrayList()
    private lateinit var taskDetailListAdapter: TaskDetailListAdapter

    private var curYear = 2021
    private var curMonth = 1
    private var curDay = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        taskDetailViewModel = ViewModelProvider(this)[TaskDetailViewModel::class.java]

        curYear = binding.calendarView.curYear
        curMonth = binding.calendarView.curMonth
        curDay = binding.calendarView.curDay
        val lunar = binding.calendarView.selectedCalendar.lunar
        setSelectedDate(curYear, curMonth, curDay, lunar)

        queryMonthTasksData(curYear, curMonth, curDay)

        Log.e("jctest", "onCreate: curYear = $curYear, curMonth = $curMonth, curDay = $curDay")
        val data = "$curYear-$curMonth-$curDay"

        binding.calendarView.setOnCalendarSelectListener(this)

        taskDetailViewModel.monthTasksDataMld.observe(this, Observer {
            setCalendarSchemeDate(it)
        })
        taskDetailViewModel.dayTaskListMld.observe(this, Observer {
            Log.e("jctest", "onCreate: it.size = ${it.size}")
            dayTaskList = it
            Log.e("jctest", "onCreate: dayTaskList.size = ${dayTaskList.size}")
            taskDetailListAdapter.setData(dayTaskList)
        })

        taskDetailListAdapter = TaskDetailListAdapter(dayTaskList)
        binding.taskRcy.adapter = taskDetailListAdapter

    }

    private fun setCalendarSchemeDate(map: Map<String, List<DayTask>>) {
        val calendarSchemeMap: MutableMap<String, Calendar?> = HashMap()

        for ((k, v) in map) {
            val taskProgress = calculateDayTaskProgress(v).toString()
            val day = k.subSequence(8, 10).toString().toInt()
            Log.e(
                "jctest",
                "setCalendarSchemeDate: $k taskProgress = $taskProgress excess day = $day"
            )
            val calendar = getSchemeCalendar(curYear, curMonth, day, R.color.teal_200, taskProgress)
            calendarSchemeMap[calendar.toString()] = calendar
        }
        binding.calendarView.setSchemeDate(calendarSchemeMap)

    }

    /**
     * calculate current day task progress
     *
     * 50 -> finish half tasks
     * 100 -> finish all tasks
     */
    private fun calculateDayTaskProgress(dayTasks: List<DayTask>): Int {
        //no task
        if (dayTasks.isEmpty()) {
            return 0
        }

        var finishCount = 0
        for (dayTask in dayTasks) {
            if (dayTask.isFinish) {
                finishCount++
            }
        }
        return finishCount * 100 / dayTasks.size
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String
    ): Calendar? {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color
        calendar.scheme = text
        return calendar
    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        setSelectedDate(calendar?.year, calendar?.month, calendar?.day, calendar?.lunar)
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {
        TODO("Not yet implemented")
    }

    private fun setSelectedDate(year: Int?, month: Int?, day: Int?, lunar: String?) {
        val selectedDate = "$month 月 $day 日"
        binding.selectedDateTv.text = selectedDate
        binding.selectedYearTv.text = year.toString()
        binding.selectedLunarTv.text = lunar
        binding.calendarSelectedDayTv.text = day.toString()

        taskDetailViewModel.getTaskByDate(formatDate(year!!, month!!, day!!))
    }

    /**
     * query from 1 to today on the month all tasks data
     */
    private fun queryMonthTasksData(year: Int, month: Int, day: Int) {
        val dateList: MutableList<String> = ArrayList()
        for (indexDay in 1..day) {
            val date = formatDate(year, month, indexDay)
            Log.e("jctest", "getTaskByDate: date = $date")
            dateList.add(date)
        }
        taskDetailViewModel.getAllTaskByDateList(dateList)
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        return if (month < 10 && day >= 10) {
            "$year-0$month-$day"
        } else if (month < 10 && day < 10) {
            "$year-0$month-0$day"
        } else if (month >= 10 && day < 10) {
            "$year-$month-0$day"
        } else {
            "$year-$month-$day"
        }
    }


    class TaskDetailListAdapter(private var dayTaskList: List<DayTask>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            private const val NORMAL_TYPE = 0
            private const val NO_TASK_TYPE = 1
        }

        fun setData(newDayTaskList: List<DayTask>) {
            this.dayTaskList = newDayTaskList
            notifyDataSetChanged()
        }

        class MyViewHolder(private val binding: RcyItemTaskDetailViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(dayTask: DayTask) {
                if (dayTask.isFinish) {
                    binding.isCheckedIconIv.setImageResource(R.drawable.vector_drawable_checked)
                } else {
                    binding.isCheckedIconIv.setImageResource(R.drawable.vector_drawable_uncheck)
                }
                binding.taskTv.text = dayTask.task
            }

        }

        class NoTaskViewHolder(private val binding: RcyItemTaskDetailNoTaskViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind() {
                Handler(Looper.getMainLooper()).postDelayed({
                    kotlin.run {
                        binding.noTaskContainerLl.visibility = View.VISIBLE
                    }
                }, 100)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == NORMAL_TYPE) {
                val binding = RcyItemTaskDetailViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MyViewHolder(binding)
            }
            val noTaskBinding =
                RcyItemTaskDetailNoTaskViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return NoTaskViewHolder(noTaskBinding)
        }

        override fun getItemViewType(position: Int): Int {
            if (itemCount == 1 && dayTaskList.isEmpty() && position == 0) {
                return NO_TASK_TYPE
            }
            return NORMAL_TYPE
        }

        override fun getItemCount(): Int {
            return if (dayTaskList.isEmpty()) {
                1
            } else {
                dayTaskList.size
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val type = getItemViewType(position)
            if (type == NORMAL_TYPE) {
                val myViewHolder = holder as MyViewHolder
                myViewHolder.bind(dayTaskList[position])
            } else {
                val noTaskViewHolder = holder as NoTaskViewHolder
                noTaskViewHolder.bind()
            }

        }
    }

}