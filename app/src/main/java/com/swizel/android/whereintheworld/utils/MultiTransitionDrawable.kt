package com.swizel.android.whereintheworld.utils

import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat

/**
 * Created by Andy on 2/08/2014.
 */
class MultiTransitionDrawable(private val mImageView: ImageView, private val mDrawables: IntArray) {
    companion object {

        private const val DEFAULT_ANIMATION_SPEED = 500
        private const val DEFAULT_ANIMATION_DELAY = 4000
    }

    private var mFirstFrame = 0
    private var mSecondFrame = 1
    private var mAnimationSpeed = DEFAULT_ANIMATION_SPEED
    private var mAnimationDelay = DEFAULT_ANIMATION_DELAY
    private val mOriginalScaleType = mImageView.scaleType

    private val mAnimationHandler = Handler()
    private val mAnimationRunnable = object : Runnable {
        override fun run() {
            val td = TransitionDrawable(
                arrayOf(
                    ResourcesCompat.getDrawable(mImageView.resources, mDrawables[mFirstFrame], null),
                    ResourcesCompat.getDrawable(mImageView.resources, mDrawables[mSecondFrame], null)
                )
            )
            mImageView.setImageDrawable(td)
            mImageView.scaleType = mOriginalScaleType

            td.startTransition(mAnimationSpeed)

            mFirstFrame = (mFirstFrame + 1) % mDrawables.size
            mSecondFrame = (mSecondFrame + 1) % mDrawables.size

            mAnimationHandler.postDelayed(this, (mAnimationDelay + mAnimationSpeed).toLong())
        }
    }

    fun setAnimationSpeed(animationSpeed: Int) {
        this.mAnimationSpeed = animationSpeed
    }

    fun setAnimationDelay(animationDelay: Int) {
        this.mAnimationDelay = animationDelay
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mImageView.setPadding(left, top, right, bottom)
    }

    fun onPause() {
        mAnimationHandler.removeCallbacks(mAnimationRunnable)
    }

    fun onResume() {
        // Show first frame and then setup delay for first transition.
        mFirstFrame = 0
        mSecondFrame = 1
        mImageView.setImageDrawable(ResourcesCompat.getDrawable(mImageView.resources, mDrawables[mFirstFrame], null))

        mAnimationHandler.postDelayed(mAnimationRunnable, mAnimationDelay.toLong())
    }

}
