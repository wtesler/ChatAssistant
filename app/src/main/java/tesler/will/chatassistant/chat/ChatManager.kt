package tesler.will.chatassistant.chat

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import tesler.will.chatassistant.chat.ChatModel.State.CREATED
import tesler.will.chatassistant.chat.ChatModel.State.ERROR
import tesler.will.chatassistant.chat.IChatManager.Listener
import tesler.will.chatassistant.server.ApiService
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest
import java.net.SocketTimeoutException

class ChatManager(private val apiService: ApiService) : IChatManager {
    private var chats: ArrayList<ChatModel> = ArrayList()
    private var submitJob: Job? = null

    private val listeners = mutableListOf<Listener>()

    override fun getChats(): ArrayList<ChatModel> {
        return chats
    }

    override fun numChats(): Int {
        return chats.size
    }

    override fun addChat(chatModel: ChatModel): String {
        chatModel.setId()
        val chatCopy = chatModel.copy()
        chats.add(chatCopy)

        for (listener in listeners) {
            listener.onChatAdded(chatCopy)
            listener.onNumChatsChanged(chats.size)
        }

        return chatModel.id
    }

    override fun removeChat(chatId: String) {
        chats.removeIf { chat -> chat.id == chatId }

        for (listener in listeners) {
            listener.onChatRemoved(chatId)
            listener.onNumChatsChanged(chats.size)
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
            listener.onNumChatsChanged(chats.size)
        }
    }

    override fun clearErrorChats() {
        chats.removeIf { chat -> chat.state == ERROR }

        for (listener in listeners) {
            listener.onErrorChatsCleared()
            listener.onNumChatsChanged(chats.size)
        }
    }

    private fun ResponseBody.stream(): Flow<String> {
        return flow {
            byteStream().use { inputStream ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var numBytes: Int
                while (run { numBytes = inputStream.read(buffer); numBytes > 0 }) {
                    val string = String(buffer.copyOfRange(0, numBytes), Charsets.UTF_8)
                    emit(string)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun submitChat(chatModel: ChatModel, scope: CoroutineScope) {
        val isJobActive = submitJob?.isActive
        if (isJobActive != null && isJobActive) {
            Log.w("Chat Manager", "Cannot submit because currently submitting.")
            return
        }

        submitJob = scope.launch {
            var chatsCopy = ArrayList(chats)
            chatsCopy.add(chatModel)
            val MAX_CHAT_LENGTH = 6
            if (chatsCopy.size > MAX_CHAT_LENGTH) {
                chatsCopy =
                    ArrayList(
                        chatsCopy.subList(
                            chatsCopy.size - MAX_CHAT_LENGTH,
                            chatsCopy.size
                        )
                    )
            }

            val inputText = chatModel.text
            val inputChat = chatModel.copy(text = "")
            val inputChatId = addChat(inputChat)

            val responseChat = ChatModel("", CREATED, false)
            val responseChatId = addChat(responseChat)

            var message = ""
            var isSuccess = false
            var hasResponseStarted = false

            try {
                apiService.updateChat(ChatUpdateRequest.build(chatsCopy))
                    .stream()
                    .catch { cause -> throw cause }
                    .cancellable()
                    .collect { string ->
                        responseChat.text += string
                        updateChat(responseChat)
                        if (!hasResponseStarted && responseChat.text.isNotEmpty()) {
                            inputChat.text = inputText
                            updateChat(inputChat)
                            hasResponseStarted = true
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
                isSuccess = false
                responseChat.text = message
                responseChat.state = ERROR
                updateChat(responseChat)
            } catch (e: SocketTimeoutException) {
                message = "Error Timeout."
                isSuccess = false
                responseChat.text = message
                responseChat.state = ERROR
                updateChat(responseChat)
            } catch (e: CancellationException) {
                message = "Cancellation Occurred."
                isSuccess = false
                removeChat(responseChatId)
            } finally {
                if (!isSuccess) {
                    removeChat(inputChatId)
                }
                for (listener in listeners) {
                    listener.onChatSubmitResponse(isSuccess, message)
                }
            }
        }
    }

    override fun cancelSubmit() {
        val isJobActive = submitJob?.isActive
        if (isJobActive != null && isJobActive) {
            submitJob?.cancel()
        }
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }
}
