# 2.0.0

Big release. The mod is now multiloader across three Minecraft versions and the UI got a full polish pass.

## UI overhaul

- **Close / back button on the top-right of every screen.** Opens with a ❌ close when you came from the sidebar/inventory; switches to a ← back arrow when you came from another leaderboard screen, so you can drill into vanilla stats and come back without ESC-closing everything.
- **No more Accept/Cancel buttons at the bottom.** They never did anything useful here; gone.
- **Smarter window sizing:**
  - Leaderboard detail screen measures the text of every row before opening and sizes itself so nothing overflows or gets clipped.
  - Floor of 150×180 for all screens — won't shrink to pancake size when there are only a few entries.
  - Caps at 90% of the game window — won't push past screen edges on tiny resolutions.
  - Grows naturally with content up to that cap, then scrolls.