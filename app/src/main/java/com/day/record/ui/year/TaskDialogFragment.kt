package com.day.record.ui.year

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import com.day.record.R
import com.day.record.data.entity.YearTask
import com.day.record.databinding.TaskDialogFragmentBinding
import com.day.record.ui.task.CrudTaskActivity
import com.day.record.utils.Utils

class TaskDialogFragment : DialogFragment(), View.OnClickListener {

    private var binding: TaskDialogFragmentBinding? = null
    private var yearTask: YearTask? = null

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

        yearTask = arguments?.getSerializable(YEAR_TASK_KEY) as YearTask?

        binding?.apply {
            updateTaskTv.setOnClickListener(this@TaskDialogFragment)
            deleteTaskTv.setOnClickListener(this@TaskDialogFragment)
            cancelTv.setOnClickListener(this@TaskDialogFragment)
            fakeContentView.setOnClickListener(this@TaskDialogFragment)
        }


        //listener system back button
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(
                dialog: DialogInterface?,
                keyCode: Int,
                event: KeyEvent?
            ): Boolean {
                Log.e("jctest", "onKey: keyCode = $keyCode")
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissDialog()
                    //if you don't want system dismiss the dialog directly, set return true
                    return true
                }
                return false
            }

        })


    }

    /**
     * custom dismiss dialog animation, rather than use system dismiss dialog directly
     */
    private fun dismissDialog() {
        val oa = ObjectAnimator.ofFloat(view, "translationY", Utils.dpToPixel(220F))
        oa.duration = 400
        oa.start()

        oa.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                dialog?.dismiss()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.updateTaskTv -> {
                goToTaskActivity(CrudTaskActivity.UPDATE_OPERATE)
            }
            R.id.deleteTaskTv -> {
                goToTaskActivity(CrudTaskActivity.DELETE_OPERATE)
            }
//                R.id.cancelTv -> dialog?.dismiss()
        }
        dismissDialog()
    }

    private fun goToTaskActivity(operateType: Int) {
        val bundle = Bundle()
        bundle.putInt(CrudTaskActivity.OPERATE_TYPE_KEY, operateType)
        bundle.putSerializable(CrudTaskActivity.YEAR_TASK_KEY, yearTask)
        val intent = Intent(context, CrudTaskActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        dialog?.dismiss()
    }

    companion object {

        const val TAG = "TaskDialogFragment"
        private const val YEAR_TASK_KEY = "YEAR_TASK"

        fun newInstance(yearTask: YearTask): TaskDialogFragment {
            return TaskDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(YEAR_TASK_KEY, yearTask)
                }
            }
        }
    }

}