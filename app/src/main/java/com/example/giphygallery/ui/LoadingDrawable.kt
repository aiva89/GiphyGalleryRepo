package com.example.giphygallery.ui
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class LoadingDrawable() : Drawable(), ValueAnimator.AnimatorUpdateListener {
    private val paint = Paint().apply {
        color = Color.GRAY
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val animator = ValueAnimator.ofFloat(20f, 60f)
    private var currentSize = 40f

    init {
        animator.addUpdateListener(this)
        animator.duration = 500
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }
    override fun draw(p0: Canvas) {
        p0.drawCircle(bounds.width() / 2f, bounds.height() / 2f, currentSize, paint)
    }

    override fun setAlpha(p0: Int) {
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSPARENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(p0: ColorFilter?) {
        paint.colorFilter = p0
    }

    override fun onAnimationUpdate(p0: ValueAnimator) {
        currentSize = p0.animatedValue as Float
        invalidateSelf()
    }
}