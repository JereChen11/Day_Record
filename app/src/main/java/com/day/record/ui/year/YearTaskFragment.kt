package com.day.record.ui.year

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.day.record.MyApp
import com.day.record.R
import com.day.record.data.YearTask
import com.day.record.databinding.FragmentYearTaskBinding
import com.day.record.databinding.RcyItemYearTaskViewBinding
import com.day.record.databinding.TaskDialogFragmentBinding
import com.day.record.ui.task.AddTaskActivity
import com.day.record.utils.MyDialogFragment

class YearTaskFragment : Fragment() {

    private lateinit var yearViewModel: YearViewModel

    private var binding: FragmentYearTaskBinding? = null
    private var yearTaskList: List<YearTask> = ArrayList()
    private var yearTaskListAdapter: YearTaskListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYearTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        yearViewModel = ViewModelProvider(this).get(YearViewModel::class.java)

        yearTaskListAdapter = context?.let { YearTaskListAdapter(it, yearTaskList) }
        yearTaskListAdapter?.setItemClickListener(object : YearTaskListAdapter.ItemClickListener {
            override fun onClick(view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onLongClick(view: View?, position: Int) {
                //todo show change year task dialog

                showDialog(yearTaskList[position].task)


            }

        })
        binding?.yearTaskRcy?.adapter = yearTaskListAdapter

        yearViewModel.yearTaskListLd.observe(viewLifecycleOwner, Observer {
            yearTaskList = it
            yearTaskListAdapter?.setData(yearTaskList)
        })

        getString(R.string.target_day, 330)
    }

    override fun onResume() {
        super.onResume()
        yearViewModel.getAllYearTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    class YearTaskListAdapter(
        private val context: Context,
        private var yearTaskList: List<YearTask>
    ) :
        RecyclerView.Adapter<YearTaskListAdapter.MyViewHolder>() {

        private lateinit var binding: RcyItemYearTaskViewBinding

        companion object {
            var itemClickListener: ItemClickListener? = null
        }

        interface ItemClickListener {
            fun onClick(view: View?, position: Int)

            fun onLongClick(view: View?, position: Int)
        }

        fun setItemClickListener(listener: ItemClickListener) {
            itemClickListener = listener
        }


        fun setData(newYearTaskList: List<YearTask>) {
            this.yearTaskList = newYearTaskList
            notifyDataSetChanged()
        }

        class MyViewHolder(private val binding: RcyItemYearTaskViewBinding) :
            RecyclerView.ViewHolder(binding.root),
            View.OnClickListener,
            View.OnLongClickListener {

            init {
                //binding.yearTaskContainerCl.setOnClickListener(this)
                binding.yearTaskContainerCl.setOnLongClickListener(this)
            }

            fun bind(context: Context, yearTask: YearTask) {
                binding.taskTv.text = yearTask.task
                binding.targetTv.text = context.getString(R.string.target_day, yearTask.target)
                binding.progressTv.text =
                    context.getString(R.string.progress_day, yearTask.progress)
                val progress: Double = (yearTask.progress * 100 / yearTask.target).toDouble()
                Log.e("jctest", "bind: ${yearTask.task} : progress = $progress")
                binding.taskProgressBar.progress = progress.toInt()
            }

            override fun onClick(v: View?) {
                TODO("Not yet implemented")
            }

            override fun onLongClick(v: View?): Boolean {
                itemClickListener?.onLongClick(v, adapterPosition)
                return false
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            binding = RcyItemYearTaskViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MyViewHolder(binding)
        }

        override fun getItemCount(): Int = yearTaskList.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(context, yearTaskList[position])
        }
    }

    class TaskDialogFragment : DialogFragment(), View.OnClickListener {

        private var binding: TaskDialogFragmentBinding? = null
        private var taskName: String? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = TaskDialogFragmentBinding.inflate(inflater, container, false)
            return binding?.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            taskName = arguments?.getString(TASK_NAME_KEY)

            binding?.updateTaskTv?.setOnClickListener(this)
            binding?.deleteTaskTv?.setOnClickListener(this)
            binding?.cancelTv?.setOnClickListener(this)

            view.scaleX = 0F
            view.scaleY = 0F
            val holder5 = PropertyValuesHolder.ofFloat("scaleX", 1F)
            val holder6 = PropertyValuesHolder.ofFloat("scaleY", 1F)
            val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                view,
                holder5,
                holder6
            )
            objectAnimator.duration = 500
            objectAnimator.start()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            binding = null
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.updateTaskTv -> {
                    goToTaskActivity(AddTaskActivity.UPDATE_OPERATE)
                }
                R.id.deleteTaskTv -> {
                    goToTaskActivity(AddTaskActivity.DELETE_OPERATE)
                }
                R.id.cancelTv -> dialog?.dismiss()
            }
        }

        private fun goToTaskActivity(operateType: Int) {
            val bundle = Bundle()
            bundle.putInt(AddTaskActivity.OPERATE_TYPE_KEY, operateType)
            bundle.putString(AddTaskActivity.TASK_NAME_KEY, taskName)
            val intent = Intent(context, AddTaskActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        companion object {

            const val TAG = "TaskDialogFragment"
            private const val TASK_NAME_KEY = "TASK_NAME"

            fun newInstance(title: String): MyDialogFragment {
                return MyDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(TASK_NAME_KEY, title)
                    }
                }
            }
        }

    }

    private fun showDialog(taskName: String) {
        val ft = childFragmentManager.beginTransaction()
        val taskDialogFragment = TaskDialogFragment.newInstance(taskName)
        taskDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_style)
        taskDialogFragment.show(ft, MyDialogFragment.TAG)
    }

}