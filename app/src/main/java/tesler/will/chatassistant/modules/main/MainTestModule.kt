package tesler.will.chatassistant.modules.main

import org.koin.dsl.module
import tesler.will.chatassistant.modules.chat.chatModule
import tesler.will.chatassistant.modules.server.serverTestModule
import tesler.will.chatassistant.modules.speech.speechTestModule
import tesler.will.chatassistant.store.EmptySettingsService
import tesler.will.chatassistant.store.ISettingsService

val mainTestModule = module {
    includes(speechTestModule, chatModule, serverTestModule)

    single<ISettingsService> { EmptySettingsService() }
}
