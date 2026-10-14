package io.github.liconrepo.mds.sample.pages

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import io.github.liconrepo.mds.R as Mds
import io.github.liconrepo.mds.sample.Preferences

/**
 * Base for the sample's pages. A page is a template filled with real content, so all a page needs from its
 * host is the chosen brand and room for the system bars.
 */
abstract class PageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Preferences(this).sunset) theme.applyStyle(Mds.style.ThemeOverlay_Mds_Sunset, true)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        view ?: return
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(left = bars.left, top = bars.top, right = bars.right, bottom = bars.bottom)
            insets
        }
    }
}
