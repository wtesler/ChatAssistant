package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    val scrollToLastItem = {
        coroutineScope.launch {
            listState.animateScrollToItem(chats.lastIndex)
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
                    scrollToLastItem()
                }
            }
        }
    }

    val speechInputListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onListeningStarted() {
                scrollToLastItem()
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
}

@Preview
@Composable
fun ChatSectionPreview() {
    Previews.Wrap(mainTestModule, true) {
        val chatManager = koinInject<IChatManager>()
        chatManager.addChat(ChatModel("This is a first test message", ChatModel.State.CREATED))
        chatManager.addChat(ChatModel("This is a second test message", ChatModel.State.CREATING))

        ChatSection()
    }
}
