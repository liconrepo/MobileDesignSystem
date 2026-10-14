package io.github.liconrepo.mds.sample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(Preferences(this).nightMode)
    }
}
