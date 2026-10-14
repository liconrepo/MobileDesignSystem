package io.github.liconrepo.mds

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt

/** Small indeterminate spinner used inside buttons. Loading is always paired with text, never colour alone. */
internal class MdsSpinnerDrawable(
    @ColorInt color: Int,
    private val sizePx: Int,
    strokePx: Float,
) : Drawable(), Animatable {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = strokePx
        this.color = color
    }
    private val arcBounds = RectF()
    private var angle = 0f

    private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = 900
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            angle = it.animatedValue as Float
            invalidateSelf()
        }
    }

    override fun draw(canvas: Canvas) {
        val inset = paint.strokeWidth / 2f
        arcBounds.set(bounds)
        arcBounds.inset(inset, inset)
        canvas.drawArc(arcBounds, angle, 270f, false, paint)
    }

    override fun getIntrinsicWidth(): Int = sizePx
    override fun getIntrinsicHeight(): Int = sizePx
    override fun setAlpha(alpha: Int) { paint.alpha = alpha }
    override fun setColorFilter(colorFilter: ColorFilter?) { paint.colorFilter = colorFilter }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun start() { if (!animator.isStarted) animator.start() }
    override fun stop() { animator.cancel() }
    override fun isRunning(): Boolean = animator.isRunning
}
