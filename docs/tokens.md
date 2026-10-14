# Tokens

Tokens are named design decisions. There are three tiers, and a value may only be referenced from the tier directly above it.

## Tier 1: primitive

`res/values/mds_palette.xml`. Raw ramps (`mds_palette_neutral_*`, `mds_palette_ocean_*`, status ramps).
These have no meaning and must not be used in components or layouts.

## Tier 2: semantic

`res/values/mds_colors.xml` and `res/values-night/mds_colors.xml`. The same names exist in both files; only the
values differ. Names follow `mds_color_<role>`, with an `on_` prefix for content that sits on a role:

| Role | Used for |
| --- | --- |
| `background`, `surface`, `surface_variant` | Page, cards, quiet fills |
| `on_surface`, `on_surface_variant`, `on_surface_muted`, `on_surface_disabled` | Text and icons at four emphasis levels |
| `outline`, `outline_strong` | Dividers and card borders / input borders |
| `primary`, `primary_pressed`, `on_primary` | The brand action |
| `primary_container`, `on_primary_container` | Soft brand surfaces |
| `danger`, `success`, `warning`, `info` (+ `on_`, `_container`, `on_*_container`) | Status |

Contrast: text roles are chosen to reach at least 4.5:1 against the surface they are used on, and borders of
inputs at least 3:1, in both modes. Re-check when you change a ramp.

## Tier 3: theme attributes

`res/values/mds_attrs.xml` + `Theme.Mds`. Each semantic colour is exposed as `?attr/mdsColor<Role>`.
Components resolve them with `Context.mdsColor(R.attr.mdsColorPrimary)`.

## Non-colour tokens

`res/values/mds_dimens.xml`: spacing (`mds_space_*`, 4dp grid), radius (`mds_radius_*`), elevation, stroke, control sizes.
`res/values/mds_type.xml`: `Mds.TextAppearance.*` (Display, Headline, Title, TitleSmall, Body, BodySmall, Label, Caption, Overline).

## Adding a token

1. Do you really need one? Prefer an existing role.
2. Add the primitive (if new), then the semantic in **both** `values` and `values-night`.
3. Declare the attr, map it in `Theme.Mds`, and document it here.
