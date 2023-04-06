package tesler.will.chatassistant._components.speechinput

import android.speech.SpeechRecognizer
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager

@Composable
fun SpeechInputSectionResolver() {
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val chatManager = koinInject<IChatManager>()

    var viewModel by remember { mutableStateOf(SpeechInputSectionViewModel(State.ACTIVE, "")) }
    var chat by remember { mutableStateOf(ChatModel()) }
    val scope = rememberCoroutineScope()

    fun setState(state: State) {
        viewModel = viewModel.copy(state = state)
    }

    fun setText(text: String) {
        viewModel = viewModel.copy(text = text)
    }

    fun submitChat() {
        val chatToSubmit = chat.copy(state = ChatModel.State.CREATED)
        chatManager.submitChat(chatToSubmit, scope)
    }

    val speechListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onText(value: String?) {
                if (value == null) {
                    return
                }
                chat.text = value
                setText(chat.text)
            }

            override fun onError(statusCode: Int?) {
                if (statusCode == null) {
                    return
                }
                if (statusCode == SpeechRecognizer.ERROR_NO_MATCH || statusCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                    chat.text = "Did not receive any speech."
                    chat.state = ChatModel.State.ERROR
                    speechInputManager.stop()
                    setState(State.READY)
                    setText("")
                } else {
                    chat.text = "Error. Status code $statusCode."
                }
                setText(chat.text)
                chatManager.addChat(chat)
            }

            override fun onSpeechFinished() {
                if (viewModel.state != State.LOADING) {
                    setState(State.LOADING)
                    submitChat()
                }
            }
        }
    }

    val chatListener = remember {
        object : IChatManager.Listener {
            override fun onChatSubmitResponseStarted() {
                setText("")
            }

            override fun onChatSubmitResponse(isSuccess: Boolean, value: String?) {
                setState(State.READY)
                setText("")
            }
        }
    }

    val start = {
        setState(State.ACTIVE)

        val numChats = chatManager.getChats().size
        val defaultMessage = if (numChats == 0) "Hi, how can I help?" else ""

        setText(defaultMessage)
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

    SpeechInputSection(viewModel, onStartClicked)
}

@Preview
@Composable
private fun SpeechInputSectionResolverPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSectionResolver()
    }
}