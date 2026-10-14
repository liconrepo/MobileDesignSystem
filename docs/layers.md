---
title: Layers
nav_order: 1
---

# Layers

The library is organised by the kind of pattern. Two ideas shape it:

- **Functional and perceptual patterns** from Alla Kholmatova's *Design Systems*: what a pattern does, versus how it
  looks and feels.
- **Atoms, molecules, organisms, templates and pages** from Brad Frost's
  [Atomic Design](https://bradfrost.com/blog/post/atomic-web-design/): a way to grow a UI from small parts to whole screens.

```
io.github.liconrepo.mds
  perceptual/               MdsTone, MdsMotion, theme helpers (mdsColor, mdsPx)
  functional/
    atoms/                  MdsButton, MdsBadge, MdsChip, MdsAvatar, MdsSkeleton
    molecules/              MdsTextField, MdsAlert, MdsListItem
    organisms/              MdsCard
  templates/                MdsScreenTemplate
```

| Layer | Package | What belongs here |
| --- | --- | --- |
| Perceptual | `perceptual` | Tone, motion and theme lookups. No views. |
| Atoms | `functional.atoms` | A part that does one job and stands alone. |
| Molecules | `functional.molecules` | Atoms working as one unit with a single purpose. |
| Organisms | `functional.organisms` | A distinct section of an interface, built from atoms and molecules. |
| Templates | `templates` | The layout of a whole screen with no content of its own. |
| Pages | your app | A template filled with real content. Pages are app code, not library code. |

The sample app has one tab per layer, in the same order. Pages are real Activities (`SignInPage`, `ProfilePage`)
built on `MdsScreenTemplate`.

## Where does my new component go?

1. Does it only describe look and feel, with no view? `perceptual`.
2. Does it stand alone? `functional.atoms`.
3. Is it atoms combined into one small unit? `functional.molecules`.
4. Is it a whole section of a screen? `functional.organisms`.
5. Is it a screen skeleton with no content? `templates`.
