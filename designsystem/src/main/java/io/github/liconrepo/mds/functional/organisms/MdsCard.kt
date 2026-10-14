package io.github.liconrepo.mds.functional.organisms

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import io.github.liconrepo.mds.R
import io.github.liconrepo.mds.perceptual.mdsColor
import io.github.liconrepo.mds.perceptual.mdsPx
import io.github.liconrepo.mds.perceptual.mdsPxF
import io.github.liconrepo.mds.perceptual.rippleOf
import io.github.liconrepo.mds.perceptual.roundedRect
import io.github.liconrepo.mds.perceptual.withAlpha

/** A container for one coherent piece of content. Clickable cards get a rounded ripple automatically. */
public class MdsCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsCardStyle,
) : FrameLayout(context, attrs, defStyleAttr) {

    public enum class Variant { Flat, Outlined, Elevated }

    public var variant: Variant = Variant.Outlined
        set(value) {
            if (field != value) { field = value; refresh() }
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.MdsCard, defStyleAttr, 0) {
            variant = Variant.entries[getInt(R.styleable.MdsCard_mdsCardVariant, 1)]
        }
        clipToOutline = true
        refresh()
    }

    private fun refresh() {
        val c = context
        val radius = c.mdsPxF(R.dimen.mds_radius_lg)
        val surface = when (variant) {
            Variant.Flat -> c.mdsColor(R.attr.mdsColorSurfaceVariant)
            else -> c.mdsColor(R.attr.mdsColorSurface)
        }
        val hasStroke = variant != Variant.Flat
        background = roundedRect(
            radius,
            ColorStateList.valueOf(surface),
            if (hasStroke) c.mdsPx(R.dimen.mds_stroke_thin) else 0,
            ColorStateList.valueOf(c.mdsColor(R.attr.mdsColorOutline)),
        )
        foreground = rippleOf(c.mdsColor(R.attr.mdsColorOnSurface).withAlpha(0.08f), radius)
        elevation = if (variant == Variant.Elevated) c.mdsPxF(R.dimen.mds_elevation_low) else 0f
    }
}
