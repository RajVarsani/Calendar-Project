package com.example.thehinducalender.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.FrameLayout

class OpacityAnimator {
    fun setOpacityTo(
        container: FrameLayout,
        time_duration: Long,
        fromOpacity: Float,
        toOpacity: Float
    ) {

//        val animator1 = ObjectAnimator.ofFloat(container, "alpha", fromOpacity).apply {
//            duration = 0
//        }
        val animator = ObjectAnimator.ofFloat(container, "alpha", fromOpacity, toOpacity)

        AnimatorSet().apply {
            play(animator)
            duration = time_duration
            start()
        }
    }


}