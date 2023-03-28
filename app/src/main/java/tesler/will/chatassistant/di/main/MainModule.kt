package tesler.will.chatassistant.di.main

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.speech.SpeechManager

val mainModule = module {
    single<ISpeechManager> { SpeechManager(androidContext()) }
}
