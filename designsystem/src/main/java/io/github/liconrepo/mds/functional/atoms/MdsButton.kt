package io.github.liconrepo.mds.functional.atoms

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.withStyledAttributes
import androidx.core.view.ViewCompat
import io.github.liconrepo.mds.R
import io.github.liconrepo.mds.perceptual.dp
import io.github.liconrepo.mds.perceptual.mdsColor
import io.github.liconrepo.mds.perceptual.mdsPx
import io.github.liconrepo.mds.perceptual.mdsPxF
import io.github.liconrepo.mds.perceptual.rippleOf
import io.github.liconrepo.mds.perceptual.roundedRect
import io.github.liconrepo.mds.perceptual.stateColors
import io.github.liconrepo.mds.perceptual.withAlpha

/**
 * The one button. Variants express importance, sizes express density.
 *
 * Use one [Variant.Primary] per screen region. Visual height is [Size]; the touch target is
 * always at least 48dp, so Small buttons stay accessible.
 */
public class MdsButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsButtonStyle,
) : AppCompatButton(context, attrs, defStyleAttr) {

    public enum class Variant { Primary, Secondary, Outline, Ghost, Destructive }
    public enum class Size { Small, Medium, Large }

    public var variant: Variant = Variant.Primary
        set(value) {
            if (field != value) { field = value; refresh() }
        }

    public var size: Size = Size.Medium
        set(value) {
            if (field != value) { field = value; refresh() }
        }

    /** While loading, taps are ignored and a spinner replaces the leading icon. The label stays visible. */
    public var isLoading: Boolean = false
        set(value) {
            if (field != value) { field = value; refresh() }
        }

    private var restingIcon: Drawable? = null
    private var spinner: MdsSpinnerDrawable? = null

    init {
        restingIcon = compoundDrawablesRelative[0]
        context.withStyledAttributes(attrs, R.styleable.MdsButton, defStyleAttr, 0) {
            variant = Variant.entries[getInt(R.styleable.MdsButton_mdsButtonVariant, 0)]
            size = Size.entries[getInt(R.styleable.MdsButton_mdsButtonSize, 1)]
            isLoading = getBoolean(R.styleable.MdsButton_mdsButtonLoading, false)
        }
        isAllCaps = false
        gravity = Gravity.CENTER
        minWidth = 0
        stateListAnimator = null
        elevation = 0f
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        refresh()
    }

    override fun performClick(): Boolean = if (isLoading) false else super.performClick()

    override fun onDetachedFromWindow() {
        spinner?.stop()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isLoading) spinner?.start()
    }

    private fun refresh() {
        val c = context
        val radius = c.mdsPxF(R.dimen.mds_radius_md)
        val disabledFill = c.mdsColor(R.attr.mdsColorSurfaceVariant)
        val disabledText = c.mdsColor(R.attr.mdsColorOnSurfaceDisabled)

        var strokeWidth = 0
        var stroke: ColorStateList? = null
        val fill: ColorStateList
        val foreground: Int
        val rippleBase: Int

        when (variant) {
            Variant.Primary -> {
                fill = stateColors(c.mdsColor(R.attr.mdsColorPrimary), disabledFill, c.mdsColor(R.attr.mdsColorPrimaryPressed))
                foreground = c.mdsColor(R.attr.mdsColorOnPrimary)
                rippleBase = foreground
            }
            Variant.Secondary -> {
                fill = stateColors(c.mdsColor(R.attr.mdsColorPrimaryContainer), disabledFill)
                foreground = c.mdsColor(R.attr.mdsColorOnPrimaryContainer)
                rippleBase = foreground
            }
            Variant.Outline -> {
                fill = ColorStateList.valueOf(0)
                strokeWidth = c.mdsPx(R.dimen.mds_stroke_thin)
                stroke = stateColors(c.mdsColor(R.attr.mdsColorOutlineStrong), c.mdsColor(R.attr.mdsColorOutline))
                foreground = c.mdsColor(R.attr.mdsColorOnSurface)
                rippleBase = foreground
            }
            Variant.Ghost -> {
                fill = ColorStateList.valueOf(0)
                foreground = c.mdsColor(R.attr.mdsColorPrimary)
                rippleBase = foreground
            }
            Variant.Destructive -> {
                fill = stateColors(c.mdsColor(R.attr.mdsColorDanger), disabledFill)
                foreground = c.mdsColor(R.attr.mdsColorOnDanger)
                rippleBase = foreground
            }
        }

        val textColors = stateColors(foreground, disabledText)
        setTextColor(textColors)

        val (heightRes, textSp, hPadDp) = when (size) {
            Size.Small -> Triple(R.dimen.mds_control_height_sm, 13f, 14)
            Size.Medium -> Triple(R.dimen.mds_control_height_md, 15f, 18)
            Size.Large -> Triple(R.dimen.mds_control_height_lg, 16f, 24)
        }
        val visualHeight = c.mdsPx(heightRes)
        val touchHeight = maxOf(visualHeight, c.mdsPx(R.dimen.mds_touch_target))
        val inset = (touchHeight - visualHeight) / 2

        setTextSize(TypedValue.COMPLEX_UNIT_SP, textSp)
        minimumHeight = touchHeight
        minHeight = touchHeight

        val shape = roundedRect(radius, fill, strokeWidth, stroke)
        val ripple = rippleOf(rippleBase.withAlpha(0.16f), radius, shape)
        background = InsetDrawable(ripple, 0, inset, 0, inset)
        // Set padding after the background: an InsetDrawable reports its own padding and would override ours.
        setPadding(c.dp(hPadDp), inset, c.dp(hPadDp), inset)

        compoundDrawablePadding = c.mdsPx(R.dimen.mds_space_sm)
        applyIcon(textColors, foreground)

        isFocusable = true
        ViewCompat.setStateDescription(this, if (isLoading) c.getString(R.string.mds_state_loading) else null)
    }

    private fun applyIcon(textColors: ColorStateList, foreground: Int) {
        spinner?.stop()
        spinner = null
        if (isLoading) {
            val iconSize = context.mdsPx(R.dimen.mds_icon_sm)
            spinner = MdsSpinnerDrawable(foreground, iconSize, context.dp(2).toFloat()).also {
                setCompoundDrawablesRelativeWithIntrinsicBounds(it, null, null, null)
                if (isAttachedToWindow) it.start()
            }
        } else {
            restingIcon?.mutate()?.setTintList(textColors)
            setCompoundDrawablesRelativeWithIntrinsicBounds(restingIcon, null, null, null)
        }
    }
}
