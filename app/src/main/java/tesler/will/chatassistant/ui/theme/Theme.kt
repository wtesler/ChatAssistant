package tesler.will.chatassistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorPalette = lightColors(
    primary = OffWhite,
    primaryVariant = LightGray,
    secondary = NearWhite,
    secondaryVariant = NearWhite,
    background = Transparent,
    surface = White,
    error = LightRed,
    onPrimary = OffBlack,
    onSecondary = OffBlack,
    onBackground = OffBlack,
    onSurface = OffBlack,
    onError = DarkRed
)

private val DarkColorPalette = darkColors(
    primary = OffBlack,
    primaryVariant = Gray,
    secondary = NearBlack,
    secondaryVariant = NearBlack,
    background = Transparent,
    surface = OffBlack,
    error = DarkRed,
    onPrimary = OffWhite,
    onSecondary = OffWhite,
    onBackground = OffWhite,
    onSurface = OffWhite,
    onError = LightRed
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
