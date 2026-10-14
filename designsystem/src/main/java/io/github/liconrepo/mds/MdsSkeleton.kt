package io.github.liconrepo.mds

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes

/**
 * A placeholder shown while content loads. Size it with layout params.
 * The shimmer is skipped when the user has turned animations off.
 */
public class MdsSkeleton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsSkeletonStyle,
) : View(context, attrs, defStyleAttr) {

    public var isCircle: Boolean = false
        set(value) { field = value; invalidate() }

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shimmerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shaderMatrix = Matrix()
    private val rect = RectF()
    private var progress = 0f
    private var shimmer: LinearGradient? = null
    private var animator: ValueAnimator? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.MdsSkeleton, defStyleAttr, 0) {
            isCircle = getBoolean(R.styleable.MdsSkeleton_mdsSkeletonCircle, false)
        }
        basePaint.color = context.mdsColor(R.attr.mdsColorSurfaceVariant)
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val highlight = context.mdsColor(R.attr.mdsColorSurface).withAlpha(0.6f)
        shimmer = LinearGradient(
            0f, 0f, w.toFloat().coerceAtLeast(1f), 0f,
            intArrayOf(Color.TRANSPARENT, highlight, Color.TRANSPARENT),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP,
        )
        shimmerPaint.shader = shimmer
    }

    override fun onDraw(canvas: Canvas) {
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        val radius = if (isCircle) minOf(width, height) / 2f else context.mdsPxF(R.dimen.mds_radius_sm)
        canvas.drawRoundRect(rect, radius, radius, basePaint)
        if (animator?.isRunning == true) {
            shaderMatrix.setTranslate((progress * 2f - 1f) * width, 0f)
            shimmer?.setLocalMatrix(shaderMatrix)
            canvas.drawRoundRect(rect, radius, radius, shimmerPaint)
        }
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        if (isVisible) startShimmer() else stopShimmer()
    }

    override fun onDetachedFromWindow() {
        stopShimmer()
        super.onDetachedFromWindow()
    }

    private fun startShimmer() {
        if (animator?.isRunning == true || MdsMotion.isReduced(context)) return
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1400
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun stopShimmer() {
        animator?.cancel()
        animator = null
    }
}
