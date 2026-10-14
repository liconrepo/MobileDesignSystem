package io.github.liconrepo.mds.perceptual

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import io.github.liconrepo.mds.R

/**
 * Communicates meaning, never decoration. Every tone resolves to a container/on-container pair
 * (for subtle surfaces such as badges and alerts) and a solid/on-solid pair (for emphasis).
 *
 * Keep the order in sync with the `mdsTone` enum in attrs.
 */
public enum class MdsTone(
    @AttrRes private val containerAttr: Int,
    @AttrRes private val onContainerAttr: Int,
    @AttrRes private val solidAttr: Int,
    @AttrRes private val onSolidAttr: Int,
) {
    Neutral(
        R.attr.mdsColorSurfaceVariant, R.attr.mdsColorOnSurfaceVariant,
        R.attr.mdsColorOnSurfaceMuted, R.attr.mdsColorSurface,
    ),
    Brand(
        R.attr.mdsColorPrimaryContainer, R.attr.mdsColorOnPrimaryContainer,
        R.attr.mdsColorPrimary, R.attr.mdsColorOnPrimary,
    ),
    Info(
        R.attr.mdsColorInfoContainer, R.attr.mdsColorOnInfoContainer,
        R.attr.mdsColorInfo, R.attr.mdsColorOnInfo,
    ),
    Success(
        R.attr.mdsColorSuccessContainer, R.attr.mdsColorOnSuccessContainer,
        R.attr.mdsColorSuccess, R.attr.mdsColorOnSuccess,
    ),
    Warning(
        R.attr.mdsColorWarningContainer, R.attr.mdsColorOnWarningContainer,
        R.attr.mdsColorWarning, R.attr.mdsColorOnWarning,
    ),
    Danger(
        R.attr.mdsColorDangerContainer, R.attr.mdsColorOnDangerContainer,
        R.attr.mdsColorDanger, R.attr.mdsColorOnDanger,
    );

    @ColorInt public fun container(context: Context): Int = context.mdsColor(containerAttr)
    @ColorInt public fun onContainer(context: Context): Int = context.mdsColor(onContainerAttr)
    @ColorInt public fun solid(context: Context): Int = context.mdsColor(solidAttr)
    @ColorInt public fun onSolid(context: Context): Int = context.mdsColor(onSolidAttr)

    internal companion object {
        fun fromAttr(index: Int): MdsTone = entries.getOrElse(index) { Neutral }
    }
}
