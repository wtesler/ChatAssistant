package tesler.will.chatassistant._components.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.lifecycle.ModuleActivityLifecycle
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.speed.SpeedSettingRowResolver
import tesler.will.chatassistant._components.settings.voice.VoiceSettingRowResolver
import tesler.will.chatassistant.modules.settings.settingsModule
import tesler.will.chatassistant.modules.settings.settingsTestModule
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.store.ISettingsService
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun SettingsScreen(activity: Activity) {
    ModuleActivityLifecycle(activity, settingsModule) {
        Content()
    }
}

@Composable
private fun Content() {
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val settingsService = koinInject<ISettingsService>()
    val scope = rememberCoroutineScope()

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
        speechOutputManager.init()

        onDispose {
            speechOutputManager.removeListener(speechOutputListener)
            speechOutputManager.destroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        VoiceSettingRowResolver()
        SpeedSettingRowResolver()
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    Previews.Wrap(settingsTestModule, false) {
        Content()
    }
}
