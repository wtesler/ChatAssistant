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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import tesler.will.chatassistant.components.chat.ChatSection
import tesler.will.chatassistant.components.speechinput.SpeechInputSection
import tesler.will.chatassistant.di.chat.chatTestModule
import tesler.will.chatassistant.di.main.mainTestModule
import tesler.will.chatassistant.di.speech.speechTestModule
import tesler.will.chatassistant.modifiers.noRippleClickable
import tesler.will.chatassistant.preview.Previews
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Card() {
    val speechManager = koinInject<ISpeechManager>()

    var state by remember { mutableStateOf(State.RECEIVING_INPUT) }

    val shape = RoundedCornerShape(MaterialTheme.spacing.large)

    val speechListener = remember {
        object : ISpeechManager.Listener {
            override fun onSpeechFinished() {
                state = State.SENDING_INPUT
            }
        }
    }

    DisposableEffect(Unit) {
        speechManager.addListener(speechListener)
        onDispose {
            speechManager.removeListener(speechListener)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(MaterialTheme.spacing.medium)
            .border(BorderStroke(0.dp, Color.Black), shape)
            .clip(shape)
            .noRippleClickable {},
        color = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.xlarge, MaterialTheme.spacing.xxlarge),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ChatSection()

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

@Preview
@Composable
fun CardPreview() {
    Previews.Wrap(listOf(speechTestModule, chatTestModule), true) {
        Card()
    }
}
