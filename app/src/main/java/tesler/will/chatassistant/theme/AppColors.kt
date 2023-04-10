package tesler.will.chatassistant.theme

import androidx.compose.ui.graphics.Color

val sharedColors = AppColors(
    underlay = Color(0x00000000)
)

val lightColors = sharedColors.copy(
    background = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF121212),
    chatPrimary = Color(0xFFEBEBEB),
    chatSecondary = Color(0xFFF0F0F0),
    chatError = Color(0xFFFFE3E3),
    iconPrimary = Color(0xFF414141),
    iconSecondary = Color(0xFFF0F0F0),
    textFieldPrimary = Color(0xFFF0F0F0),
    dropdownBackground = Color(0xFFF8F8F8),
    spacer = Color(0xFFF8F8F8),
)

val darkColors = sharedColors.copy(
    background = Color(0xFF202125),
    textPrimary = Color(0xFFEBEBEB),
    chatPrimary = Color(0xFF202125),
    chatSecondary = Color(0xFF313236),
    chatError = Color(0xFF220000),
    iconPrimary = Color(0xFFEBEBEB),
    iconSecondary = Color(0xFF313236),
    textFieldPrimary = Color(0xFF313236),
    dropdownBackground = Color(0xFF191A1D),
    spacer = Color(0xFF0A0A0A),
)

data class AppColors(
    val underlay: Color = Color.Red,
    val background: Color = Color.Red,
    val textPrimary: Color = Color.Red,
    val chatPrimary: Color = Color.Red,
    val chatSecondary: Color = Color.Red,
    val chatError: Color = Color.Red,
    val iconPrimary: Color = Color.Red,
    val iconSecondary: Color = Color.Red,
    val textFieldPrimary: Color = Color.Red,
    val dropdownBackground: Color = Color.Red,
    val spacer: Color = Color.Red,
)
