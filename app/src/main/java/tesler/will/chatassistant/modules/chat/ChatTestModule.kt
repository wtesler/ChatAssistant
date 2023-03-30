package tesler.will.chatassistant.modules.chat

import org.koin.dsl.module
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.chat.EmptyChatManager

val chatTestModule = module {
    single<IChatManager> { EmptyChatManager() }
}
