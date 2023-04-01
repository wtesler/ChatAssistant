package tesler.will.chatassistant._components.speechinput

import android.speech.SpeechRecognizer.ERROR_NO_MATCH
import android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.chat.ElevationShadow
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.speechinput.indicator.SpeechInputIndicator
import tesler.will.chatassistant._components.speechinput.startbutton.SpeechInputStartButton
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.ui.theme.spacing

enum class State {
    ACTIVE,
    WAITING,
    READY
}

@Composable
fun SpeechInputSection(initialState: State = State.ACTIVE) {
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val chatManager = koinInject<IChatManager>()

    var state by remember { mutableStateOf(initialState) }
    var text by remember { mutableStateOf("") }
    var chat by remember { mutableStateOf(ChatModel()) }
    val scope = rememberCoroutineScope()

    val speechListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onText(value: String?) {
                if (value == null) {
                    return
                }
                chat.text = value
                text = chat.text
            }

            override fun onError(statusCode: Int?) {
                if (statusCode == null) {
                    return
                }
                if (statusCode == ERROR_NO_MATCH || statusCode == ERROR_SPEECH_TIMEOUT) {
                    chat.text = "Did not receive any speech."
                    chat.state = ChatModel.State.ERROR
                    speechInputManager.stop()
                    state = State.READY
                } else {
                    chat.text = "Error. Status code $statusCode."
                }
                text = chat.text
                chatManager.addChat(chat)
            }

            override fun onSpeechFinished(value: String?) {
                if (state != State.WAITING) {
                    state = State.WAITING
                    if (value != null) {
                        chat.text = value
                    }
                    val chatToSubmit = chat.copy(state = ChatModel.State.CREATED)
                    chatManager.submitChat(chatToSubmit, scope)
                }
            }
        }
    }

    val chatListener = remember {
        object : IChatManager.Listener {
            override fun onChatSubmitResponseStarted() {
                text = ""
            }

            override fun onChatSubmitResponse(isSuccess: Boolean, value: String?) {
                state = State.READY
            }
        }
    }

    val start = {
        state = State.ACTIVE

        val numChats = chatManager.getChats().size
        val defaultMessage = if (numChats == 0) "Hi, how can I help?" else ""

        text = defaultMessage
        chat = ChatModel(defaultMessage)

        speechInputManager.start()
    }

    val onStartClicked = {
        speechOutputManager.stop()
        chatManager.clearErrorChats()
        start()
    }

    DisposableEffect(Unit) {
        chatManager.addListener(chatListener)
        speechInputManager.addListener(speechListener)
        speechInputManager.init()
        start()

        onDispose {
            chatManager.removeListener(chatListener)
            speechInputManager.destroy()
            speechInputManager.removeListener(speechListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state == State.ACTIVE || state == State.WAITING) {
            if (text.isNotBlank()) {
                val hPadding = MaterialTheme.spacing.large
                val topPadding = MaterialTheme.spacing.xxlarge
                val bottomPadding = MaterialTheme.spacing.small

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(hPadding, topPadding, hPadding, bottomPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth(),
                        text = text,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }

        if (state == State.READY || (state == State.ACTIVE && text.isEmpty())) {
            ElevationShadow()
        }

        Box(
            modifier = Modifier
                .height(90.dp),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                State.ACTIVE -> SpeechInputIndicator()
                State.WAITING -> CircularProgressIndicator(
                    color = MaterialTheme.colors.onSurface,
                    strokeWidth = 5.dp
                )
                State.READY -> SpeechInputStartButton(onStartClicked)
            }
        }
    }
}

@Preview
@Composable
private fun SpeechInputSectionPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSection(State.ACTIVE)
    }
}
