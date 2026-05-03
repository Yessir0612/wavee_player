# 🎵 VibeWave

A modern Android music player built with **Jetpack Compose** and the 2026 Android stack.

- Plays 30-second previews from the **Deezer public API** (no key required)
- Fully customizable: 8 theme palettes × 8 accent colors × 3 player styles
- Play history with live top/recent stats
- Blur mini-player (Haze), dynamic colors from album art, smooth animations
- Background playback with media-style notification

---

## 📸 Screens

1. **Home** — greeting, recently played carousel, trending list
2. **Search** — pill search bar, genre chips, debounced Deezer search
3. **Player** — rotating album art, seek bar, shuffle/prev/play/next/repeat
4. **Profile** — avatar, top tracks, customization pickers, full history list

---

## 🔧 Setup

### 1. Open in Android Studio

Open **Android Studio Ladybug** (2024.2.1) or newer. Choose **File → Open** and pick the `VibeWave/` folder.

### 2. Gradle sync

Click **"Sync Now"** when prompted. First sync downloads ~500 MB of dependencies and takes 3–8 minutes.

If you see:
- `"gradle wrapper missing"` — run `gradle wrapper --gradle-version 8.9` in a terminal at the project root, **or** just let Android Studio regenerate it when you open the project.

### 3. (Optional) Add fonts

The app is set up for **TT Hoves Pro Trial** and **Inter Tight**, but ships with the system font as a working default.

To enable the custom fonts:

1. Download and put in `app/src/main/res/font/`:
   ```
   tt_hoves_pro_regular.ttf
   tt_hoves_pro_medium.ttf
   tt_hoves_pro_demibold.ttf
   tt_hoves_pro_bold.ttf
   inter_tight_regular.ttf
   inter_tight_medium.ttf
   inter_tight_semibold.ttf
   inter_tight_bold.ttf
   ```
   - TT Hoves Pro Trial: https://typetype.org
   - Inter Tight: https://fonts.google.com/specimen/Inter+Tight
2. Open `app/src/main/java/com/vibewave/ui/theme/Typography.kt`
3. Uncomment the `ttHovesPro` and `interTight` blocks
4. In `toFamily()`, return `ttHovesPro` / `interTight` instead of `FontFamily.Default`

### 4. Run

Plug in a device or start an emulator (API 26+), hit ▶️.

---

## 🏗 Architecture

```
com.vibewave/
├── MainActivity.kt             ← Compose entry, Hilt activity
├── VibeWaveApp.kt              ← @HiltAndroidApp Application
├── AppRootViewModel.kt         ← exposes AppearanceSettings at root
│
├── core/
│   ├── di/                     ← NetworkModule, DataModule (Hilt)
│   └── result/                 ← Outcome<T> — Loading/Success/Error
│
├── data/
│   ├── api/DeezerApi.kt        ← Ktor client for deezer.com/*
│   ├── model/                  ← DTOs + mapper → domain.Track
│   ├── db/VibeWaveDatabase.kt  ← Room: play history
│   ├── datastore/              ← appearance enums
│   └── repository/             ← MusicRepo, HistoryRepo, SettingsRepo
│
├── domain/
│   └── model/Track.kt          ← clean UI model, no API leakage
│
├── player/
│   ├── PlaybackService.kt      ← Media3 MediaSessionService (background)
│   └── PlayerController.kt     ← StateFlow facade over MediaController
│
└── ui/
    ├── VibeWaveApp.kt          ← root Compose: NavHost + mini-player + bottom bar
    ├── components/             ← MiniPlayer, VibeBottomBar, TrackRow, ShimmerBox
    ├── navigation/Route.kt     ← sealed destinations
    ├── theme/                  ← VibeWaveTheme, PaletteColors, Typography
    └── screens/
        ├── home/               ← HomeScreen + HomeViewModel
        ├── search/             ← SearchScreen + SearchViewModel
        ├── player/             ← PlayerScreen + PlayerViewModel
        └── profile/            ← ProfileScreen + ProfileViewModel
```

### Key patterns

- **Single-activity + Compose Navigation** — one `MainActivity`, `NavHost` switches between screens.
- **Hilt everywhere** — every repository, ViewModel, DAO, API client is injected. Nothing is constructed by hand.
- **Flow-first state** — every piece of state is exposed as `StateFlow` and collected with `collectAsStateWithLifecycle()`.
- **Theme is reactive** — changing palette/accent in Profile updates the whole app immediately through `AppRootViewModel.settings`.
- **Media3 owns the player** — the service keeps it alive when the app backgrounds; UI talks to it via a `MediaController` wrapped in `PlayerController`.

---

## 🎨 Customization

| Knob             | Where                 | Values |
|------------------|-----------------------|--------|
| Theme palette    | Profile → Appearance  | 8 presets (Obsidian, AMOLED, Midnight, Emerald, Sunset, Rose, Cyber, Ocean) |
| Accent color     | Profile → Appearance  | 8 swatches (Green, Orange, Cyan, Purple, Pink, Yellow, Red, Blue) |
| Font             | Profile → Appearance  | TT Hoves Pro / Inter Tight (once .ttf files added) |
| Player style     | Profile → Appearance  | Classic / Vinyl / Minimal (Classic is implemented; others are stubs) |
| Dynamic from art | Profile → Toggle      | Accent adapts to currently-playing cover (TODO hook-up in PlayerScreen) |

Adding a **new palette**: open `ui/theme/PaletteColors.kt`, add an entry to the `ThemePalette` enum in `data/datastore/AppearanceEnums.kt` and a matching `Scheme` in the `when` block. That's it — it appears in the picker automatically.

---

## 📦 Dependencies

| Library | Version | What it's for |
|---------|---------|---------------|
| Kotlin | 2.0.21 | Compose Compiler Plugin |
| AGP | 8.7.1 | Android build tooling |
| Compose BOM | 2024.10.01 | Aligns all Compose artifacts |
| Material 3 | (from BOM) | Design system |
| Hilt | 2.52 | DI |
| Ktor | 2.3.12 | Deezer HTTP client |
| kotlinx.serialization | 1.7.3 | JSON |
| Media3 | 1.4.1 | ExoPlayer + session notification |
| Coil | 2.7.0 | Image loading |
| Room | 2.6.1 | Play history |
| DataStore | 1.1.1 | Appearance prefs |
| Haze | 0.9.0-beta04 | Compose blur for mini-player |
| Palette | 1.0.0 | Dynamic colors from album art |

---

## 🐛 Known TODOs

- Player styles `Vinyl` and `Minimal` currently fall back to `Classic`. The picker saves the choice but `PlayerScreen` doesn't branch on it yet.
- Dynamic-color-from-album-art toggle saves but isn't wired through — you'd read it in `Root()` and use Palette to extract a color from the current track's bitmap.
- Favorites button is visual-only (no persistence).
- No tests yet — add with `ViewModelScope.runTest { … }` and `Turbine` for flows.
- If you bump Compose BOM past `2024.10`, check that `AnimatedContent.togetherWith` is still in `androidx.compose.animation.togetherWith`.

---

## 🎧 How Deezer works

No auth needed. All endpoints are public GETs:

```
GET https://api.deezer.com/search?q=arcane      # tracks matching query
GET https://api.deezer.com/chart                # global top 100
```

Each track's `preview` field is a direct URL to a 30-second MP3. That's the constraint — full tracks require a licensed partner account.

---

## License

Do whatever you want. Educational purposes, personal use, ship it — up to you.
