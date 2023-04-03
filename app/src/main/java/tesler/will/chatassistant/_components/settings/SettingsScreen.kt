package tesler.will.chatassistant._components.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.voice.VoiceSetting
import tesler.will.chatassistant.modules.settings.settingsTestModule
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager

@Composable
fun SettingsScreen(activity: Activity?) {
    val speechOutputManager = koinInject<ISpeechOutputManager>()

    DisposableEffect(Unit) {
        speechOutputManager.init(null)

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
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    Previews.Wrap(settingsTestModule, false) {
        SettingsScreen(null)
    }
}
