package com.campuscrave.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ObsidianPrimary,
    onPrimary = SurfaceCard,
    primaryContainer = ObsidianPrimary,
    onPrimaryContainer = SurfaceCard,
    secondary = EmeraldSecondary,
    onSecondary = SurfaceCard,
    secondaryContainer = EmeraldContainer,
    onSecondaryContainer = EmeraldDark,
    tertiary = CoralRed,
    onTertiary = SurfaceCard,
    background = CanvasBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainerLow,
    onSurfaceVariant = TextSecondary,
    outline = OutlineBorder,
    outlineVariant = OutlineVariant,
    error = ErrorRed,
    onError = SurfaceCard,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer
)

val CampusCraveShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(999.dp)
)

@Composable
fun CampusCraveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // The Stitch CampusCrave design system is focused on high-contrast crisp Nordic Light aesthetic
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = CanvasBackground.toArgb()
                window.navigationBarColor = CanvasBackground.toArgb()
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = CampusCraveShapes
    ) {
        androidx.compose.material3.ProvideTextStyle(
            value = androidx.compose.ui.text.TextStyle(
                fontFamily = PlusJakartaSans
            )
        ) {
            content()
        }
    }
}

