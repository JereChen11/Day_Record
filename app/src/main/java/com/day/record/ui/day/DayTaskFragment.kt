package com.day.record.ui.day

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.day.record.data.entity.DayTask
import com.day.record.databinding.FragmentDayTaskBinding
import com.day.record.databinding.RcyItemDayTaskViewBinding
import com.day.record.databinding.RcyItemTaskBottomViewBinding
import com.day.record.ui.task.CrudTaskActivity
import com.day.record.utils.SpUtils
import com.day.record.utils.Utils

/**
 * @author Jere
 */
class DayTaskFragment : Fragment() {

    private lateinit var dayTaskViewModel: DayTaskViewModel
    private var binding: FragmentDayTaskBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dayTaskViewModel = ViewModelProvider(this)[DayTaskViewModel::class.java]
        dayTaskViewModel.dayTaskListLd.observe(viewLifecycleOwner, {
            binding?.dayTaskRcy?.adapter = DayTaskListAdapter(it,
                object : DayTaskListAdapter.ItemCheckBoxListener {
                    override fun onChange(view: View, position: Int, isChecked: Boolean) {
                        val dayTask = it[position]
                        dayTask.isFinish = isChecked
                        dayTaskViewModel.updateTask(isChecked, dayTask)
                    }
                })
        })

    }

    override fun onResume() {
        super.onResume()
        //todo need add new dayTask on new day
        if (SpUtils.getInstance().getDate() != Utils.getCurrentDate()) {
            dayTaskViewModel.resetDayTask()
            SpUtils.getInstance().setDate(Utils.getCurrentDate())
        } else {
            dayTaskViewModel.getCurrentDateAllTasks()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    class DayTaskListAdapter(
        private var dayTaskList: List<DayTask>,
        private val itemCheckBoxListener: ItemCheckBoxListener
    ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            private const val NORMAL_TYPE = 0
            private const val BOTTOM_TYPE = 1
        }

        interface ItemCheckBoxListener {
            fun onChange(view: View, position: Int, isChecked: Boolean)
        }

        class MyViewHolder(
            private val binding: RcyItemDayTaskViewBinding,
            private val itemCheckBoxListener: ItemCheckBoxListener
        ) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(dayTask: DayTask) {
                binding.checkbox.text = dayTask.taskName
                binding.checkbox.isChecked = dayTask.isFinish

                binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    itemCheckBoxListener.onChange(buttonView, bindingAdapterPosition, isChecked)
                }
            }

        }

        class BottomViewHolder(private val binding: RcyItemTaskBottomViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind() {
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    kotlin.run {
                        binding.addTaskContainerLl.visibility = View.VISIBLE
                    }
                }, 100)
                binding.addTaskContainerLl.setOnClickListener {
                    val intent = Intent(it.context, CrudTaskActivity::class.java)
                    val bundle = Bundle()
                    bundle.putInt(CrudTaskActivity.OPERATE_TYPE_KEY, CrudTaskActivity.ADD_OPERATE)
                    intent.putExtras(bundle)
                    it.context.startActivity(intent)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == NORMAL_TYPE) {
                val binding = RcyItemDayTaskViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MyViewHolder(binding, itemCheckBoxListener)
            }
            val bottomBinding =
                RcyItemTaskBottomViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return BottomViewHolder(bottomBinding)
        }

        override fun getItemViewType(position: Int): Int {
            if (itemCount == 0 || position == itemCount - 1) {
                return BOTTOM_TYPE
            }
            return NORMAL_TYPE
        }

        override fun getItemCount(): Int {
            return dayTaskList.size + 1
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val type = getItemViewType(position)
            if (type == NORMAL_TYPE) {
                val myViewHolder = holder as MyViewHolder
                myViewHolder.bind(dayTaskList[position])
            } else {
                val bottomViewHolder = holder as BottomViewHolder
                bottomViewHolder.bind()
            }

        }
    }

}