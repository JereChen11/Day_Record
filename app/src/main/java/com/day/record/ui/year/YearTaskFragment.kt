package com.day.record.ui.year

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.day.record.R
import com.day.record.data.entity.YearTask
import com.day.record.databinding.FragmentYearTaskBinding
import com.day.record.databinding.RcyItemYearTaskViewBinding

/**
 * @author Jere
 */
class YearTaskFragment : Fragment() {

    private lateinit var yearViewModel: YearViewModel

    private var binding: FragmentYearTaskBinding? = null

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

        yearViewModel.yearTaskListLd.observe(viewLifecycleOwner, {

            binding?.yearTaskRcy?.adapter =
                YearTaskListAdapter(it, object : YearTaskListAdapter.ItemClickListener {
                    override fun onClick(view: View?, position: Int) {

                    }

                    override fun onLongClick(view: View?, position: Int) {
                        showDialog(it[position])
                    }

                })
        })
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
        private var yearTaskList: List<YearTask>,
        private val itemClickListener: ItemClickListener
    ) :
        RecyclerView.Adapter<YearTaskListAdapter.MyViewHolder>() {

        class MyViewHolder(private val binding: RcyItemYearTaskViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(yearTask: YearTask) {
                binding.apply {
                    taskTv.text = yearTask.task
                    targetTv.text = root.context.getString(R.string.target_day, yearTask.target)
                    progressTv.text =
                        root.context.getString(R.string.progress_day, yearTask.progress)
                    val progress: Double = (yearTask.progress * 100 / yearTask.target).toDouble()
                    Log.e("jctest", "bind: ${yearTask.task} : progress = $progress")
                    taskProgressBar.progress = progress.toInt()
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                RcyItemYearTaskViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int = yearTaskList.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(yearTaskList[position])
            holder.itemView.rootView.apply {
                setOnClickListener {
                    itemClickListener.onClick(it, position)
                }
                setOnLongClickListener {
                    itemClickListener.onLongClick(it, position)
                    false
                }
            }

        }

        interface ItemClickListener {
            fun onClick(view: View?, position: Int)

            fun onLongClick(view: View?, position: Int)
        }
    }

    private fun showDialog(yearTask: YearTask) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        val taskDialogFragment = TaskDialogFragment.newInstance(yearTask)
        taskDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_style)
        ft?.let { taskDialogFragment.show(it, TaskDialogFragment.TAG) }
    }

}