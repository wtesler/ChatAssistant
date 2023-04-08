package tesler.will.chatassistant._components.speechinput.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule

@Composable
fun SpeechSubmitLoading(onStopClicked: () -> Unit) {
    Box(modifier = Modifier.size(65.dp)) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp),
            color = MaterialTheme.colors.onSurface, strokeWidth = 5.dp
        )

        Box(
            modifier = Modifier
                .size(20.dp)
                .background(MaterialTheme.colors.onSurface)
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
