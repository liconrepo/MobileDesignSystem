package io.github.liconrepo.mds.functional.molecules

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import io.github.liconrepo.mds.R
import io.github.liconrepo.mds.perceptual.dp
import io.github.liconrepo.mds.perceptual.mdsColor
import io.github.liconrepo.mds.perceptual.mdsPx
import io.github.liconrepo.mds.perceptual.mdsPxF
import io.github.liconrepo.mds.perceptual.rippleOf
import io.github.liconrepo.mds.perceptual.roundedRect
import io.github.liconrepo.mds.perceptual.withAlpha

/** A row with an optional leading icon, a title, a subtitle and an optional chevron. */
public class MdsListItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsListItemStyle,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val iconHolder = FrameLayout(context)
    private val iconView = AppCompatImageView(context)
    private val titleView = AppCompatTextView(context)
    private val subtitleView = AppCompatTextView(context)
    private val chevronView = AppCompatImageView(context)

    public var title: CharSequence?
        get() = titleView.text
        set(value) { titleView.text = value }

    public var subtitle: CharSequence?
        get() = subtitleView.text
        set(value) {
            subtitleView.text = value
            subtitleView.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
        }

    public var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            iconView.setImageDrawable(value)
            iconHolder.visibility = if (value == null) GONE else VISIBLE
        }

    public var showChevron: Boolean
        get() = chevronView.visibility == VISIBLE
        set(value) { chevronView.visibility = if (value) VISIBLE else GONE }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        minimumHeight = context.mdsPx(R.dimen.mds_touch_target) + context.dp(8)
        val pad = context.mdsPx(R.dimen.mds_space_lg)
        setPadding(pad, context.dp(10), pad, context.dp(10))

        val c = context
        val tile = c.dp(40)
        iconHolder.background = roundedRect(c.mdsPxF(R.dimen.mds_radius_md), ColorStateList.valueOf(c.mdsColor(R.attr.mdsColorPrimaryContainer)))
        iconView.scaleType = ImageView.ScaleType.CENTER
        ImageViewCompat.setImageTintList(iconView, ColorStateList.valueOf(c.mdsColor(R.attr.mdsColorOnPrimaryContainer)))
        iconHolder.addView(iconView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        addView(iconHolder, LayoutParams(tile, tile).apply { marginEnd = c.mdsPx(R.dimen.mds_space_lg) })

        TextViewCompat.setTextAppearance(titleView, R.style.Mds_TextAppearance_TitleSmall)
        TextViewCompat.setTextAppearance(subtitleView, R.style.Mds_TextAppearance_BodySmall)
        val column = LinearLayout(c).apply {
            orientation = VERTICAL
            addView(titleView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            addView(subtitleView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        }
        addView(column, LayoutParams(0, WRAP_CONTENT, 1f))

        chevronView.setImageResource(R.drawable.mds_ic_chevron_right)
        ImageViewCompat.setImageTintList(chevronView, ColorStateList.valueOf(c.mdsColor(R.attr.mdsColorOnSurfaceMuted)))
        addView(chevronView, LayoutParams(c.mdsPx(R.dimen.mds_icon_lg), c.mdsPx(R.dimen.mds_icon_lg)))

        foreground = rippleOf(c.mdsColor(R.attr.mdsColorOnSurface).withAlpha(0.08f), 0f)

        context.withStyledAttributes(attrs, R.styleable.MdsListItem, defStyleAttr, 0) {
            title = getString(R.styleable.MdsListItem_mdsListItemTitle)
            subtitle = getString(R.styleable.MdsListItem_mdsListItemSubtitle)
            icon = getDrawable(R.styleable.MdsListItem_mdsListItemIcon)
            showChevron = getBoolean(R.styleable.MdsListItem_mdsListItemShowChevron, false)
        }
        subtitle = subtitle
        icon = icon
    }
}
