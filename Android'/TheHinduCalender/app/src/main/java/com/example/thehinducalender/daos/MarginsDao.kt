package com.example.thehinducalender.daos

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup

class MarginsDao {

    fun setMarginTop(view: View, margin: Float) {
        view.margin(top = margin)
    }

    fun setMarginBottom(view: View, margin: Float) {
        view.margin(bottom = margin)
    }

    fun setMarginLeft(view: View, margin: Float) {
        view.margin(left = margin)
    }

    fun setMarginRight(view: View, margin: Float) {
        view.margin(right = margin)
    }


    private fun View.margin(
        left: Float? = null,
        top: Float? = null,
        right: Float? = null,
        bottom: Float? = null
    ) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    private inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    private fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    private fun Context.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()


}