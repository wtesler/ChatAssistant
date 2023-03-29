package tesler.will.chatassistant.chat

class TestChatManager : IChatManager {
    override fun getChats(): ArrayList<ChatModel> {
        return ArrayList()
    }

    override fun addChat(chatModel: ChatModel) {
    }

    override fun updateChat(chatModel: ChatModel) {
    }

    override fun clearChats() {
    }

    override fun addListener(listener: IChatManager.Listener) {
    }

    override fun removeListener(listener: IChatManager.Listener) {
    }
}