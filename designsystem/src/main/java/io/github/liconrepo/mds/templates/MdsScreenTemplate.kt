package io.github.liconrepo.mds.templates

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import io.github.liconrepo.mds.R
import io.github.liconrepo.mds.perceptual.mdsColor
import io.github.liconrepo.mds.perceptual.mdsPx

/**
 * A page-level layout with no content of its own: a header, a scrolling content region and a pinned footer.
 *
 * A template fixes the structure and spacing of a screen, and a page fills it with real content. Children
 * declared in XML go into the content region; give one `app:layout_mdsTemplateSlot="footer"` to pin it to the
 * bottom. The footer stays hidden until it has a child.
 */
public class MdsScreenTemplate @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val titleView = AppCompatTextView(context)
    private val subtitleView = AppCompatTextView(context)
    private val header = LinearLayout(context)
    private val scroll = ScrollView(context)
    private val content = LinearLayout(context)
    private val footerRegion = LinearLayout(context)
    private val divider = View(context)
    private var structured = false

    public var title: CharSequence?
        get() = titleView.text
        set(value) {
            titleView.text = value
            titleView.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
        }

    public var subtitle: CharSequence?
        get() = subtitleView.text
        set(value) {
            subtitleView.text = value
            subtitleView.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
        }

    init {
        orientation = VERTICAL
        val lg = context.mdsPx(R.dimen.mds_space_lg)
        val xl = context.mdsPx(R.dimen.mds_space_xl)

        TextViewCompat.setTextAppearance(titleView, R.style.Mds_TextAppearance_Headline)
        titleView.setTextColor(context.mdsColor(R.attr.mdsColorOnSurface))
        ViewCompat.setAccessibilityHeading(titleView, true)
        TextViewCompat.setTextAppearance(subtitleView, R.style.Mds_TextAppearance_Body)
        subtitleView.setTextColor(context.mdsColor(R.attr.mdsColorOnSurfaceMuted))

        header.orientation = VERTICAL
        header.setPadding(xl, xl, xl, lg)
        header.addView(titleView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        header.addView(
            subtitleView,
            LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply { topMargin = context.mdsPx(R.dimen.mds_space_xs) },
        )

        content.orientation = VERTICAL
        content.setPadding(xl, lg, xl, xl)
        scroll.isFillViewport = true
        scroll.clipToPadding = false
        scroll.addView(content, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        divider.setBackgroundColor(context.mdsColor(R.attr.mdsColorOutline))
        footerRegion.orientation = VERTICAL
        footerRegion.setPadding(xl, lg, xl, lg)
        footerRegion.setBackgroundColor(context.mdsColor(R.attr.mdsColorSurface))
        footerRegion.visibility = GONE

        super.addView(header, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        super.addView(scroll, LayoutParams(MATCH_PARENT, 0, 1f))
        super.addView(divider, LayoutParams(MATCH_PARENT, context.mdsPx(R.dimen.mds_stroke_thin)))
        super.addView(footerRegion, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        divider.visibility = GONE
        structured = true

        title = null
        subtitle = null
        context.withStyledAttributes(attrs, R.styleable.MdsScreenTemplate, defStyleAttr, 0) {
            title = getString(R.styleable.MdsScreenTemplate_mdsTemplateTitle)
            subtitle = getString(R.styleable.MdsScreenTemplate_mdsTemplateSubtitle)
        }
    }

    /** Adds a view to the pinned footer, for pages that build their actions in code. */
    public fun addFooterView(view: View, params: ViewGroup.LayoutParams? = null) {
        footerRegion.addView(view, params ?: LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        footerRegion.visibility = VISIBLE
        divider.visibility = VISIBLE
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (!structured) return super.addView(child, index, params)
        val slot = (params as? SlotLayoutParams)?.slot ?: Slot.Content
        // Copy into plain LinearLayout params so weight, gravity and margins survive the move.
        val plain = if (params is LinearLayout.LayoutParams) LayoutParams(params) else LayoutParams(params)
        when (slot) {
            Slot.Content -> content.addView(child, index, plain)
            Slot.Footer -> {
                footerRegion.addView(child, index, plain)
                footerRegion.visibility = VISIBLE
                divider.visibility = VISIBLE
            }
        }
    }

    internal enum class Slot { Content, Footer }

    /** Layout params that remember which region of the template a child belongs to. */
    public class SlotLayoutParams : LayoutParams {
        internal var slot: Slot = Slot.Content

        public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            context.withStyledAttributes(attrs, R.styleable.MdsScreenTemplate_Layout) {
                slot = Slot.entries[getInt(R.styleable.MdsScreenTemplate_Layout_layout_mdsTemplateSlot, 0)]
            }
        }

        public constructor(width: Int, height: Int) : super(width, height)
        public constructor(source: ViewGroup.LayoutParams) : super(source)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams = SlotLayoutParams(context, attrs)
    override fun generateDefaultLayoutParams(): LayoutParams = SlotLayoutParams(MATCH_PARENT, WRAP_CONTENT)
    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams = SlotLayoutParams(p)
    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean = p is SlotLayoutParams
}
