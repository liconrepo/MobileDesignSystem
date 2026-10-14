---
title: Adding a component
nav_order: 6
---

# Adding a component

1. **Justify it.** Two real call sites, and no existing component can do it with a new variant.
2. **Name it** `Mds<Thing>`, in `designsystem/src/main/java/io/github/liconrepo/mds/functional/<layer>`, where `<layer>` is `atoms` (stands alone),
   `molecules` (atoms working as one unit) or `organisms` (a distinct section built from both). Whole-screen
   layouts with no content go in `templates`. Tokens and other look-and-feel code go in `perceptual`. Pages are
   app code, not library code.
3. **Attributes**: add a `declare-styleable`, a `mds<Thing>Style` theme attribute and a `Mds.Widget.<Thing>` default style.
4. **Constructor**: `@JvmOverloads constructor(context, attrs, defStyleAttr = R.attr.mds<Thing>Style)`.
5. **Colours only through roles** (`context.mdsColor(R.attr...)`). No hex values, no `R.color.mds_palette_*`.
6. **Dimensions only through tokens** (`context.mdsPx(R.dimen...)`).
7. **Set background before padding** when using an `InsetDrawable` (it reports its own padding).
8. **Accessibility**: touch target, content description, live region where content changes.
9. **State**: if the view owns children with ids, override `dispatchSaveInstanceState` as `MdsTextField` does.
10. **Public API**: `explicitApi()` is on. Keep helpers `internal`.
11. **Sample + docs**: add it to the gallery and to `docs/components.md`, then a line in `CHANGELOG.md`.
12. **Tests**: put pure logic (such as `AvatarText`) in testable functions and cover them.
