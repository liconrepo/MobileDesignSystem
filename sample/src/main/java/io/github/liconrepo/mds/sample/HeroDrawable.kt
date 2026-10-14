package io.github.liconrepo.mds.sample

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

/** Brand gradient with soft circles. Colours come from the theme, so it re-brands with everything else. */
class HeroDrawable(
    @ColorInt private val start: Int,
    @ColorInt private val end: Int,
    @ColorInt private val accent: Int,
    private val cornerRadius: Float,
) : Drawable() {

    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circle = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = accent }
    private val clip = android.graphics.Path()

    override fun onBoundsChange(bounds: android.graphics.Rect) {
        fill.shader = LinearGradient(
            0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(),
            start, end, Shader.TileMode.CLAMP,
        )
        clip.reset()
        clip.addRoundRect(
            0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(),
            floatArrayOf(0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius),
            android.graphics.Path.Direction.CW,
        )
    }

    override fun draw(canvas: Canvas) {
        val w = bounds.width().toFloat()
        val h = bounds.height().toFloat()
        val save = canvas.save()
        canvas.clipPath(clip)
        canvas.drawPaint(fill)
        canvas.drawCircle(w * 0.92f, h * 0.18f, h * 0.55f, circle)
        canvas.drawCircle(w * 0.70f, h * 1.05f, h * 0.42f, circle)
        canvas.restoreToCount(save)
    }

    override fun setAlpha(alpha: Int) { fill.alpha = alpha }
    override fun setColorFilter(colorFilter: ColorFilter?) { fill.colorFilter = colorFilter }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}
