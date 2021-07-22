package com.example.thehinducalender.daos

import android.content.res.Resources

class DisplayDensityDao {

    fun getDp(num: Int): Int {
        return num.toDp()
    }

    fun getPx(num: Int): Int {
        return num.toPx()
    }

    private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}