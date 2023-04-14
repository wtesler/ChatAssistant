package tesler.will.chatassistant.theme

import androidx.compose.ui.graphics.Color

val sharedColors = AppColors(
    underlay = Color(0x00000000)
)

val lightColors = sharedColors.copy(
    background = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF121212),
    chatPrimary = Color(0xFFFFFFFF),
    chatSecondary = Color(0xFFF9F9F9),
    chatError = Color(0xFFFFADAD),
    iconPrimary = Color(0xFF474747),
    iconSecondary = Color(0xFFE7E7E7),
    textFieldPrimary = Color(0xFFFAFAFA),
    dropdownBackground = Color(0xFFF8F8F8),
    spacer = Color(0xFFF8F8F8),
)

val darkColors = sharedColors.copy(
    background = Color(0xFF1B1B1F),
    textPrimary = Color(0xFFEBEBEB),
    chatPrimary = Color(0xFF202125),
    chatSecondary = Color(0xFF2B2C30),
    chatError = Color(0xFF220000),
    iconPrimary = Color(0xFFEBEBEB),
    iconSecondary = Color(0xFF313236),
    textFieldPrimary = Color(0xFF313236),
    dropdownBackground = Color(0xFF17171A),
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
