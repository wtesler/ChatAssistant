package tesler.will.chatassistant.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val default: Dp = 0.dp,
    val extraSmall: Dp = 2.5.dp,
    val small: Dp = 5.dp,
    val medium: Dp = 10.dp,
    val large: Dp = 15.dp,
    val xlarge: Dp = 25.dp,
    val xxlarge: Dp = 35.dp,
    val icon_normal: Dp = 35.dp
)

val appDimensions = AppDimensions()
