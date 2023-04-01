package tesler.will.chatassistant.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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

    override fun removeChat(chatId: String) {
        chats.removeIf { chat -> chat.id == chatId }

        for (listener in listeners) {
            listener.onChatRemoved(chatId)
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

    private fun ResponseBody.stream(): Flow<String> {
        return flow {
            byteStream().use { inputStream ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var numBytes = inputStream.read(buffer)
                while (numBytes > 0) {
                    val string = String(buffer.copyOfRange(0, numBytes), Charsets.UTF_8)
                    emit(string)
                    numBytes = inputStream.read(buffer)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun submitChat(chatModel: ChatModel, scope: CoroutineScope) {
        scope.launch {
            val chatsCopy = ArrayList(chats)
            chatsCopy.add(chatModel)

            // Prepares our input chat
            val inputText = chatModel.text
            val inputChat = chatModel.copy(text = "")
            val inputChatId = inputChat.id
            var hasUpdatedInputChat = false
            addChat(inputChat)

            var responseChat = ChatModel("", CREATED, false)
            addChat(responseChat)

            var message = ""
            var isSuccess = false
            try {
                apiService.updateChat(ChatUpdateRequest.build(chatsCopy))
                    .stream()
                    .collect { string ->
                        responseChat.text += string
                        updateChat(responseChat)
                        if (!hasUpdatedInputChat && responseChat.text.isNotEmpty()) {
                            inputChat.text = inputText
                            updateChat(inputChat)
                            hasUpdatedInputChat = true
                            for (listener in listeners) {
                                listener.onChatSubmitResponseStarted()
                            }
                        }
                        if (string.isNotEmpty()) {
                            for (listener in listeners) {
                                listener.onChatSubmitResponsePartial(string)
                            }
                        }
                    }

                message = responseChat.text
                isSuccess = true
            } catch (e: HttpException) {
                message = "${e.code()}: ${e.message()}"
                responseChat = responseChat.copy(text = message, state = ERROR)
                isSuccess = false
                removeChat(inputChatId)
                addChat(responseChat)
            } finally {
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
