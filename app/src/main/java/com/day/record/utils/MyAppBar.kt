package com.day.record.utils

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.day.record.R
import com.day.record.databinding.CustomViewMyAppBarBinding

/**
 * @author Jere
 */
class MyAppBar(
    context: Context,
    attr: AttributeSet?,
    def: Int
) : ConstraintLayout(context, attr, def) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    init {
        initView(context, attr)
    }

    private var binding: CustomViewMyAppBarBinding? = null

    private fun initView(context: Context, attr: AttributeSet?) {
        binding = CustomViewMyAppBarBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.MyAppBar)
        val titleString = typedArray.getString(R.styleable.MyAppBar_title)

        binding?.apply {
            backIconIv.setOnClickListener { (context as Activity).finish() }
            titleTv.text = titleString
        }

        typedArray.recycle()
    }

    fun setTitleText(title: String) {
        binding?.titleTv?.text = title
        invalidate()
    }

    fun setTitleTextColor(titleColor: Int) {
        binding?.titleTv?.setTextColor(titleColor)
        invalidate()
    }
}