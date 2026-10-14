package io.github.liconrepo.mds

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.InsetDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatCheckBox

/** A toggleable filter or choice. Selection shows a check mark as well as a colour change. */
public class MdsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsChipStyle,
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

    private var ready = false

    init {
        buttonDrawable = null
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        minWidth = 0
        compoundDrawablePadding = context.mdsPx(R.dimen.mds_space_xs)

        val c = context
        val radius = c.mdsPxF(R.dimen.mds_radius_full)
        val thin = c.mdsPx(R.dimen.mds_stroke_thin)
        val checked = intArrayOf(android.R.attr.state_checked)
        val fill = ColorStateList(
            arrayOf(checked, intArrayOf()),
            intArrayOf(c.mdsColor(R.attr.mdsColorPrimaryContainer), c.mdsColor(R.attr.mdsColorSurface)),
        )
        val stroke = ColorStateList(
            arrayOf(checked, intArrayOf()),
            intArrayOf(c.mdsColor(R.attr.mdsColorPrimary), c.mdsColor(R.attr.mdsColorOutlineStrong)),
        )
        setTextColor(
            ColorStateList(
                arrayOf(checked, intArrayOf()),
                intArrayOf(c.mdsColor(R.attr.mdsColorOnPrimaryContainer), c.mdsColor(R.attr.mdsColorOnSurface)),
            ),
        )

        // Visual height 36dp inside a 48dp touch target.
        val touch = c.mdsPx(R.dimen.mds_touch_target)
        val visual = c.mdsPx(R.dimen.mds_control_height_sm)
        val inset = (touch - visual) / 2
        minimumHeight = touch
        minHeight = touch
        val shape = roundedRect(radius, fill, thin, stroke)
        background = InsetDrawable(rippleOf(c.mdsColor(R.attr.mdsColorPrimary).withAlpha(0.16f), radius, shape), 0, inset, 0, inset)
        // Set padding after the background: an InsetDrawable reports its own padding and would override ours.
        setPadding(c.dp(14), inset, c.dp(14), inset)

        ready = true
        updateIcon()
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if (ready) updateIcon()
    }

    private fun updateIcon() {
        val icon = if (isChecked) {
            AppCompatResources.getDrawable(context, R.drawable.mds_ic_check)?.mutate()?.apply {
                setTint(context.mdsColor(R.attr.mdsColorOnPrimaryContainer))
                val size = context.mdsPx(R.dimen.mds_icon_sm)
                setBounds(0, 0, size, size)
            }
        } else {
            null
        }
        setCompoundDrawablesRelative(icon, null, null, null)
    }
}
