package com.example.thehinducalender.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout


class NavbarAnimator {
    fun setActive(
        container: ConstraintLayout,
        iconImage1: ImageView,
        iconImage2: ImageView,
        time_duration: Long,
        distance: Int,
    ) {

//        val animator1 = ObjectAnimator.ofFloat(iconImage1, "translationY", -distance.toFloat())
//        val animator2 = ObjectAnimator.ofFloat(iconImage2, "translationY", -distance.toFloat())
        val animator3 = ObjectAnimator.ofFloat(iconImage1, "alpha", 1f, 0f)
        val animator4 = ObjectAnimator.ofFloat(iconImage2, "alpha", 0f, 1f)
        val animator5 = ObjectAnimator.ofFloat(container, "translationY", -distance.toFloat())

        (container.background as TransitionDrawable).reverseTransition(0)
        (container.background as TransitionDrawable).startTransition(time_duration.toInt())


        AnimatorSet().apply {
//            play(animator1).with(animator2)
//            play(animator1).with(animator3)
            play(animator3).with(animator4)
            play(animator3).with(animator5)
            duration = time_duration
            start()
        }
    }

    fun setInactive(
        container: ConstraintLayout,
        iconImage1: ImageView,
        iconImage2: ImageView,
        time_duration: Long
    ) {

//        val animator1 = ObjectAnimator.ofFloat(iconImage1, "translationY", distance.toFloat())
//        val animator2 = ObjectAnimator.ofFloat(iconImage2, "translationY", distance.toFloat())
        val animator3 = ObjectAnimator.ofFloat(iconImage1, "alpha", 0f, 1f)
        val animator4 = ObjectAnimator.ofFloat(iconImage2, "alpha", 1f, 0f)
        val animator5 = ObjectAnimator.ofFloat(container, "translationY",0f)

        (container.background as TransitionDrawable).startTransition(0)
        (container.background as TransitionDrawable).reverseTransition(time_duration.toInt())



        AnimatorSet().apply {
//            play(animator1).with(animator2)
//            play(animator1).with(animator3)
            play(animator3).with(animator4)
            play(animator3).with(animator5)
            duration = time_duration
            start()
        }
    }
}