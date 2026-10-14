package io.github.liconrepo.mds

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes

/**
 * Resolves a semantic colour attribute (for example [R.attr.mdsColorPrimary]) against this context's theme.
 *
 * Components call this instead of reading raw colours, so they follow dark mode and brand overlays.
 */
@ColorInt
public fun Context.mdsColor(@AttrRes attr: Int): Int {
    val value = TypedValue()
    check(theme.resolveAttribute(attr, value, true)) {
        "Theme attribute ${resources.getResourceName(attr)} is not set. Apply Theme.Mds (or a child) to this context."
    }
    return value.data
}

/** Resolves a dimension token (for example [R.dimen.mds_space_lg]) to pixels. */
public fun Context.mdsPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)

internal fun Context.mdsPxF(@DimenRes id: Int): Float = resources.getDimension(id)

internal fun Context.dp(value: Int): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics).toInt()

@ColorInt
internal fun Int.withAlpha(fraction: Float): Int =
    Color.argb((255 * fraction).toInt().coerceIn(0, 255), Color.red(this), Color.green(this), Color.blue(this))

/** Builds a colour state list for the three states every control cares about. */
internal fun stateColors(
    @ColorInt normal: Int,
    @ColorInt disabled: Int = normal,
    @ColorInt pressed: Int = normal,
): ColorStateList = ColorStateList(
    arrayOf(
        intArrayOf(-android.R.attr.state_enabled),
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf(),
    ),
    intArrayOf(disabled, pressed, normal),
)

internal fun roundedRect(
    radius: Float,
    fill: ColorStateList,
    strokeWidth: Int = 0,
    stroke: ColorStateList? = null,
): GradientDrawable = GradientDrawable().apply {
    shape = GradientDrawable.RECTANGLE
    cornerRadius = radius
    color = fill
    if (strokeWidth > 0 && stroke != null) setStroke(strokeWidth, stroke)
}

/** A ripple clipped to a rounded rectangle, so touch feedback never leaks past the corners. */
internal fun rippleOf(@ColorInt color: Int, radius: Float, content: Drawable? = null): RippleDrawable {
    val mask = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = radius
        setColor(Color.BLACK)
    }
    return RippleDrawable(ColorStateList.valueOf(color), content, mask)
}
