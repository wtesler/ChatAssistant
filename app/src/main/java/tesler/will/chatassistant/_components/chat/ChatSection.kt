package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.chat.chatModule
import tesler.will.chatassistant.preview.Previews

@Composable
fun ChatSection() {
    val chatManager = koinInject<IChatManager>()

    val chats = remember { mutableStateListOf<ChatModel>() }

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
        }
    }

    DisposableEffect(Unit) {
        chatManager.addListener(chatListener)

        for (chat in chatManager.getChats()) {
            chats.add(chat)
        }

        onDispose {
            chatManager.removeListener(chatListener)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(chats) { chat ->
            Chat(chat)
        }
    }
}

@Preview
@Composable
fun ChatSectionPreview() {
    Previews.Wrap(chatModule, true) {
        val chatManager = koinInject<IChatManager>()
        chatManager.addChat(ChatModel("This is a first test message", ChatModel.State.CREATED))
        chatManager.addChat(ChatModel("This is a second test message", ChatModel.State.CREATING))

        ChatSection()
    }
}
