package com.day.record.ui.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.day.record.R
import com.day.record.data.YearTask
import com.day.record.databinding.ActivityAddTaskBinding
import com.day.record.utils.MyDialogFragment
import com.day.record.utils.SpUtils
import com.day.record.utils.Utils

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var viewModel: AddTaskViewModel
    private var taskName: String? = null
    private var operateType: Int? = null

    companion object {
        const val TASK_NAME_KEY = "TASK_NAME"
        const val OPERATE_TYPE_KEY = "OPERATE_TYPE"
        const val ADD_OPERATE = 0
        const val UPDATE_OPERATE = 1
        const val DELETE_OPERATE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        taskName = intent.extras?.getString(TASK_NAME_KEY)
        operateType = intent.extras?.getInt(OPERATE_TYPE_KEY)

        viewModel = ViewModelProvider(this)[AddTaskViewModel::class.java]
        viewModel.insertTaskResultMld.observe(this, Observer {
            if (it) {
                val taskContent = "${binding.taskEt.text} ${binding.taskTargetEt.text}"
                showDialog(getString(R.string.add_task_successfully), taskContent)

                SpUtils.getInstance().setDate(Utils.getCurrentDate())

                binding.taskEt.text.clear()
                binding.taskTargetEt.text.clear()
                binding.taskEt.clearFocus()
                binding.taskTargetEt.clearFocus()
            }
        })

        binding.addTaskBtn.setOnClickListener {
            if (binding.taskEt.text.isNotBlank() && binding.taskTargetEt.text.isNotBlank()) {
                val yearTask = YearTask(
                    binding.taskEt.text.toString(),
                    binding.taskTargetEt.text.toString().toInt(),
                    0,
                    false,
                    Utils.getCurrentDate(),
                    Utils.getCurrentDate()
                )
                viewModel.insertTask(yearTask)
            } else {
                showDialog(
                    getString(R.string.add_task_failed),
                    getString(R.string.pls_input_right_format)
                )
            }

        }

    }

    private fun showDialog(title: String, taskContent: String) {
        val ft = supportFragmentManager.beginTransaction()

        val dialogFragment = MyDialogFragment.newInstance(
            title,
            taskContent
        )
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_style)
        dialogFragment.show(ft, MyDialogFragment.TAG)
    }


}