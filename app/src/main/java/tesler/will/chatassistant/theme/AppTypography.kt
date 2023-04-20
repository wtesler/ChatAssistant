package tesler.will.chatassistant.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import tesler.will.chatassistant.R

val typography = AppTypography(
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.mona_sans)),
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        lineHeight = 30.sp,
        letterSpacing = TextUnit(.03f, TextUnitType.Em)
    ),
    option1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.mona_sans)),
        fontWeight = FontWeight.W200,
        fontSize = 15.sp,
    ),
)

fun themedTypography(appColors: AppColors): AppTypography {
    return typography.copy(
        body1 = typography.body1.plus(TextStyle(color = appColors.textPrimary)),
        option1 = typography.option1.plus(TextStyle(color = appColors.textPrimary))
    )
}

data class AppTypography(
    val body1: TextStyle,
    val option1: TextStyle
)
