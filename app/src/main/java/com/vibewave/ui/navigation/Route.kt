package com.vibewave.ui.navigation

/**
 * Top-level destinations. Using sealed route objects gives us type-safety
 * without the weight of a multi-module nav-graph solution.
 */
sealed class Route(val path: String) {
    data object Welcome : Route("welcome")
    data object Login : Route("login")
    data object Register : Route("register")
    data object Home : Route("home")
    data object Search : Route("search")
    data object Library : Route("library")
    data object Profile : Route("profile")
    data object Player : Route("player")
}

/** Items shown in the bottom bar. */
data class BottomDest(
    val route: Route,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)
