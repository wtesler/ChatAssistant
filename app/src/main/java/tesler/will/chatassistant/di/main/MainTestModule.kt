package tesler.will.chatassistant.di.main

import org.koin.dsl.module
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.speech.TestSpeechManager

val mainTestModule = module {
    single<ISpeechManager> { TestSpeechManager() }
}
