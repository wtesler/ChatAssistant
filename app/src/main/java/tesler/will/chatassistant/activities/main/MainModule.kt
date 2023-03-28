package tesler.will.chatassistant.activities.main

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tesler.will.chatassistant.speech.SpeechManager

val mainModule = module {
    single { SpeechManager(androidContext()) }
}
