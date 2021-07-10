package com.example.thehinducalender.models

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView


class SquareCardView(context: Context?, attrs: AttributeSet?) :
    CardView(context!!, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, ignoredHeightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
