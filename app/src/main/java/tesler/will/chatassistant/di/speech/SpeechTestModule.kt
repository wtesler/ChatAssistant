package tesler.will.chatassistant.di.speech

import org.koin.dsl.module
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.speech.TestSpeechManager

val speechTestModule = module {
    single<ISpeechManager> { TestSpeechManager() }
}
