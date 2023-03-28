package tesler.will.chatassistant.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import tesler.will.chatassistant.components.speechinput.SpeechInputSection
import tesler.will.chatassistant.di.main.mainTestModule
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.ui.theme.AppTheme
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Card() {
    val speechManager = koinInject<ISpeechManager>()

    var state by remember { mutableStateOf(State.RECEIVING_INPUT) }

    val shape = RoundedCornerShape(MaterialTheme.spacing.large)

    val speechFinishedListener = remember {
        object: ISpeechManager.SpeechFinishedListener {
            override fun onSpeechFinished() {
                state = State.SENDING_INPUT
            }
        }
    }

    DisposableEffect(Unit) {
        speechManager.addFinishedListener(speechFinishedListener)
        onDispose {
            speechManager.removeFinishedListener(speechFinishedListener)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(MaterialTheme.spacing.medium)
            .border(BorderStroke(0.dp, Color.Black), shape)
            .clip(shape),
        color = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.extraLarge),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (state) {
                State.SENDING_INPUT -> SendingInput()
                State.PLAYING_RESPONSE -> PlayingResponse { state = State.RECEIVING_INPUT }
                else -> {}
            }

            Box(
                modifier = Modifier.weight(1f, false)
            ) {
                SpeechInputSection()
            }
        }
    }
}

enum class State {
    RECEIVING_INPUT,
    SENDING_INPUT,
    PLAYING_RESPONSE
}

@Preview(
    showBackground = true,
    backgroundColor = 0xF00,
    device = Devices.NEXUS_5
)
@Composable
fun CardPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(mainTestModule)
    }
    AppTheme(darkTheme = true) {
        Card()
    }
}
