package com.day.record.utils

import android.content.res.Resources
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun dpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * metrics.density
    }

    /**
     * get current date, like: 2021-01-04
     */
    fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}