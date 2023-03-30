package tesler.will.chatassistant.modules.speech

import org.koin.dsl.module
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechinput.EmptySpeechInputManager
import tesler.will.chatassistant.speechoutput.EmptySpeechOutputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager

val speechTestModule = module {
    single<ISpeechInputManager> { EmptySpeechInputManager() }
    single<ISpeechOutputManager> { EmptySpeechOutputManager() }
}
