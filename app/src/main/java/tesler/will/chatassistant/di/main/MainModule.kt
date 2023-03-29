package tesler.will.chatassistant.di.main

import org.koin.dsl.module
import tesler.will.chatassistant.di.chat.chatModule
import tesler.will.chatassistant.di.speech.speechModule

val mainModule = module {
    includes(speechModule, chatModule)
}
