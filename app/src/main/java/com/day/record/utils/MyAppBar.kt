package com.day.record.utils

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.day.record.R
import kotlinx.android.synthetic.main.custom_view_my_app_bar.view.*

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

    private fun initView(context: Context, attr: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.custom_view_my_app_bar, this, true)
        backIconIv.setOnClickListener { (context as Activity).finish() }

        val typedArray = context.obtainStyledAttributes(attr, R.styleable.MyAppBar)
        val titleString = typedArray.getString(R.styleable.MyAppBar_title)
        titleTv.text = titleString

        typedArray.recycle()
    }

    fun setTitleText(title: String) {
        titleTv.text = title
        invalidate()
    }

    fun setTitleTextColor(titleColor: Int) {
        titleTv.setTextColor(titleColor)
        invalidate()
    }
}