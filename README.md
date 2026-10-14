# Mobile Design System

A small, opinionated, **View-based** design system for Android, written in Kotlin. **No Jetpack Compose.**
It is meant to be read, copied and adapted: a reference for how a company can structure tokens, themes and
components so that a brand change, a dark mode and an accessibility fix are each a one-place change.

<p align="center">
  <img src="docs/images/light-1.png" width="23%" alt="Light theme" />
  <img src="docs/images/dark-1.png" width="23%" alt="Dark theme" />
  <img src="docs/images/sunset-1.png" width="23%" alt="Sunset brand" />
  <img src="docs/images/light-2.png" width="23%" alt="Components" />
</p>

## Why this exists

Most design-system write-ups show finished UI and hide the decisions. This repo keeps the decisions visible:

- **Three tiers of tokens**: primitive values, semantic roles, component defaults. Components only read roles.
- **One theme, many brands**: the `Sunset` brand is a 5-line theme overlay. Try it in the sample app.
- **Light and dark from the same tokens**: no per-component dark code.
- **Accessibility as a default**: 48dp touch targets, contrast-checked pairs, state never conveyed by colour alone,
  live-region announcements, reduced-motion support.
- **Small public API**: `explicitApi()` is on, so every public declaration is a deliberate decision.

## What is inside

| Module | What it is |
| --- | --- |
| [`designsystem`](designsystem) | The library: tokens (`res/values`), themes, and components (`MdsButton`, `MdsTextField`, ...). |
| [`sample`](sample) | A gallery app that uses the library exactly the way a consumer would. |
| [`docs`](docs) | Principles, token reference, theming guide, component guidelines, contributor checklist. |

### Components

| Component | Purpose |
| --- | --- |
| `MdsButton` | Five variants (primary, secondary, outline, ghost, destructive), three sizes, loading state |
| `MdsTextField` | Always-visible label, helper text, error text with icon, saved state |
| `MdsCard` | Flat, outlined or elevated container with a rounded ripple |
| `MdsBadge` | Status label in six tones |
| `MdsChip` | Toggleable choice with a check mark when selected |
| `MdsAvatar` | Image or stable-colour initials, four sizes |
| `MdsAlert` | Inline message, tone icon, optional dismiss, polite live region |
| `MdsListItem` | Leading icon, title, subtitle, chevron |
| `MdsSkeleton` | Loading placeholder with shimmer that respects "remove animations" |
| Switch (style) | `Mds.Widget.Switch` applied automatically by the theme |

## Using it

Add the dependency (see [Publishing](#publishing)), then make your app theme extend `Theme.Mds`:

```xml
<style name="Theme.MyApp" parent="Theme.Mds" />
```

Use the components in layouts:

```xml
<io.github.liconrepo.mds.MdsButton
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Continue"
    app:mdsButtonVariant="primary" />

<io.github.liconrepo.mds.MdsTextField
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:mdsLabel="Email"
    app:mdsHelperText="We only use this to send receipts."
    android:inputType="textEmailAddress" />
```

Or in code:

```kotlin
button.variant = MdsButton.Variant.Destructive
button.isLoading = true
field.errorText = "Enter a valid email."   // null clears it
val brand = context.mdsColor(R.attr.mdsColorPrimary)
```

Rebrand with an overlay (see `ThemeOverlay.Mds.Sunset`):

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    theme.applyStyle(R.style.ThemeOverlay_Mds_Sunset, true)
    super.onCreate(savedInstanceState)
}
```

## Architecture in one picture

```
 primitive   mds_palette_ocean_600 = #4040D8        raw values, no meaning
     |
 semantic    mds_color_primary  -> ocean_600        values/  (light)
                                -> ocean_400        values-night/  (dark)
     |
 theme attr  ?attr/mdsColorPrimary                  what components read
     |
 component   MdsButton.refresh()                    never sees a raw colour
```

Read [docs/tokens.md](docs/tokens.md) and [docs/theming.md](docs/theming.md) for the full story.

## Build and run

```bash
./gradlew :sample:installDebug      # run the gallery on a device or emulator
./gradlew :designsystem:test        # unit tests
./gradlew :designsystem:lint        # lint
```

Requires JDK 17 and the Android SDK (compileSdk 36, minSdk 24).

## Publishing

Version `0.1.0` is configured for Maven publishing (`io.github.liconrepo:mds`).

```bash
./gradlew :designsystem:publishReleasePublicationToMavenLocal
```

then in a consumer project:

```kotlin
repositories { mavenLocal() }
dependencies { implementation("io.github.liconrepo:mds:0.1.0") }
```

To distribute publicly, push a version tag and use [JitPack](https://jitpack.io)
(`jitpack.yml` is included), or add a Maven Central repository to the publishing block.

## Credits and inspiration

The structure borrows *ideas* (never code or assets) from public design systems and writing, including
Material Design, IBM Carbon, Shopify Polaris, GitHub Primer, Atlassian Design System, Microsoft Fluent and
Airbnb's public design-language writing, and from Alla Kholmatova's book *Design Systems*
(Smashing Magazine, 2017), notably its split between functional and perceptual patterns, its emphasis on
shared vocabulary and its principles-first approach. Colours, icons and code here are original.

This project is independent and is not affiliated with, or endorsed by, any of those organisations
or by any employer of its contributors.

## License

MIT. See [LICENSE](LICENSE).
