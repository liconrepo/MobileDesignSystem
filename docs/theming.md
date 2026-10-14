# Theming

## Light and dark

`Theme.Mds` extends `Theme.AppCompat.DayNight.NoActionBar`. Colours come from `values/` or `values-night/`, so the
system (or `AppCompatDelegate.setDefaultNightMode`) decides. Components do nothing special.

## A new brand

A brand re-points the `primary*` roles and nothing else. `ThemeOverlay.Mds.Sunset` is the whole implementation:

```xml
<style name="ThemeOverlay.Mds.Sunset" parent="">
    <item name="mdsColorPrimary">@color/mds_sunset_primary</item>
    <item name="mdsColorPrimaryPressed">@color/mds_sunset_primary_pressed</item>
    <item name="mdsColorOnPrimary">@color/mds_sunset_on_primary</item>
    <item name="mdsColorPrimaryContainer">@color/mds_sunset_primary_container</item>
    <item name="mdsColorOnPrimaryContainer">@color/mds_sunset_on_primary_container</item>
</style>
```

Define the `mds_sunset_*` colours in both `values` and `values-night`. Apply the overlay before `super.onCreate()`
(`theme.applyStyle(..., true)`), or per subtree with `android:theme="@style/ThemeOverlay..."`.

## Restyling a component globally

Every component takes its default style from a theme attribute, for example `mdsButtonStyle`. Point it at your own style
to change defaults app-wide without touching layouts:

```xml
<style name="Theme.MyApp" parent="Theme.Mds">
    <item name="mdsButtonStyle">@style/MyApp.Button</item>
</style>
```

## Switching theme at runtime

Prefer starting a fresh Activity over `recreate()` for an edge-to-edge screen (see the note in the sample's
`MainActivity.restart()`).
