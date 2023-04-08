package tesler.will.chatassistant._components.speechinput

import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
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

    var viewModel by remember { mutableStateOf(SpeechInputSectionViewModel()) }
    var chat by remember { mutableStateOf(ChatModel()) }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    fun setState(state: State) {
        viewModel = viewModel.copy(state = state)
    }

    fun setText(text: String) {
        chat.text = text
        viewModel = viewModel.copy(text = text)
    }

    fun setNumChats(num: Int) {
        viewModel = viewModel.copy(numChats = num)
    }

    fun submitChat() {
        if (chat.text.isBlank()) {
            return
        }
        setState(State.LOADING)
        val chatToSubmit = chat.copy(state = ChatModel.State.CREATED)
        chatManager.clearErrorChats()
        chatManager.submitChat(chatToSubmit, scope)
    }

    val speechListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onText(value: String?) {
                if (value == null) {
                    return
                }
                setText(value)
            }

            override fun onError(statusCode: Int?) {
                if (statusCode == null) {
                    return
                }

                speechInputManager.stop()

                chat.state = ChatModel.State.ERROR
                when (statusCode) {
                    SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                        chat.text = "Did not receive any speech."
                        chatManager.addChat(chat)
                        setState(State.READY)
                    }
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                        chat.text = "Enable Audio Permissions."
                        chatManager.addChat(chat)
                        setState(State.READY)
                    }
                    SpeechRecognizer.ERROR_CLIENT -> {
                        val message = "Error. Status code $statusCode."
                        if (viewModel.state == State.TEXT_INPUT) {
                            Log.w("Speech Input", message)
                        } else {
                            chat.text = message
                            chatManager.addChat(chat)
                            setState(State.READY)
                        }
                    }
                    else -> {
                        chat.text = "Error. Status code $statusCode."
                        chatManager.addChat(chat)
                        setState(State.READY)
                    }
                }

                setText("")
            }

            override fun onSpeechFinished() {
                if (viewModel.state != State.LOADING) {
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

            override fun onNumChatsChanged(num: Int) {
                setNumChats(num)
            }
        }
    }

    val start = {
        setState(State.ACTIVE)

        val defaultMessage = if (chatManager.numChats() == 0) "Hi, how can I help?" else ""

        chat = ChatModel(defaultMessage)
        setText(defaultMessage)

        speechInputManager.start()
    }

    fun onStartClicked() {
        speechOutputManager.stop()
        chatManager.clearErrorChats()
        start()
    }

    fun onKeyboardClicked() {
        chatManager.clearErrorChats()
        setText("")
        if (viewModel.state == State.TEXT_INPUT) {
            setState(State.READY)
        } else {
            speechInputManager.stop()
            chat = ChatModel("")
            setState(State.TEXT_INPUT)
            scope.launch {
                // Don't love this concept
                awaitFrame()
                focusRequester.requestFocus()
            }
        }
    }

    fun onStopClicked() {
        chatManager.cancelSubmit()
    }

    fun onTextChanged(string: String) {
        setText(string)
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

    SpeechInputSection(
        viewModel,
        ::onStartClicked,
        ::submitChat,
        ::onKeyboardClicked,
        ::onStopClicked,
        ::onTextChanged,
        focusRequester
    )
}

@Preview
@Composable
private fun SpeechInputSectionResolverPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSectionResolver()
    }
}
