package io.github.liconrepo.mds

import android.content.Context
import android.provider.Settings

/** Motion policy in one place, so every animated component honours the user's "remove animations" setting. */
public object MdsMotion {
    public fun isReduced(context: Context): Boolean =
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
}
