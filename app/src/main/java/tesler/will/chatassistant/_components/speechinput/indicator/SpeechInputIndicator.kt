package tesler.will.chatassistant._components.speechinput.indicator

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.modules.speech.speechTestModule
import tesler.will.chatassistant._components.preview.Previews
import kotlin.math.roundToInt

@Composable
fun SpeechInputIndicator() {
    val SPACING = 10.dp
    val DURATION_MS = 1500
    val WOBBLE_DISTANCE = 2f

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(32.dp)
            .offset(0.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpeechInputIndicatorDot(
            DURATION_MS,
            WOBBLE_DISTANCE,
            0,
            1.3f,
            1.2f
        )
        Spacer(Modifier.size(SPACING))
        SpeechInputIndicatorDot(
            DURATION_MS,
            WOBBLE_DISTANCE,
            (DURATION_MS / 4f).roundToInt(),
            .9f,
            1.5f
        )
        Spacer(Modifier.size(SPACING))
        SpeechInputIndicatorDot(
            DURATION_MS,
            WOBBLE_DISTANCE,
            ((2 * DURATION_MS) / 4f).roundToInt(),
            1.45f,
            1.35f
        )
        Spacer(Modifier.size(SPACING))
        SpeechInputIndicatorDot(
            DURATION_MS,
            WOBBLE_DISTANCE,
            ((3 * DURATION_MS) / 4f).roundToInt(),
            1.15f,
            1.6f
        )
    }
}

@Preview
@Composable
fun SpeechInputIndicatorPreview() {
    Previews.Wrap(speechTestModule, true) {
        SpeechInputIndicator()
    }
}
