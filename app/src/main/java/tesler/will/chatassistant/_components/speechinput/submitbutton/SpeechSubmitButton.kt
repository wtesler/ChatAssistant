package tesler.will.chatassistant._components.speechinput.submitbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
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
fun SpeechSubmitButton(onClick: () -> Unit) {
    Image(
        modifier = Modifier
            .height(25.dp)
            .padding(0.dp, 0.dp, 10.dp, 0.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = onClick
            ),
        painter = painterResource(id = R.drawable.submit),
        contentDescription = "Submit Button",
        colorFilter = tint(MaterialTheme.colors.onSurface)
    )
}

@Preview
@Composable
private fun SpeechSubmitButtonPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechSubmitButton { }
    }
}
