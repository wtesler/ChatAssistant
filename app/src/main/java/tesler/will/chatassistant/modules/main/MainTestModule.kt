package tesler.will.chatassistant.modules.main

import org.koin.dsl.module
import tesler.will.chatassistant.modules.chat.chatModule
import tesler.will.chatassistant.modules.server.serverTestModule
import tesler.will.chatassistant.modules.speech.speechTestModule

val mainTestModule = module {
    includes(speechTestModule, chatModule, serverTestModule)
}
