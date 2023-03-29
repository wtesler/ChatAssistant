package tesler.will.chatassistant.di.main

import org.koin.dsl.module
import tesler.will.chatassistant.di.chat.chatModule
import tesler.will.chatassistant.di.speech.speechTestModule

val mainTestModule = module {
    includes(speechTestModule, chatModule)
}
