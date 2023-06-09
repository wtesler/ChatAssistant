package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.*
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
import tesler.will.chatassistant._components.shadow.ElevationShadow
import tesler.will.chatassistant._components.shadow.OverflowShadow
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.ChatModel.State.ERROR
import tesler.will.chatassistant.chat.HighlightModel
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager.SpeechChunk

@Composable
fun ChatSection() {
    val chatManager = koinInject<IChatManager>()
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()

    val chats = remember { mutableStateListOf<ChatModel>() }
    val activeChunks = remember { mutableStateListOf<SpeechChunk>() }
    var highlightModel by remember { mutableStateOf(HighlightModel()) }
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

    fun clearHighlights() {
        highlightModel = HighlightModel()
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
                chats.removeIf { chat -> chat.state == ERROR }
            }

            override fun onChatSubmitResponseStarted() {
                hasManuallyInterfered = false
                activeChunks.clear()
                clearHighlights()
            }

            override fun onChatSubmitResponsePartial(value: String) {
                val chunk = speechOutputManager.queueSpeech(value)
                if (chunk != null) {
                    activeChunks.add(chunk)
                }
                scrollToLastItem(0f)
            }

            override fun onChatSubmitResponse(isSuccess: Boolean, value: String?) {
                if (isSuccess && value != null) {
                    val chunk = speechOutputManager.flushSpeech()
                    activeChunks.add(chunk)
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

    val speechOutputListener = remember {
        object : ISpeechOutputManager.Listener {
            override fun onTtsReady(chunk: SpeechChunk?) {
                if (chunk != null) {
                    activeChunks.add(chunk)
                }
            }

            override fun onSpeechProgress(utteranceId: String?, start: Int, end: Int) {
                if (utteranceId == null) {
                    return
                }
                var startIndex = 0
                var endIndex = 0
                for (chunk in activeChunks) {
                    if (chunk.utteranceId != utteranceId) {
                        startIndex += chunk.text.length + chunk.trimmedAmount
                    } else {
                        startIndex += chunk.trimmedAmount
                        endIndex = startIndex + end
                        startIndex += start
                        break
                    }
                }

                highlightModel = highlightModel.copy(start=startIndex, end=endIndex)
            }

            override fun onSpeechEnded() {
                clearHighlights()
            }
        }
    }

    DisposableEffect(Unit) {
        chatManager.addListener(chatListener)
        speechInputManager.addListener(speechInputListener)
        speechOutputManager.addListener(speechOutputListener)

        for (chat in chatManager.getChats()) {
            chats.add(chat)
        }

        onDispose {
            chatManager.removeListener(chatListener)
            speechInputManager.removeListener(speechInputListener)
            speechOutputManager.removeListener(speechOutputListener)
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
                .pointerInput("scroll") {
                    detectVerticalDragGestures(
                        onDragStart = { preventAutoScroll() },
                        onVerticalDrag = { _, _ -> preventAutoScroll() }
                    )
                }
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

//            itemsIndexed(chats) { index, chat ->
//                val highlight = if (index == chats.size - 1) highlightModel else null
//                Chat(Modifier, chat, highlight)
//            }
        }

        val shadowColor = Color(0x60000000)
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

        if (chats.any { x -> x.text.isNotBlank() }) {
            ElevationShadow(Modifier.align(Alignment.BottomCenter))
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
