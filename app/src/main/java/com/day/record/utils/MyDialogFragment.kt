package com.day.record.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.day.record.databinding.MyDialogFragmentBinding

class MyDialogFragment : DialogFragment() {

    private var binding: MyDialogFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyDialogFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleString = arguments?.getString(TITLE_DATA_KEY)
        val taskString = arguments?.getString(CONTENT_DATA_KEY)
        binding?.titleTv?.text = titleString
        binding?.taskContentTv?.text = taskString

        binding?.cancelIv?.setOnClickListener {
            dialog?.dismiss()
        }

        view.scaleX = 0F
        view.scaleY = 0F

        val holder1 = PropertyValuesHolder.ofFloat("scaleX", 1.05F)
        val holder2 = PropertyValuesHolder.ofFloat("scaleY", 1.05F)
        val holder3 = PropertyValuesHolder.ofFloat("scaleX", 0.8F)
        val holder4 = PropertyValuesHolder.ofFloat("scaleY", 0.8F)
        val holder5 = PropertyValuesHolder.ofFloat("scaleX", 1F)
        val holder6 = PropertyValuesHolder.ofFloat("scaleY", 1F)
        val oa = ObjectAnimator.ofPropertyValuesHolder(
            view,
            holder1,
            holder2
        )

        val oa1 = ObjectAnimator.ofPropertyValuesHolder(
            view,
            holder3,
            holder4
        )
        oa1.duration = 400

        val oa2 = ObjectAnimator.ofPropertyValuesHolder(
            view,
            holder5,
            holder6
        )
        oa2.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(oa, oa1, oa2)
        animatorSet.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        const val TAG = "MyDialogFragment"
        private const val CONTENT_DATA_KEY = "CONTENT_DATA"
        private const val TITLE_DATA_KEY = "TITLE_DATA"

        fun newInstance(title: String, taskContent: String): MyDialogFragment {
            return MyDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE_DATA_KEY, title)
                    putString(CONTENT_DATA_KEY, taskContent)
                }
            }
        }
    }

}