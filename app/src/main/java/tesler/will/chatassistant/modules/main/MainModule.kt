package tesler.will.chatassistant.modules.main

import org.koin.dsl.module
import tesler.will.chatassistant.modules.chat.chatModule
import tesler.will.chatassistant.modules.speech.speechModule

val mainModule = module {
    includes(speechModule, chatModule)
}
