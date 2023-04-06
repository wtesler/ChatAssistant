package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.shadow.OverflowShadow
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.store.ISettingsService

@Composable
fun ChatSection() {
    val chatManager = koinInject<IChatManager>()
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val settingsService = koinInject<ISettingsService>()

    val chats = remember { mutableStateListOf<ChatModel>() }
    var height by remember { mutableStateOf(0) }
    var hasManuallyInterfered by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val isTopOverflowing by remember(listState) {
        derivedStateOf {
            listState.canScrollBackward
        }
    }

    val isBottomOverflowing by remember(listState) {
        derivedStateOf {
            listState.canScrollForward
        }
    }

    val scrollToEnd = {
        coroutineScope.launch {
            awaitFrame()
            awaitFrame()
            if (!hasManuallyInterfered) {
                listState.scrollBy(height.toFloat() + 1)
            }
        }
    }

    val scrollToLastItem = { additionalScrollY: Float ->
        coroutineScope.launch {
            awaitFrame()
            awaitFrame()
            if (!hasManuallyInterfered) {
                listState.scrollToItem(chats.lastIndex, additionalScrollY.toInt())
            }
        }
    }

    val preventAutoScroll = {
        if (!hasManuallyInterfered) {
            hasManuallyInterfered = true
            coroutineScope.launch {
                listState.stopScroll(MutatePriority.UserInput)
            }
        }
    }

    val chatListener = remember {
        object : IChatManager.Listener {
            override fun onChatAdded(chatModel: ChatModel) {
                chats.add(chatModel)
            }

            override fun onChatUpdated(chatModel: ChatModel) {
                for (i in 0 until chats.size) {
                    if (chats[i].id == chatModel.id) {
                        chats[i] = chatModel
                        break
                    }
                }
            }

            override fun onChatRemoved(chatId: String) {
                chats.removeIf { x -> x.id == chatId }
            }

            override fun onChatsCleared() {
                chats.clear()
            }

            override fun onErrorChatsCleared() {
                chats.removeIf { chat -> chat.state == ChatModel.State.ERROR }
            }

            override fun onChatSubmitResponseStarted() {
                speechOutputManager.reset()
                hasManuallyInterfered = false
            }

            override fun onChatSubmitResponsePartial(value: String) {
                speechOutputManager.queueSpeech(value)
                scrollToLastItem(0f)
            }

            override fun onChatSubmitResponse(isSuccess: Boolean, value: String?) {
                if (isSuccess && value != null) {
                    speechOutputManager.flushSpeech()
                }
            }
        }
    }

    val speechInputListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onListeningStarted() {
                hasManuallyInterfered = false
            }

            override fun onText(value: String?) {
                // scrollToEnd()
            }

            override fun onError(statusCode: Int?) {
                scrollToEnd()
            }
        }
    }

    DisposableEffect(Unit) {
        chatManager.addListener(chatListener)
        speechInputManager.addListener(speechInputListener)

        coroutineScope.launch {
            settingsService.observeSettings().collect { settings ->
                if (!speechOutputManager.isInit()) {
                    val voice = settings.voice
                    val speed = settings.speed
                    speechOutputManager.init(voice, speed)
                }
            }
        }

        for (chat in chatManager.getChats()) {
            chats.add(chat)
        }

        onDispose {
            chatManager.removeListener(chatListener)
            speechInputManager.removeListener(speechInputListener)
            speechOutputManager.destroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onGloballyPositioned { height = it.size.height }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .pointerInput("tap_input") {
                    detectTapGestures(
                        onPress = { preventAutoScroll() },
                        onTap = { preventAutoScroll() },
                        onLongPress = { preventAutoScroll() },
                        onDoubleTap = { preventAutoScroll() }
                    )
                }
                .pointerInput("drag_input") {
                    detectDragGestures(
                        onDrag = { _, _ -> preventAutoScroll() },
                        onDragStart = { preventAutoScroll() },
                    )
                },
            state = listState
        ) {
            items(chats) { chat ->
                Chat(Modifier, chat)
            }
        }

        val shadowColor = Color(0x70000000)
        val shadowHeight = 40.dp

        if (isTopOverflowing) {
            OverflowShadow(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .height(shadowHeight),
                startColor = shadowColor,
                endColor = Color.Transparent
            )
        }

        if (isBottomOverflowing) {
            OverflowShadow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(shadowHeight),
                startColor = Color.Transparent,
                endColor = shadowColor
            )
        }
    }
}

@Preview
@Composable
private fun ChatSectionPreview() {
    Previews.Wrap(mainTestModule, true) {
        val chatManager = koinInject<IChatManager>()
        chatManager.addChat(ChatModel("This is a test message", ChatModel.State.CREATED, true))
        chatManager.addChat(ChatModel("This is a test response", ChatModel.State.CREATED, false))

        ChatSection()
    }
}
