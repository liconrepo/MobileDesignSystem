package io.github.liconrepo.mds.sample

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

/** Remembers the two appearance choices the gallery demonstrates: light/dark and brand. */
class Preferences(context: Context) {
    private val prefs = context.getSharedPreferences("gallery", Context.MODE_PRIVATE)

    var nightMode: Int
        get() = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = prefs.edit { putInt("night_mode", value) }

    var sunset: Boolean
        get() = prefs.getBoolean("sunset", false)
        set(value) = prefs.edit { putBoolean("sunset", value) }
}
