package tesler.will.chatassistant.chat

import kotlinx.coroutines.CoroutineScope

class EmptyChatManager : IChatManager {
    override fun getChats(): ArrayList<ChatModel> {
        return ArrayList()
    }

    override fun addChat(chatModel: ChatModel) {
    }

    override fun updateChat(chatModel: ChatModel) {
    }

    override fun clearChats() {
    }

    override fun clearErrorChats() {
    }

    override fun submitChat(chatModel: ChatModel, scope: CoroutineScope) {
    }

    override fun addListener(listener: IChatManager.Listener) {
    }

    override fun removeListener(listener: IChatManager.Listener) {
    }
}