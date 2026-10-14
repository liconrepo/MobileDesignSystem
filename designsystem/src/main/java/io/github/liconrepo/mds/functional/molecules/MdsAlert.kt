package io.github.liconrepo.mds.functional.molecules

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import io.github.liconrepo.mds.R
import io.github.liconrepo.mds.perceptual.MdsTone
import io.github.liconrepo.mds.perceptual.dp
import io.github.liconrepo.mds.perceptual.mdsColor
import io.github.liconrepo.mds.perceptual.mdsPx
import io.github.liconrepo.mds.perceptual.mdsPxF
import io.github.liconrepo.mds.perceptual.rippleOf
import io.github.liconrepo.mds.perceptual.roundedRect
import io.github.liconrepo.mds.perceptual.withAlpha

/**
 * An inline message about the current context. Announced politely to screen readers.
 * The icon changes with the tone so meaning never depends on colour alone.
 */
public class MdsAlert @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsAlertStyle,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val iconView = AppCompatImageView(context)
    private val titleView = AppCompatTextView(context)
    private val messageView = AppCompatTextView(context)
    private val dismissView = AppCompatImageButton(context)

    public var tone: MdsTone = MdsTone.Info
        set(value) {
            if (field != value) { field = value; refresh() }
        }

    public var title: CharSequence?
        get() = titleView.text
        set(value) {
            titleView.text = value
            titleView.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
        }

    public var message: CharSequence?
        get() = messageView.text
        set(value) { messageView.text = value }

    public var isDismissible: Boolean
        get() = dismissView.visibility == VISIBLE
        set(value) { dismissView.visibility = if (value) VISIBLE else GONE }

    /** Called after the alert hides itself. */
    public var onDismiss: (() -> Unit)? = null

    init {
        orientation = HORIZONTAL
        gravity = Gravity.TOP
        val pad = context.mdsPx(R.dimen.mds_space_lg)
        setPadding(pad, pad, pad / 2, pad)

        val iconSize = context.mdsPx(R.dimen.mds_icon_lg)
        addView(iconView, LayoutParams(iconSize, iconSize).apply { marginEnd = context.mdsPx(R.dimen.mds_space_md) })

        TextViewCompat.setTextAppearance(titleView, R.style.Mds_TextAppearance_TitleSmall)
        TextViewCompat.setTextAppearance(messageView, R.style.Mds_TextAppearance_BodySmall)
        val column = LinearLayout(context).apply {
            orientation = VERTICAL
            addView(titleView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            addView(messageView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        }
        addView(column, LayoutParams(0, WRAP_CONTENT, 1f))

        dismissView.apply {
            setImageResource(R.drawable.mds_ic_close)
            scaleType = ImageView.ScaleType.CENTER
            contentDescription = context.getString(R.string.mds_dismiss)
            visibility = GONE
            background = rippleOf(context.mdsColor(R.attr.mdsColorOnSurface).withAlpha(0.12f), context.mdsPxF(R.dimen.mds_radius_full))
            setOnClickListener {
                visibility = GONE
                onDismiss?.invoke()
            }
        }
        val touch = context.mdsPx(R.dimen.mds_touch_target)
        addView(dismissView, LayoutParams(touch, touch).apply { topMargin = -context.dp(12); bottomMargin = -context.dp(12) })

        ViewCompat.setAccessibilityLiveRegion(this, ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE)

        context.withStyledAttributes(attrs, R.styleable.MdsAlert, defStyleAttr, 0) {
            tone = MdsTone.fromAttr(getInt(R.styleable.MdsAlert_mdsTone, 2))
            title = getString(R.styleable.MdsAlert_mdsAlertTitle)
            message = getString(R.styleable.MdsAlert_mdsAlertMessage)
            isDismissible = getBoolean(R.styleable.MdsAlert_mdsAlertDismissible, false)
        }
        refresh()
    }

    @DrawableRes
    private fun iconFor(tone: MdsTone): Int = when (tone) {
        MdsTone.Success -> R.drawable.mds_ic_success
        MdsTone.Warning -> R.drawable.mds_ic_warning
        MdsTone.Danger -> R.drawable.mds_ic_error
        else -> R.drawable.mds_ic_info
    }

    private fun refresh() {
        val c = context
        iconView.setImageResource(iconFor(tone))
        ImageViewCompat.setImageTintList(iconView, ColorStateList.valueOf(tone.solid(c)))
        ImageViewCompat.setImageTintList(dismissView, ColorStateList.valueOf(tone.onContainer(c)))
        titleView.setTextColor(tone.onContainer(c))
        messageView.setTextColor(tone.onContainer(c))
        background = roundedRect(
            c.mdsPxF(R.dimen.mds_radius_md),
            ColorStateList.valueOf(tone.container(c)),
            c.mdsPx(R.dimen.mds_stroke_thin),
            ColorStateList.valueOf(tone.solid(c).withAlpha(0.35f)),
        )
    }
}
