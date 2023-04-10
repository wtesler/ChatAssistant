package tesler.will.chatassistant.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val typography = AppTypography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
)

fun themedTypography(appColors: AppColors): AppTypography {
    return typography.copy(
        body1 = typography.body1.plus(TextStyle(color = appColors.textPrimary))
    )
}

data class AppTypography(
    val body1: TextStyle
)
