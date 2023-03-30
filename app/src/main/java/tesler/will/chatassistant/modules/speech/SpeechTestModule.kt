package tesler.will.chatassistant.modules.speech

import org.koin.dsl.module
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.speech.EmptySpeechManager

val speechTestModule = module {
    single<ISpeechManager> { EmptySpeechManager() }
}
