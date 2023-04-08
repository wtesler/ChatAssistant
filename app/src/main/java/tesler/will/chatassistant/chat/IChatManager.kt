package tesler.will.chatassistant.chat

import kotlinx.coroutines.CoroutineScope

interface IChatManager {
    fun getChats(): ArrayList<ChatModel>
    fun numChats(): Int
    fun addChat(chatModel: ChatModel)
    fun removeChat(chatId: String)
    fun updateChat(chatModel: ChatModel)
    fun clearChats()
    fun clearErrorChats()
    fun submitChat(chatModel: ChatModel, scope: CoroutineScope)
    fun cancelSubmit()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onChatAdded(chatModel: ChatModel) = run { }
        fun onChatRemoved(chatId: String) = run { }
        fun onChatUpdated(chatModel: ChatModel) = run { }
        fun onChatsCleared() = run { }
        fun onErrorChatsCleared() = run { }
        fun onNumChatsChanged(num: Int) = run { }
        fun onChatSubmitResponseStarted() = run { }
        fun onChatSubmitResponsePartial(value: String) = run { }
        fun onChatSubmitResponse(isSuccess: Boolean, value: String?) = run { }
    }
}
