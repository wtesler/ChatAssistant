package tesler.will.chatassistant._components

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.background.Background
import tesler.will.chatassistant._components.card.Card
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.SettingsScreen
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.stack.BackStackManager
import tesler.will.chatassistant.stack.State
import tesler.will.chatassistant.stack.State.MAIN
import tesler.will.chatassistant.stack.State.SETTINGS
import tesler.will.chatassistant.store.ISettingsService

@Composable
fun Main(activity: Activity?) {
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val settingsService = koinInject<ISettingsService>()
    val backStackManager = koinInject<BackStackManager>()

    var backStackState by remember { mutableStateOf(MAIN) }

    val scope = rememberCoroutineScope()

    val backStackListener = remember {
        object : BackStackManager.Listener {
            override fun onBackStackChange(state: State) {
                backStackState = state
            }
        }
    }

    val speechOutputListener = remember {
        object : ISpeechOutputManager.Listener {
            override fun onTtsReady() {
                scope.launch {
                    settingsService.observeSettings().collect { settings ->
                        val voice = settings.voice
                        val speed = settings.speed

                        if (voice != null) {
                            speechOutputManager.setVoice(voice)
                        }
                        if (speed != null) {
                            speechOutputManager.setSpeed(speed)
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        speechOutputManager.addListener(speechOutputListener)
        backStackManager.addListener(backStackListener)

        speechInputManager.init()
        speechOutputManager.init()

        onDispose {
            speechOutputManager.removeListener(speechOutputListener)
            backStackManager.removeListener(backStackListener)

            speechInputManager.destroy()
            speechOutputManager.destroy()
        }
    }

    Background(activity) {
        Card()
        if (backStackState == SETTINGS) {
            SettingsScreen()
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    Previews.Wrap(mainTestModule, true) {
        Main(null)
    }
}
