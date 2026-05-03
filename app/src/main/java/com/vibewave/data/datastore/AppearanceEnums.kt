package com.vibewave.data.datastore

/** Preset color palettes shown in the Profile → Appearance section. */
enum class ThemePalette(
    val displayName: String,
    val emoji: String,
) {
    OBSIDIAN("Obsidian", "🖤"),
    AMOLED("AMOLED", "⬛"),
    MIDNIGHT("Midnight", "🌌"),
    EMERALD("Emerald", "🌿"),
    SUNSET("Sunset", "🌅"),
    ROSE("Rose Gold", "🌸"),
    CYBER("Cyber", "⚡"),
    OCEAN("Ocean", "🌊"),
    WHITE("White", "🤍"),
}

/** Accent colors — independent of palette, user picks one. */
enum class AccentColor(val displayName: String, val argb: Long) {
    GREEN("Spotify Green", 0xFF1DB954),
    ORANGE("Deezer Orange", 0xFFEF5466),
    CYAN("Electric Cyan", 0xFF00E5FF),
    PURPLE("Lavender", 0xFFAB47BC),
    PINK("Hot Pink", 0xFFFF6B9D),
    YELLOW("Solar Yellow", 0xFFFFD166),
    RED("Crimson", 0xFFE63946),
    BLUE("Deep Blue", 0xFF3A86FF),
}

/** Font choice — matches the two fonts requested. */
enum class AppFont(val displayName: String, val family: String) {
    TT_HOVES("TT Hoves Pro", "tt_hoves_pro"),
    INTER_TIGHT("Inter Tight", "inter_tight"),
}

/** Player screen style variants. */
enum class PlayerStyle(val displayName: String) {
    CLASSIC("Classic"),        // big square cover, centered
    VINYL("Vinyl"),            // rotating disc
    MINIMAL("Minimal"),        // text-forward, tiny cover
}
