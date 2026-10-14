---
title: Components
nav_order: 5
---

# Component guidelines

## Button (`MdsButton`)

- **Primary** once per region. **Secondary** for a supporting action. **Outline** for neutral choices.
  **Ghost** for low-emphasis actions. **Destructive** only for irreversible actions, and confirm them.
- Labels are verbs ("Save", "Delete account"), sentence case, no full stops.
- While `isLoading` is true taps are ignored and a spinner appears. Keep the label so context is not lost.
- Small buttons are 36dp tall but keep a 48dp touch target.

## Text field (`MdsTextField`)

- Always set `mdsLabel`. A hint is an example, not a label.
- Prefer helper text for rules ("8 characters or more") and error text for what went wrong and how to fix it.
- Errors replace helper text, are announced politely, and show an icon.
- The field saves and restores its own state, so several fields can share one screen safely.

## Card (`MdsCard`)

- Outlined is the default. Use elevated sparingly (one level of lift per screen). Flat groups quiet content.
- Make the whole card clickable, not parts of it, and give it a content description if it has no text.

## Badge, chip, alert

- **Badge**: read-only status. **Chip**: user choice, toggles. **Alert**: needs the user's attention inline.
- Tone is semantic: do not use `Danger` for decoration.

## Avatar, list item, skeleton

- Avatar initials use a stable tone per name; supply `name` so accessibility gets a description.
- Skeletons mirror the final layout's size to avoid jumps; stop showing them as soon as content arrives.

## Screen template (`MdsScreenTemplate`)

- Use one per screen. It gives every page the same header, spacing and pinned actions.
- XML children land in the scrolling content region. Add `app:layout_mdsTemplateSlot="footer"` to pin a button
  to the bottom; the footer and its divider stay hidden until there is one.
- The primary action goes in the footer so it is always reachable. Keep it to one primary button.
- The template does not handle system bar insets. Pad the root in the host Activity (see `PageActivity` in the
  sample).

## Accessibility checklist for every component

- Touch target >= 48dp.
- Text contrast >= 4.5:1, non-text >= 3:1 in light and dark.
- State is not colour-only.
- TalkBack: sensible label, role and state; dynamic changes use live regions.
- Respects font scale and "remove animations".
