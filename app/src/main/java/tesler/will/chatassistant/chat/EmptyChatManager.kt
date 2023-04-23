package tesler.will.chatassistant.chat

import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.collections.ArrayList

class EmptyChatManager : IChatManager {
    override fun getChats(): ArrayList<ChatModel> {
        return ArrayList()
    }

    override fun numChats(): Int {
        return 0
    }

    override fun addChat(chatModel: ChatModel): String {
        return UUID.randomUUID().toString()
    }

    override fun removeChat(chatId: String) {
    }

    override fun updateChat(chatModel: ChatModel) {
    }

    override fun clearChats() {
    }

    override fun clearErrorChats() {
    }

    override fun submitChat(chatModel: ChatModel, scope: CoroutineScope) {
    }

    override fun cancelSubmit() {
    }

    override fun addListener(listener: IChatManager.Listener) {
    }

    override fun removeListener(listener: IChatManager.Listener) {
    }
}