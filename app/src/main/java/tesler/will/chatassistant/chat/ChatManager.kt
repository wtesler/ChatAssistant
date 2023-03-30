package tesler.will.chatassistant.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import tesler.will.chatassistant.chat.ChatModel.State.CREATED
import tesler.will.chatassistant.chat.ChatModel.State.ERROR
import tesler.will.chatassistant.chat.IChatManager.Listener
import tesler.will.chatassistant.server.ApiService
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest

class ChatManager(private val apiService: ApiService) : IChatManager {
    private var chats: ArrayList<ChatModel> = ArrayList()

    private val listeners = mutableListOf<Listener>()

    override fun getChats(): ArrayList<ChatModel> {
        return chats
    }

    override fun addChat(chatModel: ChatModel) {
        for (chat in chats) {
            if (chat.id == chatModel.id) {
                throw Exception("Cannot add chat because it already exists.")
            }
        }

        val chat = chatModel.copy()

        chats.add(chat)
        for (listener in listeners) {
            listener.onChatAdded(chat)
        }
    }

    override fun updateChat(chatModel: ChatModel) {
        val chat = chatModel.copy()

        for (i in 0 until chats.size) {
            if (chats[i].id == chat.id) {
                chats[i] = chat
                break
            }
        }

        for (listener in listeners) {
            listener.onChatUpdated(chat)
        }
    }

    override fun clearChats() {
        chats.clear()
        for (listener in listeners) {
            listener.onChatsCleared()
        }
    }

    override fun clearErrorChats() {
        chats.removeIf{chat -> chat.state == ERROR}

        for (listener in listeners) {
            listener.onErrorChatsCleared();
        }
    }

    override fun submitChat(value: String?, scope: CoroutineScope) {
        if (value == null) {
            return
        }

        var pendingChat: ChatModel? = null
        if (chats.size > 0) {
            pendingChat = chats.last()
        }

        scope.launch {
            try {
                val response = apiService.updateChat(ChatUpdateRequest(value))
                val message = response.message

                val responseChat = ChatModel(message, CREATED, false)
                addChat(responseChat)

                for (listener in listeners) {
                    listener.onChatSubmitResponse(true, message)
                }
            } catch (e: HttpException) {
                val message = "${e.code()}: ${e.message()}"
                val responseChat = ChatModel(message, ERROR)
                addChat(responseChat)
                for (listener in listeners) {
                    listener.onChatSubmitResponse(false, null)
                }
            } finally {
                if (pendingChat != null) {
                    pendingChat.state = CREATED
                    updateChat(pendingChat)
                }
            }
        }
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }
}
