package tesler.will.chatassistant._components.speechinput.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun SpeechSubmitLoading(onStopClicked: () -> Unit) {
    val SIZE = 65.dp

    Box(modifier = Modifier.size(SIZE)) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(SIZE.times(.9f)),
            color = AppTheme.colors.iconSecondary, strokeWidth = 5.dp
        )

        Box(
            modifier = Modifier
                .size(SIZE.times(.25f))
                .background(AppTheme.colors.iconPrimary)
                .align(Alignment.Center)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = onStopClicked
                )
        )
    }
}

@Preview
@Composable
private fun SpeechSubmitButtonPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechSubmitLoading {}
    }
}
