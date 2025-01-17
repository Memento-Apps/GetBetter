package com.velkonost.getbetter.core.compose.theme

import android.os.Build
import android.os.Build.VERSION_CODES.Q
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ApplicationTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    val window = (view.context as ComponentActivity).window
    val windowsInsetsController = WindowCompat.getInsetsController(window, view)
    val darkTheme = isSystemInDarkTheme()
    val transparentNav = true
    val transparentStatus = true
    val colorTransparent = Color.Transparent.toArgb()
    val colorBlackTranslucent = Color.Black.copy(alpha = 0.2F).toArgb()

    WindowCompat.setDecorFitsSystemWindows(window, false)

    window.statusBarColor = if (transparentStatus) colorTransparent else colorBlackTranslucent
    window.navigationBarColor = if (transparentNav) colorTransparent else colorBlackTranslucent

    windowsInsetsController.isAppearanceLightStatusBars = !darkTheme
    windowsInsetsController.isAppearanceLightNavigationBars = !darkTheme

    if (Build.VERSION.SDK_INT >= Q) window.isNavigationBarContrastEnforced = false

    MaterialTheme(
        colorScheme = LightColorPalette,
        typography = AppTypography,
        shapes = AppShapes
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null,
            content = content
        )
    }
}
