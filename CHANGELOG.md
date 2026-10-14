# Changelog

## Unreleased

- **Breaking:** components moved into packages by layer: `perceptual`, `functional.atoms`,
  `functional.molecules`, `functional.organisms`. Update layout class names and imports.
- `MdsScreenTemplate` in a new `templates` package. Children go into its content region; mark one with
  `app:layout_mdsTemplateSlot="footer"` to pin it.
- Sample pages (`SignInPage`, `ProfilePage`) built on the template.
- Fixed `MdsScreenTemplate` collapsing its content region to zero height (the weighted scroll area lost its weight).
- Gallery sample is now tabbed, one tab per layer, with new tone, motion, chip, skeleton and organism examples.

## 0.1.0

- Token architecture: primitive, semantic, theme attributes; light and dark.
- Brand overlay example (`ThemeOverlay.Mds.Sunset`).
- Components: `MdsButton`, `MdsTextField`, `MdsCard`, `MdsBadge`, `MdsChip`, `MdsAvatar`, `MdsAlert`,
  `MdsListItem`, `MdsSkeleton`, and a themed switch.
- Gallery sample app, docs, CI.
