package tesler.will.chatassistant._components.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.lifecycle.ModuleActivityLifecycle
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.speed.SpeedSetting
import tesler.will.chatassistant._components.settings.voice.VoiceSetting
import tesler.will.chatassistant.modules.settings.settingsModule
import tesler.will.chatassistant.modules.settings.settingsTestModule
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
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

    DisposableEffect(Unit) {
        speechOutputManager.init(null, null)

        onDispose {
            speechOutputManager.destroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(AppTheme.colors.background)
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
