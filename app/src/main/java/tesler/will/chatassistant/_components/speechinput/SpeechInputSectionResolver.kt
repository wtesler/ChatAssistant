package tesler.will.chatassistant._components.speechinput

import android.speech.SpeechRecognizer
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
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

    val view = LocalView.current

    var viewModel by remember { mutableStateOf(SpeechInputSectionViewModel()) }
    var chat by remember { mutableStateOf(ChatModel()) }
    val scope = rememberCoroutineScope()

    val INITIAL_PROMPT = "Hi, how can I help?"

    fun setState(state: State) {
        viewModel = viewModel.copy(state = state)
    }

    fun setText(text: String) {
        chat.text = text
        viewModel = viewModel.copy(text = text)
    }

    fun setIsSpeaking(isSpeaking: Boolean) {
        viewModel = viewModel.copy(isSpeaking = isSpeaking)
    }

    fun submitChat() {
        if (chat.text.isBlank()) {
            return
        }
        setState(State.LOADING)
        val chatToSubmit = chat.copy(state = ChatModel.State.CREATED)
        chatManager.clearErrorChats()
        speechOutputManager.stop()
        chatManager.submitChat(chatToSubmit, scope)
    }

    val speechListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onListeningStarted() {
                setState(State.ACTIVE)

                val defaultMessage = if (chatManager.numChats() == 0) INITIAL_PROMPT else ""
                chat = ChatModel(defaultMessage)
                setText(defaultMessage)
            }

            override fun onText(value: String?) {
                setText(value ?: "")
            }

            override fun onError(statusCode: Int?) {
                if (statusCode == null) {
                    return
                }

                if (speechInputManager.isListening()) {
                    speechInputManager.stop()
                }

                chat = ChatModel()
                chat.state = ChatModel.State.ERROR
                when (statusCode) {
                    SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                        chat.text = "Did not receive any speech."
                        chatManager.addChat(chat)
                        setState(State.READY)
                        setText("")
                    }
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                        chat.text = "Enable Audio Permissions."
                        chatManager.addChat(chat)
                        setState(State.READY)
                        setText("")
                    }
                    -1 -> {
                        chat.text = "Speech Recognition Unavailable."
                        chatManager.addChat(chat)
                        setState(State.READY)
                        setText("")
                    }
                    SpeechRecognizer.ERROR_CLIENT -> {
                        val message = "Error. Status code $statusCode."
                        if (viewModel.state == State.TEXT_INPUT) {
                            Log.w("Speech Input", message)
                        } else {
                            chat.text = message
                            chatManager.addChat(chat)
                            setState(State.READY)
                            setText("")
                        }
                    }
                    else -> {
                        chat.text = "Error. Status code $statusCode."
                        chatManager.addChat(chat)
                        setState(State.READY)
                        setText("")
                    }
                }
            }

            override fun onSpeechFinished() {
                if (viewModel.state != State.LOADING && viewModel.state != State.TEXT_INPUT) {
                    submitChat()
                }
            }
        }
    }

    val speechOutputListener = remember {
        object : ISpeechOutputManager.Listener {
            override fun onSpeechInProgress() {
                setIsSpeaking(true)
            }

            override fun onSpeechEnded() {
                setIsSpeaking(false)
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

    fun startSpeechInput() {
        speechOutputManager.stop()
        speechInputManager.start()
    }

    fun onKeyboardClicked() {
        chatManager.clearErrorChats()
        if (viewModel.state == State.TEXT_INPUT) {
            setText("")
            setState(State.READY)
        } else {
            val storedText = chat.text
            speechInputManager.stop()
            chat = ChatModel()
            setText(if (storedText == INITIAL_PROMPT) "" else storedText)
            setState(State.TEXT_INPUT)
        }
    }

    fun onStopClicked() {
        chatManager.cancelSubmit()
        speechOutputManager.stop()
    }

    fun onTextChanged(string: String) {
        setText(string)
    }

    val insetListener = remember {
        object :
            WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
            override fun onProgress(
                insets: WindowInsets,
                runningAnimations: MutableList<WindowInsetsAnimation>
            ): WindowInsets {
                return insets
            }

            override fun onEnd(animation: WindowInsetsAnimation) {
                super.onEnd(animation)

                val isShowingSoftKeyboard =
                    view.rootWindowInsets.isVisible(WindowInsets.Type.ime())
                if (!isShowingSoftKeyboard && viewModel.state == State.TEXT_INPUT) {
                    setText("")
                    setState(State.READY)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        chatManager.addListener(chatListener)
        speechInputManager.addListener(speechListener)
        speechOutputManager.addListener(speechOutputListener)
        view.setWindowInsetsAnimationCallback(insetListener)
        startSpeechInput()

        onDispose {
            chatManager.removeListener(chatListener)
            speechInputManager.removeListener(speechListener)
            speechOutputManager.removeListener(speechOutputListener)
            view.setWindowInsetsAnimationCallback(null)
        }
    }

    SpeechInputSection(
        viewModel,
        ::startSpeechInput,
        ::submitChat,
        ::onKeyboardClicked,
        ::onStopClicked,
        ::onTextChanged
    )
}

@Preview
@Composable
private fun SpeechInputSectionResolverPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSectionResolver()
    }
}
