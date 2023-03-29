package tesler.will.chatassistant.chat

interface IChatManager {
    fun getChats(): ArrayList<ChatModel>
    fun addChat(chatModel: ChatModel)
    fun updateChat(chatModel: ChatModel)
    fun clearChats()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onChatAdded(chatModel: ChatModel) = run { }
        fun onChatUpdated(chatModel: ChatModel) = run { }
        fun onChatsCleared() = run { }
    }
}
