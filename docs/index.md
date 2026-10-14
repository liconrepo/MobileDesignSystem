---
layout: home
title: Overview
nav_order: 0
---

<section class="hero">
  <h1>A design system you can read, copy and adapt</h1>
  <p>A small, opinionated, View-based design system for Android, written in Kotlin, with no Jetpack Compose dependency.
  Tokens, themes and components, with the decisions kept visible.</p>
  <a class="btn primary" href="{{ '/layers.html' | relative_url }}">Explore the layers</a>
  <a class="btn outline" href="{{ site.repository_url }}">View on GitHub</a>
  <div class="shots">
    <img src="{{ '/images/light-1.png' | relative_url }}" alt="Gallery app in the light theme" loading="lazy">
    <img src="{{ '/images/dark-1.png' | relative_url }}" alt="Gallery app in the dark theme" loading="lazy">
    <img src="{{ '/images/sunset-1.png' | relative_url }}" alt="Gallery app with the Sunset brand" loading="lazy">
    <img src="{{ '/images/light-2.png' | relative_url }}" alt="Components in the gallery app" loading="lazy">
  </div>
</section>

<h2>Why it exists</h2>
<div class="grid">
  <div class="card"><h3>Three tiers of tokens</h3><p>Primitive values, semantic roles, component defaults. Components only read roles.</p></div>
  <div class="card"><h3>One theme, many brands</h3><p>The Sunset brand is a small theme overlay. Switch it live in the sample app.</p></div>
  <div class="card"><h3>Light and dark, same tokens</h3><p>No per-component dark mode code.</p></div>
  <div class="card"><h3>Accessible by default</h3><p>48dp touch targets, contrast-checked pairs, never colour alone, reduced-motion support.</p></div>
  <div class="card"><h3>Small public API</h3><p><code>explicitApi()</code> is on, so every public declaration is deliberate.</p></div>
</div>

<h2>Organised in layers</h2>
<ol class="layers">
  <li><div><strong>Perceptual</strong><br>How it looks and feels: tone, motion, theme helpers.</div></li>
  <li><div><strong>Atoms</strong><br>Button, badge, chip, avatar, skeleton.</div></li>
  <li><div><strong>Molecules</strong><br>Text field, alert, list item.</div></li>
  <li><div><strong>Organisms</strong><br>Card, and the compositions built on it.</div></li>
  <li><div><strong>Templates</strong><br>Screen layout with the content taken out.</div></li>
  <li><div><strong>Pages</strong><br>A template filled with real content.</div></li>
</ol>

<h2>Get started</h2>

```xml
<style name="Theme.MyApp" parent="Theme.Mds" />
```

```xml
<io.github.liconrepo.mds.functional.atoms.MdsButton
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Continue" />
```

Then read the [principles]({{ '/principles.html' | relative_url }}), the [token reference]({{ '/tokens.html' | relative_url }})
and the [theming guide]({{ '/theming.html' | relative_url }}).
