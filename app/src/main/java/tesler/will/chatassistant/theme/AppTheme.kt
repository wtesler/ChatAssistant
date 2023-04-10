package tesler.will.chatassistant.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val type: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val dimens: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}

internal val LocalColors = staticCompositionLocalOf { lightColors }
internal val LocalTypography = staticCompositionLocalOf { typography }
internal val LocalDimensions = staticCompositionLocalOf { appDimensions }

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: AppColors
    if (isDarkTheme) {
        colors = darkColors
    } else {
        colors = lightColors
    }

    val typography = themedTypography(colors)

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalDimensions provides appDimensions
        ) {
        content()
    }
}