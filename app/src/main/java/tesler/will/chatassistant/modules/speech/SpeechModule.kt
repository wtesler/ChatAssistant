package tesler.will.chatassistant.modules.speech

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechinput.SpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.speechoutput.SpeechOutputManager

val speechModule = module {
    single<ISpeechInputManager> { SpeechInputManager(androidContext(), get(), get()) }
    single<ISpeechOutputManager> { SpeechOutputManager(androidContext()) }

}
