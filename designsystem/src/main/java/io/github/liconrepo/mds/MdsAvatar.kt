package io.github.liconrepo.mds

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

/** A person or entity, shown as an image or, failing that, as coloured initials. */
public class MdsAvatar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mdsAvatarStyle,
) : View(context, attrs, defStyleAttr) {

    public enum class Size(internal val dp: Int) { Small(32), Medium(40), Large(56), XLarge(80) }

    public var avatarSize: Size = Size.Medium
        set(value) {
            if (field != value) { field = value; requestLayout(); invalidate() }
        }

    /** Used for the initials, the background tone, and the accessibility description. */
    public var name: String? = null
        set(value) {
            field = value
            contentDescription = value
            invalidate()
        }

    public var image: Drawable? = null
        set(value) { field = value; invalidate() }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
    }
    private val clip = Path()

    init {
        context.withStyledAttributes(attrs, R.styleable.MdsAvatar, defStyleAttr, 0) {
            avatarSize = Size.entries[getInt(R.styleable.MdsAvatar_mdsAvatarSize, 1)]
            name = getString(R.styleable.MdsAvatar_mdsAvatarName)
        }
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val px = context.dp(avatarSize.dp)
        setMeasuredDimension(px, px)
    }

    override fun onDraw(canvas: Canvas) {
        val radius = width / 2f
        val picture = image
        if (picture != null) {
            clip.reset()
            clip.addCircle(radius, radius, radius, Path.Direction.CW)
            val save = canvas.save()
            canvas.clipPath(clip)
            picture.setBounds(0, 0, width, height)
            picture.draw(canvas)
            canvas.restoreToCount(save)
            return
        }
        val tone = AvatarText.tone(name)
        fillPaint.color = tone.container(context)
        canvas.drawCircle(radius, radius, radius, fillPaint)

        textPaint.color = tone.onContainer(context)
        textPaint.textSize = width * 0.38f
        val baseline = radius - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(AvatarText.initials(name), radius, baseline, textPaint)
    }
}

internal object AvatarText {
    private val tones = listOf(MdsTone.Brand, MdsTone.Info, MdsTone.Success, MdsTone.Warning, MdsTone.Danger)

    /** First letter of the first and last word, upper-cased. Blank input yields "?". */
    fun initials(name: String?): String {
        val words = name.orEmpty().trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (words.isEmpty()) return "?"
        val picked = if (words.size == 1) listOf(words.first()) else listOf(words.first(), words.last())
        return picked.joinToString("") { word ->
            String(Character.toChars(word.codePointAt(0))).uppercase()
        }
    }

    /** Stable per-name tone, so the same person is always the same colour. */
    fun tone(name: String?): MdsTone {
        val key = name.orEmpty().trim().lowercase()
        return tones[Math.floorMod(key.hashCode(), tones.size)]
    }
}
