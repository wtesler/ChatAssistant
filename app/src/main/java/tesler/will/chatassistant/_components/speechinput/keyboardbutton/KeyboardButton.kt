package tesler.will.chatassistant._components.speechinput.keyboardbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun KeyboardButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val padding = 5.dp

    Box(
        modifier = modifier
            .size(MaterialTheme.spacing.icon_normal)
            .padding(padding, padding.times(2), padding, 0.dp),
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = onClick
                ),
            painter = painterResource(id = R.drawable.keyboard),
            contentDescription = "Keyboard Button",
            colorFilter = tint(MaterialTheme.colors.onSurface)
        )
    }
}

@Preview
@Composable
private fun KeyboardButtonPreview() {
    Previews.Wrap(mainTestModule, false) {
        KeyboardButton(
            Modifier
        ) {}
    }
}
