---
title: Principles
nav_order: 2
---

# Design principles

Principles are only useful if they settle arguments. Each one below says what to do when two options collide.
(The approach follows the idea that a design language needs written, testable principles, not slogans.)

1. **Meaning before decoration.** A colour, icon or animation must carry information. If removing it loses nothing,
   remove it.
2. **Never colour alone.** Errors have an icon and a text prefix. Selected chips have a check mark. Loading has a
   spinner *and* a state description.
3. **Roles, not values.** Code asks for "primary" or "danger", never for `#4040D8`. When in doubt, add a role
   rather than a one-off colour.
4. **Reachable by default.** Controls are at least 48dp to touch, even when they look smaller (see `MdsButton.Size.Small`).
5. **Calm motion.** Motion confirms a change or reserves space; it never entertains. Respect the system's
   "remove animations" setting (`MdsMotion.isReduced`).
6. **One primary action per region.** Importance is expressed with `MdsButton.Variant`, not with size or position tricks.
7. **Small surface, deliberate growth.** A new component must justify itself with two real uses.

## Functional and perceptual patterns

It helps to name two kinds of pattern:

- **Functional**: what a pattern does. `MdsButton` triggers an action; `MdsTextField` collects text.
- **Perceptual**: how it looks and feels. Corner radius, type scale, tone colours, spacing rhythm.

In this repo functional behaviour lives in Kotlin classes under `functional/` (split into `atoms`, `molecules` and
`organisms`) and perceptual choices live in resources (tokens) and the `perceptual` package (`MdsTone`, `MdsMotion`). Keeping
them apart means a rebrand edits `res/values`, and a behaviour fix edits one class.

## Shared vocabulary

Use the same words in design files, code and conversation:

| Word | Means |
| --- | --- |
| Tone | Neutral, brand, info, success, warning, danger (`MdsTone`) |
| Variant | A visual-importance level of one component (`MdsButton.Variant`) |
| Size | Density of one component (`Small`, `Medium`, `Large`) |
| Container / On-container | A soft surface and the content colour that sits on it |
| Solid / On-solid | An emphatic fill and the content colour that sits on it |
