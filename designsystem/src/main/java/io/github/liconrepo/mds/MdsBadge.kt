package io.github.liconrepo.mds

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import androidx.core.widget.TextViewCompat

/** A short status label. Always pair tone with text; never rely on colour alone. */
public class MdsBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsBadgeStyle,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    public var tone: MdsTone = MdsTone.Neutral
        set(value) {
            if (field != value) { field = value; refresh() }
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.MdsBadge, defStyleAttr, 0) {
            tone = MdsTone.fromAttr(getInt(R.styleable.MdsBadge_mdsTone, 0))
        }
        TextViewCompat.setTextAppearance(this, R.style.Mds_TextAppearance_Caption)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setPadding(context.dp(10), context.dp(3), context.dp(10), context.dp(3))
        refresh()
    }

    private fun refresh() {
        setTextColor(tone.onContainer(context))
        background = GradientDrawable().apply {
            cornerRadius = context.mdsPxF(R.dimen.mds_radius_full)
            setColor(tone.container(context))
        }
    }
}
