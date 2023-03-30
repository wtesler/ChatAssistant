package tesler.will.chatassistant.modules.chat

import org.koin.dsl.module
import tesler.will.chatassistant.chat.ChatManager
import tesler.will.chatassistant.chat.IChatManager

val chatModule = module {
    single<IChatManager> { ChatManager(get()) }
}
