package io.github.liconrepo.mds.functional.molecules

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import io.github.liconrepo.mds.R
import io.github.liconrepo.mds.perceptual.dp
import io.github.liconrepo.mds.perceptual.mdsColor
import io.github.liconrepo.mds.perceptual.mdsPx
import io.github.liconrepo.mds.perceptual.mdsPxF

/**
 * A labelled text input with helper and error text.
 *
 * The label is always visible (placeholders are not labels), errors are announced to screen readers,
 * and an error is shown with an icon and a prefix as well as colour.
 */
public class MdsTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsTextFieldStyle,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var ready = false

    private val labelView = AppCompatTextView(context)
    private val box = FrameLayout(context)
    private val boxBackground = GradientDrawable()
    private val supportView = AppCompatTextView(context)

    /** The underlying input, for IME actions, filters and listeners not covered by this class. */
    public val editText: AppCompatEditText = AppCompatEditText(context)

    public var label: CharSequence?
        get() = labelView.text
        set(value) {
            labelView.text = value
            labelView.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
        }

    public var helperText: CharSequence? = null
        set(value) { field = value; updateSupportText() }

    /** Setting a non-empty value puts the field in its error state. Set `null` to clear. */
    public var errorText: CharSequence? = null
        set(value) { field = value; updateSupportText(); updateBox() }

    public var hint: CharSequence?
        get() = editText.hint
        set(value) { editText.hint = value }

    public var text: CharSequence?
        get() = editText.text
        set(value) { editText.setText(value) }

    private var focused = false

    init {
        orientation = VERTICAL

        TextViewCompat.setTextAppearance(labelView, R.style.Mds_TextAppearance_Label)
        labelView.setPadding(0, 0, 0, context.mdsPx(R.dimen.mds_space_xs))
        labelView.labelFor = View.generateViewId().also { editText.id = it }

        editText.apply {
            background = null
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(context.mdsColor(R.attr.mdsColorOnSurface))
            setHintTextColor(context.mdsColor(R.attr.mdsColorOnSurfaceMuted))
            gravity = Gravity.CENTER_VERTICAL
            minHeight = context.mdsPx(R.dimen.mds_touch_target)
            setPadding(context.dp(14), context.dp(10), context.dp(14), context.dp(10))
            setOnFocusChangeListener { _, hasFocus ->
                focused = hasFocus
                updateBox()
            }
        }
        boxBackground.cornerRadius = context.mdsPxF(R.dimen.mds_radius_md)
        box.background = boxBackground
        box.addView(editText, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        TextViewCompat.setTextAppearance(supportView, R.style.Mds_TextAppearance_Caption)
        supportView.setPadding(0, context.mdsPx(R.dimen.mds_space_xs), 0, 0)
        supportView.compoundDrawablePadding = context.mdsPx(R.dimen.mds_space_xs)
        supportView.gravity = Gravity.CENTER_VERTICAL
        ViewCompat.setAccessibilityLiveRegion(supportView, ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE)

        addView(labelView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        addView(box, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        addView(supportView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        context.withStyledAttributes(attrs, R.styleable.MdsTextField, defStyleAttr, 0) {
            label = getString(R.styleable.MdsTextField_mdsLabel)
            helperText = getString(R.styleable.MdsTextField_mdsHelperText)
            errorText = getString(R.styleable.MdsTextField_mdsErrorText)
            hint = getString(R.styleable.MdsTextField_android_hint)
            editText.inputType = getInt(R.styleable.MdsTextField_android_inputType, InputType.TYPE_CLASS_TEXT)
            editText.imeOptions = getInt(R.styleable.MdsTextField_android_imeOptions, editText.imeOptions)
            getString(R.styleable.MdsTextField_android_text)?.let { editText.setText(it) }
            isEnabled = getBoolean(R.styleable.MdsTextField_android_enabled, true)
        }
        label = label // normalises visibility when no label was provided

        ready = true
        editText.isEnabled = isEnabled
        updateBox()
        updateSupportText()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (ready) {
            editText.isEnabled = enabled
            updateBox()
        }
    }

    private fun updateBox() {
        val c = context
        val thin = c.mdsPx(R.dimen.mds_stroke_thin)
        val thick = c.mdsPx(R.dimen.mds_stroke_thick)
        val (width, strokeColor) = when {
            !isEnabled -> thin to c.mdsColor(R.attr.mdsColorOutline)
            !errorText.isNullOrEmpty() -> thick to c.mdsColor(R.attr.mdsColorDanger)
            focused -> thick to c.mdsColor(R.attr.mdsColorPrimary)
            else -> thin to c.mdsColor(R.attr.mdsColorOutlineStrong)
        }
        val fill = if (isEnabled) c.mdsColor(R.attr.mdsColorSurface) else c.mdsColor(R.attr.mdsColorSurfaceVariant)
        boxBackground.color = ColorStateList.valueOf(fill)
        boxBackground.setStroke(width, strokeColor)
    }

    private fun updateSupportText() {
        val error = errorText
        val c = context
        when {
            !error.isNullOrEmpty() -> {
                supportView.text = c.getString(R.string.mds_error_prefix, error)
                supportView.setTextColor(c.mdsColor(R.attr.mdsColorDanger))
                val icon = AppCompatResources.getDrawable(c, R.drawable.mds_ic_error)?.mutate()?.apply {
                    setTint(c.mdsColor(R.attr.mdsColorDanger))
                    val size = c.mdsPx(R.dimen.mds_icon_sm)
                    setBounds(0, 0, size, size)
                }
                supportView.setCompoundDrawablesRelative(icon, null, null, null)
                supportView.visibility = VISIBLE
            }
            !helperText.isNullOrEmpty() -> {
                supportView.text = helperText
                supportView.setTextColor(c.mdsColor(R.attr.mdsColorOnSurfaceMuted))
                supportView.setCompoundDrawablesRelative(null, null, null, null)
                supportView.visibility = VISIBLE
            }
            else -> supportView.visibility = GONE
        }
    }

    // Children share generated ids across instances, so save our own state instead of the default per-child state.
    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable {
        val state = SavedState(super.onSaveInstanceState())
        state.text = editText.text?.toString()
        state.error = errorText?.toString()
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        editText.setText(state.text)
        errorText = state.error
    }

    private class SavedState : BaseSavedState {
        var text: String? = null
        var error: String? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            text = source.readString()
            error = source.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(text)
            out.writeString(error)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }
}
