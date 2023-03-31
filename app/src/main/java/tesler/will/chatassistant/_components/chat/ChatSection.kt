package tesler.will.chatassistant._components.chat

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun ChatSection() {
    val chatManager = koinInject<IChatManager>()
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()

    val chats = remember { mutableStateListOf<ChatModel>() }
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

    val scrollToLastItem = { shouldAnimate: Boolean ->
        coroutineScope.launch {
            awaitFrame()
            if (listState.isScrollInProgress) {
                listState.stopScroll()
            }
            if (shouldAnimate) {
                listState.animateScrollToItem(chats.lastIndex)
            } else {
                listState.scrollToItem(chats.lastIndex)
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

            override fun onChatsCleared() {
                chats.clear()
            }

            override fun onErrorChatsCleared() {
                chats.removeIf { chat -> chat.state == ChatModel.State.ERROR }
            }

            override fun onChatSubmitResponse(isSuccess: Boolean, value: String?) {
                if (isSuccess && value != null) {
                    speechOutputManager.speak(value)
                    scrollToLastItem(true)
                }
            }
        }
    }

    val speechInputListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onListeningStarted() {
                scrollToLastItem(true)
            }

            override fun onText(value: String?) {
                scrollToLastItem(false)
            }

            override fun onError(statusCode: Int?) {
                scrollToLastItem(false)
            }
        }
    }

    DisposableEffect(Unit) {
        chatManager.addListener(chatListener)
        speechInputManager.addListener(speechInputListener)
        speechOutputManager.init()

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
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = listState
        ) {
            items(chats) { chat ->
                Chat(chat)
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
        chatManager.addChat(ChatModel("This is a first test message", ChatModel.State.CREATED))
        chatManager.addChat(ChatModel("This is a second test message", ChatModel.State.CREATED))

        ChatSection()
    }
}
