package tesler.will.chatassistant._components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.speed.SpeedSetting
import tesler.will.chatassistant._components.settings.voice.VoiceSetting
import tesler.will.chatassistant.modules.settings.settingsModule
import tesler.will.chatassistant.modules.settings.settingsTestModule
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager

@Composable
fun SettingsScreen() {
    DisposableEffect(Unit) {
        loadKoinModules(settingsModule)

        onDispose {
            unloadKoinModules(settingsModule)
        }
    }

    Content()
}

@Composable
private fun Content() {
    val speechOutputManager = koinInject<ISpeechOutputManager>()

    DisposableEffect(Unit) {
        speechOutputManager.init(null, null)

        onDispose {
            speechOutputManager.destroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        VoiceSetting()
        SpeedSetting()
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    Previews.Wrap(settingsTestModule, false) {
        Content()
    }
}
