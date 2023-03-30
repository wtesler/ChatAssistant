package tesler.will.chatassistant.modules.speech

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.speech.SpeechManager

val speechModule = module {
    single<ISpeechManager> { SpeechManager(androidContext()) }
}
