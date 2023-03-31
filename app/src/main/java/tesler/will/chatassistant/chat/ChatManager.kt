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
        chats.removeIf { chat -> chat.state == ERROR }

        for (listener in listeners) {
            listener.onErrorChatsCleared()
        }
    }

    override fun submitChat(chatModel: ChatModel, scope: CoroutineScope) {
        scope.launch {
            var responseChat = ChatModel()
            var message = ""
            var isSuccess = false
            try {
                val response = apiService.updateChat(ChatUpdateRequest.build(chats))
                message = response.message
                responseChat = ChatModel(message, CREATED, false)
                isSuccess = true
            } catch (e: HttpException) {
                message = "${e.code()}: ${e.message()}"
                responseChat = ChatModel(message, ERROR)
                isSuccess = false
            } finally {
                addChat(chatModel)
                addChat(responseChat)
                for (listener in listeners) {
                    listener.onChatSubmitResponse(isSuccess, message)
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
