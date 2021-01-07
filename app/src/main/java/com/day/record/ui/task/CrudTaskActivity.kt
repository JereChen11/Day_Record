package com.day.record.ui.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.day.record.R
import com.day.record.data.entity.YearTask
import com.day.record.databinding.ActivityCrudTaskBinding
import com.day.record.utils.MyDialogFragment
import com.day.record.utils.SpUtils
import com.day.record.utils.Utils

/**
 * @author Jere
 */
class CrudTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrudTaskBinding
    private lateinit var viewModel: CrudTaskViewModel
    private var yearTask: YearTask? = null
    private var operateType: Int? = null

    companion object {
        const val YEAR_TASK_KEY = "YEAR_TASK"
        const val OPERATE_TYPE_KEY = "OPERATE_TYPE"
        const val ADD_OPERATE = 0
        const val UPDATE_OPERATE = 1
        const val DELETE_OPERATE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudTaskBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        operateType = intent.extras?.getInt(OPERATE_TYPE_KEY)
        yearTask = intent.extras?.getSerializable(YEAR_TASK_KEY) as YearTask?
        initView()

        viewModel = ViewModelProvider(this)[CrudTaskViewModel::class.java]
        viewModel.crudTaskResultMld.observe(this, Observer {
            if (it) {
                val taskContent = "${binding.taskEt.text} ${binding.taskTargetEt.text}"

                when (operateType) {
                    UPDATE_OPERATE -> {
                        showDialog(getString(R.string.update_task_successfully), taskContent)
                    }
                    DELETE_OPERATE -> {
                        showDialog(getString(R.string.delete_task_successfully), taskContent)
                    }
                    ADD_OPERATE -> {
                        showDialog(getString(R.string.add_task_successfully), taskContent)
                        SpUtils.getInstance().setDate(Utils.getCurrentDate())
                    }
                }
            }
        })

        binding.taskBtn.setOnClickListener {
            if (binding.taskEt.text.isNotBlank() && binding.taskTargetEt.text.toString()
                    .toInt() > 0
            ) {
                val newYearTask = YearTask(
                    binding.taskEt.text.toString(),
                    binding.taskTargetEt.text.toString().toInt(),
                    0,
                    false,
                    Utils.getCurrentDate(),
                    Utils.getCurrentDate()
                )
                newYearTask.id = yearTask?.id ?: 0
                when (operateType) {
                    UPDATE_OPERATE -> {
                        viewModel.updateTask(yearTask!!, newYearTask)
                    }
                    DELETE_OPERATE -> {
                        viewModel.deleteTask(yearTask!!, newYearTask)
                    }
                    ADD_OPERATE -> {
                        viewModel.insertTask(newYearTask)
                    }
                }
            } else {
                showDialog(
                    getString(R.string.failed_prompt),
                    getString(R.string.pls_input_right_format)
                )
            }

        }

    }

    private fun initView() {
        yearTask?.let {
            binding.taskEt.setText(it.task)
            binding.taskTargetEt.setText(it.target.toString())
        }

        when (operateType) {
            UPDATE_OPERATE -> {
                binding.taskBtn.text = getString(R.string.update_task)
                binding.taskAppBar.setTitleText(getString(R.string.update_task))
            }
            DELETE_OPERATE -> {
                binding.taskBtn.text = getString(R.string.delete_task)
                binding.taskAppBar.setTitleText(getString(R.string.delete_task))
            }
            ADD_OPERATE -> {
                binding.taskTargetEt.setText("21")
                binding.taskBtn.text = getString(R.string.add_task)
                binding.taskAppBar.setTitleText(getString(R.string.add_task))
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