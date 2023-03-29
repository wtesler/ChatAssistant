package tesler.will.chatassistant.components.speechinput

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.components.speechinput.indicator.SpeechInputIndicator
import tesler.will.chatassistant.di.chat.chatTestModule
import tesler.will.chatassistant.di.main.mainTestModule
import tesler.will.chatassistant.di.speech.speechTestModule
import tesler.will.chatassistant.preview.Previews
import tesler.will.chatassistant.speech.ISpeechManager

@Composable
fun SpeechInputSection() {
    val speechManager = koinInject<ISpeechManager>()
    val chatManager = koinInject<IChatManager>()

    var state by remember { mutableStateOf(State.ACTIVE) }
    var chat by remember { mutableStateOf(ChatModel()) }

    val speechListener = remember {
        object : ISpeechManager.Listener {
            override fun onText(value: String?) {
                if (value != null) {
                    chat.text = value
                    chatManager.updateChat(chat)
                }
            }

            override fun onError(statusCode: Int?) {
                if (statusCode != null) {
                    if (statusCode == 7) {
                        chat.text = "Did not receive any speech."
                        speechManager.stop()
                        state = State.READY
                    } else {
                        chat.text = "Error. Status code $statusCode."
                    }
                    chatManager.updateChat(chat)
                }
            }

            override fun onSpeechFinished() {
                speechManager.stop()
                state = State.WAITING
            }
        }
    }

    val start = remember {{
        chat = ChatModel("Hi, how can I help?")
        chatManager.addChat(chat)
        speechManager.start()
    }}

    DisposableEffect(Unit) {
        speechManager.addListener(speechListener)
        start()

        onDispose {
            speechManager.stop()
            speechManager.removeListener(speechListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                State.ACTIVE -> SpeechInputIndicator()
                State.WAITING -> CircularProgressIndicator(
                    color = MaterialTheme.colors.onSurface,
                    strokeWidth = 5.dp
                )
                else -> {}
            }
        }
    }
}

enum class State {
    ACTIVE,
    WAITING,
    READY
}

@Preview
@Composable
fun SpeechInputSectionPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSection()
    }
}
