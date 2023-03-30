package tesler.will.chatassistant.chat

import kotlinx.coroutines.CoroutineScope

interface IChatManager {
    fun getChats(): ArrayList<ChatModel>
    fun addChat(chatModel: ChatModel)
    fun updateChat(chatModel: ChatModel)
    fun clearChats()
    fun clearErrorChats()
    fun submitChat(value: String?, scope: CoroutineScope)
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onChatAdded(chatModel: ChatModel) = run { }
        fun onChatUpdated(chatModel: ChatModel) = run { }
        fun onChatsCleared() = run { }
        fun onErrorChatsCleared() = run { }
        fun onChatSubmitResponse(isSuccess: Boolean, value: String?) = run { }
    }
}
