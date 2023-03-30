package tesler.will.chatassistant._components.speechinput.startbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun SpeechInputStartButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        elevation = null,
        contentPadding = PaddingValues(0.dp, MaterialTheme.spacing.small)
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.microphone),
            contentDescription = "Speech Start Button",
            colorFilter = tint(MaterialTheme.colors.onSurface),
        )
    }
}

@Preview
@Composable
fun SpeechInputStartButtonPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputStartButton { }
    }
}
