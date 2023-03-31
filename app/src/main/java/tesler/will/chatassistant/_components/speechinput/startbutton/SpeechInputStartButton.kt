package tesler.will.chatassistant._components.speechinput.startbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule

@Composable
fun SpeechInputStartButton(onClick: () -> Unit) {
    Image(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = onClick
            )
            .padding(0.dp, 25.dp),
        painter = painterResource(id = R.drawable.microphone),
        contentDescription = "Speech Start Button",
        colorFilter = tint(MaterialTheme.colors.onSurface)
    )
}

@Preview
@Composable
private fun SpeechInputStartButtonPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputStartButton { }
    }
}
