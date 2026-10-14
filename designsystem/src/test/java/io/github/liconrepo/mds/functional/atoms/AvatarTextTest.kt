package io.github.liconrepo.mds.functional.atoms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class AvatarTextTest {
    @Test fun `two words use first and last initial`() =
        assertEquals("AL", AvatarText.initials("ada king lovelace"))

    @Test fun `single word uses one initial`() = assertEquals("G", AvatarText.initials("grace"))

    @Test fun `blank input falls back to question mark`() {
        assertEquals("?", AvatarText.initials("   "))
        assertEquals("?", AvatarText.initials(null))
    }

    @Test fun `extra whitespace is ignored`() = assertEquals("LT", AvatarText.initials("  linus   torvalds "))

    @Test fun `tone is stable and case-insensitive`() {
        assertSame(AvatarText.tone("Grace Hopper"), AvatarText.tone("  grace hopper "))
    }
}
