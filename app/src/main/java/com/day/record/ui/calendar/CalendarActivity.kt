package com.day.record.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.day.record.R
import com.day.record.data.entity.DayTask
import com.day.record.databinding.ActivityCalendarBinding
import com.day.record.databinding.RcyItemTaskDetailNoTaskViewBinding
import com.day.record.databinding.RcyItemTaskDetailViewBinding
import com.day.record.utils.Utils
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author Jere
 */
class CalendarActivity : AppCompatActivity(), CalendarView.OnCalendarSelectListener {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private var dayTaskList: List<DayTask> = ArrayList()
    private lateinit var taskDetailListAdapter: TaskDetailListAdapter
    private lateinit var currentSelectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        val curYear = binding.calendarView.curYear
        val curMonth = binding.calendarView.curMonth
        val curDay = binding.calendarView.curDay
        val lunar = binding.calendarView.selectedCalendar.lunar
        setSelectedDate(curYear, curMonth, curDay, lunar)
        queryMonthTasksData(curYear, curMonth, curDay)

        binding.calendarView.setOnCalendarSelectListener(this)

        calendarViewModel.monthTasksDataMld.observe(this, Observer {
            setCalendarSchemeDate(it, curYear, curMonth)
        })
        calendarViewModel.dayTaskListMld.observe(this, Observer {
            dayTaskList = it
            taskDetailListAdapter.setData(dayTaskList, currentSelectedDate)
        })

        taskDetailListAdapter =
            TaskDetailListAdapter(
                this,
                dayTaskList
            )
        binding.taskRcy.adapter = taskDetailListAdapter

    }

    private fun setCalendarSchemeDate(
        map: Map<String, List<DayTask>>,
        curYear: Int,
        curMonth: Int
    ) {
        val calendarSchemeMap: MutableMap<String, Calendar?> = HashMap()

        for ((k, v) in map) {
            val taskProgress = calculateDayTaskProgress(v).toString()
            val day = k.subSequence(8, 10).toString().toInt()
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
        val selectedDate = getString(R.string.format_selected_date, year, month, day)
        binding.calendarAppBar.setTitleText(selectedDate)
        binding.selectedDateTv.text = selectedDate
        binding.calendarSelectedDayTv.text = day.toString()

        currentSelectedDate = formatDate(year!!, month!!, day!!)
        calendarViewModel.getTaskByDate(currentSelectedDate)
    }

    /**
     * query from 1 to today on the month all tasks data
     */
    private fun queryMonthTasksData(year: Int, month: Int, day: Int) {
        val dateList: MutableList<String> = ArrayList()
        for (indexDay in 1..day) {
            val date = formatDate(year, month, indexDay)
            dateList.add(date)
        }
        calendarViewModel.getAllTaskByDateList(dateList)
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


    class TaskDetailListAdapter(
        private val context: Context,
        private var dayTaskList: List<DayTask>
    ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            private const val NORMAL_TYPE = 0
            private const val NO_TASK_TYPE = 1
        }

        private var currentSelectedDate: String? = null

        fun setData(newDayTaskList: List<DayTask>, newSelectedDate: String) {
            this.dayTaskList = newDayTaskList
            currentSelectedDate = newSelectedDate
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
                binding.taskTv.text = dayTask.taskName
            }

        }

        class NoTaskViewHolder(private val binding: RcyItemTaskDetailNoTaskViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(context: Context, selectedDateString: String?) {
                if (!selectedDateString.isNullOrBlank()) {
                    val currentDate = convertStringToDate(Utils.getCurrentDate())
                    val selectedDate = convertStringToDate(selectedDateString)
                    if (currentDate?.before(selectedDate)!!) {
                        binding.noTaskTv.text =
                            context.getString(R.string.meet_in_the_future)
                    } else {
                        binding.noTaskTv.text = context.getString(R.string.no_do_any_task)
                    }
                    binding.noTaskContainerLl.visibility = View.VISIBLE
                }
            }

            @SuppressLint("SimpleDateFormat")
            private fun convertStringToDate(dateString: String): Date? {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                return dateFormat.parse(dateString, ParsePosition(0))
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == NORMAL_TYPE) {
                val binding = RcyItemTaskDetailViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MyViewHolder(
                    binding
                )
            }
            val noTaskBinding =
                RcyItemTaskDetailNoTaskViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return NoTaskViewHolder(
                noTaskBinding
            )
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
                noTaskViewHolder.bind(context, currentSelectedDate)
            }

        }
    }

}