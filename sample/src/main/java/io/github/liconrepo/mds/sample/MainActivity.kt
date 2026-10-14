package io.github.liconrepo.mds.sample

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import io.github.liconrepo.mds.R as Mds
import io.github.liconrepo.mds.mdsColor
import io.github.liconrepo.mds.mdsPx
import io.github.liconrepo.mds.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = Preferences(this)
        // A brand is just a theme overlay on top of Theme.Mds. Apply it before inflating.
        if (prefs.sunset) theme.applyStyle(Mds.style.ThemeOverlay_Mds_Sunset, true)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyInsets()
        styleHero()
        bindAppearance()
        bindButtons()
        bindFields()
        bindLoading()
        buildFoundations()
    }

    // The manifest declares uiMode as handled, so a system dark-mode change lands here instead of recreating.
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        restart()
    }

    /**
     * Relaunches as a fresh Activity instead of calling recreate(): on API 35 a recreated edge-to-edge
     * window kept the system bars' insets as padding on the emulator, a fresh launch does not.
     */
    private fun restart() {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(0, 0)
    }

    private fun applyInsets() {
        val heroTop = binding.hero.paddingTop
        val contentBottom = binding.content.paddingBottom
        var statusBarHeight = 0
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            statusBarHeight = bars.top
            binding.root.post {
                binding.hero.updatePadding(top = heroTop + bars.top)
                binding.content.updatePadding(bottom = contentBottom + bars.bottom)
            }
            insets
        }
        // Status bar icons must contrast with whatever is underneath: the brand hero first, the page after.
        val update = {
            val overHero = binding.scroll.scrollY < binding.hero.height - statusBarHeight
            val under = mdsColor(if (overHero) Mds.attr.mdsColorPrimary else Mds.attr.mdsColorBackground)
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                ColorUtils.calculateLuminance(under) > 0.4
        }
        binding.scroll.setOnScrollChangeListener { _, _, _, _, _ -> update() }
        binding.scroll.post { update() }
    }

    private fun styleHero() {
        val onPrimary = mdsColor(Mds.attr.mdsColorOnPrimary)
        binding.hero.background = HeroDrawable(
            start = mdsColor(Mds.attr.mdsColorPrimary),
            end = mdsColor(Mds.attr.mdsColorPrimaryPressed),
            accent = (onPrimary and 0x00FFFFFF) or (0x1A shl 24),
            cornerRadius = mdsPx(Mds.dimen.mds_radius_xl).toFloat(),
        )
    }

    private fun bindAppearance() {
        val isDark = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES
        // These controls always reflect current state, so never let view-state restore overwrite them.
        binding.switchDark.isSaveEnabled = false
        binding.chipOcean.isSaveEnabled = false
        binding.chipSunset.isSaveEnabled = false

        binding.switchDark.isChecked = isDark
        binding.switchDark.setOnCheckedChangeListener { button, checked ->
            if (!button.isPressed) return@setOnCheckedChangeListener // ignore programmatic changes
            prefs.nightMode = if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(prefs.nightMode)
            restart()
        }

        binding.chipOcean.isChecked = !prefs.sunset
        binding.chipSunset.isChecked = prefs.sunset
        binding.chipOcean.setOnClickListener { chooseBrand(sunset = false) }
        binding.chipSunset.setOnClickListener { chooseBrand(sunset = true) }
    }

    private fun chooseBrand(sunset: Boolean) {
        if (prefs.sunset == sunset) {
            // Chips are checkboxes; keep the group behaving like a radio group.
            binding.chipOcean.isChecked = !sunset
            binding.chipSunset.isChecked = sunset
            return
        }
        prefs.sunset = sunset
        restart()
    }

    private fun bindButtons() {
        binding.btnLoading.setOnClickListener {
            binding.btnLoading.isLoading = true
            binding.btnLoading.text = getString(R.string.saving)
            binding.btnLoading.postDelayed({
                binding.btnLoading.isLoading = false
                binding.btnLoading.text = getString(R.string.saved_once_more)
            }, 1800)
        }
    }

    private fun bindFields() {
        binding.btnValidate.setOnClickListener {
            val email = binding.fieldEmail.text?.toString().orEmpty().trim()
            binding.fieldEmail.errorText = when {
                email.isEmpty() -> getString(R.string.error_email_empty)
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> getString(R.string.error_email_invalid)
                else -> null
            }
        }
    }

    private fun bindLoading() {
        binding.alertDanger.onDismiss = { /* nothing to do: the alert hides itself */ }
        binding.btnToggleSkeleton.setOnClickListener {
            val showSkeleton = binding.skeletonGroup.visibility != View.VISIBLE
            binding.skeletonGroup.visibility = if (showSkeleton) View.VISIBLE else View.GONE
            binding.loadedGroup.visibility = if (showSkeleton) View.GONE else View.VISIBLE
            binding.btnToggleSkeleton.text = getString(if (showSkeleton) R.string.load_content else R.string.show_skeleton)
        }
    }

    /** Foundations are rendered from the live theme, so they are documentation that can never go stale. */
    private fun buildFoundations() {
        val roles = listOf(
            "Primary" to (Mds.attr.mdsColorPrimary to Mds.attr.mdsColorOnPrimary),
            "Primary container" to (Mds.attr.mdsColorPrimaryContainer to Mds.attr.mdsColorOnPrimaryContainer),
            "Surface" to (Mds.attr.mdsColorSurface to Mds.attr.mdsColorOnSurface),
            "Surface variant" to (Mds.attr.mdsColorSurfaceVariant to Mds.attr.mdsColorOnSurfaceVariant),
            "Success" to (Mds.attr.mdsColorSuccess to Mds.attr.mdsColorOnSuccess),
            "Warning" to (Mds.attr.mdsColorWarning to Mds.attr.mdsColorOnWarning),
            "Danger" to (Mds.attr.mdsColorDanger to Mds.attr.mdsColorOnDanger),
            "Info" to (Mds.attr.mdsColorInfo to Mds.attr.mdsColorOnInfo),
        )
        val chipHeight = mdsPx(Mds.dimen.mds_touch_target)
        val gap = mdsPx(Mds.dimen.mds_space_sm)
        roles.forEach { (name, pair) ->
            val swatch = TextView(this).apply {
                text = name
                gravity = Gravity.CENTER_VERTICAL
                TextViewCompat.setTextAppearance(this, Mds.style.Mds_TextAppearance_Label)
                setTextColor(mdsColor(pair.second))
                setPadding(mdsPx(Mds.dimen.mds_space_lg), 0, 0, 0)
                background = swatchBackground(mdsColor(pair.first), mdsColor(Mds.attr.mdsColorOutline), mdsPx(Mds.dimen.mds_radius_md).toFloat())
            }
            binding.swatches.addView(
                swatch,
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, chipHeight).apply { bottomMargin = gap },
            )
        }

        listOf(
            "Display" to Mds.style.Mds_TextAppearance_Display,
            "Headline" to Mds.style.Mds_TextAppearance_Headline,
            "Title" to Mds.style.Mds_TextAppearance_Title,
            "Body" to Mds.style.Mds_TextAppearance_Body,
            "Label" to Mds.style.Mds_TextAppearance_Label,
            "Caption" to Mds.style.Mds_TextAppearance_Caption,
        ).forEach { (label, style) ->
            val row = TextView(this).apply {
                text = label
                TextViewCompat.setTextAppearance(this, style)
            }
            binding.typeScale.addView(
                row,
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    .apply { bottomMargin = gap },
            )
        }

        listOf(
            "xs" to Mds.dimen.mds_space_xs, "sm" to Mds.dimen.mds_space_sm, "md" to Mds.dimen.mds_space_md,
            "lg" to Mds.dimen.mds_space_lg, "xl" to Mds.dimen.mds_space_xl, "xxl" to Mds.dimen.mds_space_xxl,
        ).forEach { (name, res) ->
            val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
            val bar = View(this).apply {
                background = swatchBackground(
                    mdsColor(Mds.attr.mdsColorPrimary), mdsColor(Mds.attr.mdsColorPrimary), mdsPx(Mds.dimen.mds_radius_xs).toFloat(),
                )
            }
            row.addView(bar, LinearLayout.LayoutParams(mdsPx(res), mdsPx(Mds.dimen.mds_space_lg)))
            row.addView(
                TextView(this).apply {
                    text = "$name  ·  ${resources.getDimension(res) / resources.displayMetrics.density}dp"
                    TextViewCompat.setTextAppearance(this, Mds.style.Mds_TextAppearance_Caption)
                },
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    .apply { marginStart = gap },
            )
            binding.spacing.addView(
                row,
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    .apply { bottomMargin = gap },
            )
        }
    }
}

private fun swatchBackground(fill: Int, stroke: Int, radius: Float) = android.graphics.drawable.GradientDrawable().apply {
    cornerRadius = radius
    setColor(fill)
    setStroke(1, stroke)
}
