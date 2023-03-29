package tesler.will.chatassistant.di.chat

import org.koin.dsl.module
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.chat.TestChatManager

val chatTestModule = module {
    single<IChatManager> { TestChatManager() }
}
