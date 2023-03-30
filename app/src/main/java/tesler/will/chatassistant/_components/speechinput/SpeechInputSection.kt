package tesler.will.chatassistant._components.speechinput

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.speechinput.indicator.SpeechInputIndicator
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.preview.Previews
import tesler.will.chatassistant.server.ApiService
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager

@Composable
fun SpeechInputSection() {
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val chatManager = koinInject<IChatManager>()
    val apiService = koinInject<ApiService>()

    var state by remember { mutableStateOf(State.ACTIVE) }
    var chat by remember { mutableStateOf(ChatModel()) }
    val composableScope = rememberCoroutineScope()

    val speechListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onText(value: String?) {
                if (value == null) {
                    return
                }
                chat.text = value
                chatManager.updateChat(chat)
            }

            override fun onError(statusCode: Int?) {
                if (statusCode == null) {
                    return
                }
                if (statusCode == 7) {
                    chat.text = "Did not receive any speech."
                    speechInputManager.stop()
                    state = State.READY
                } else {
                    chat.text = "Error. Status code $statusCode."
                }
                chatManager.updateChat(chat)
            }

            override fun onSpeechFinished(value: String?) {
                speechInputManager.stop()

                if (value == null) {
                    state = State.READY
                    return
                }

                state = State.WAITING

                composableScope.launch {
                    val response = apiService.updateChat(ChatUpdateRequest(value))

                    chat.state = ChatModel.State.CREATED
                    chatManager.updateChat(chat)

                    val responseChat = ChatModel(response.message, ChatModel.State.CREATED)
                    chatManager.addChat(responseChat)

                    chat = ChatModel()
                    state = State.READY

                    speechOutputManager.speak(response.message)
                }
            }
        }
    }

    val start = remember {
        {
            val numChats = chatManager.getChats().size
            val defaultMessage = if (numChats == 0) "Hi, how can I help?" else ""

            chat = ChatModel(defaultMessage)
            chatManager.addChat(chat)
            speechInputManager.start()
        }
    }

    DisposableEffect(Unit) {
        speechInputManager.addListener(speechListener)
        start()
        speechOutputManager.start()

        onDispose {
            speechInputManager.stop()
            speechInputManager.removeListener(speechListener)
            speechOutputManager.stop()
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
                State.READY -> Text(
                    text = "Show Microphone",
                    color = MaterialTheme.colors.onSurface
                )
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
