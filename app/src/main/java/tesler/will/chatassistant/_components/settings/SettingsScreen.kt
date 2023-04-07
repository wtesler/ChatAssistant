package tesler.will.chatassistant._components.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
fun SettingsScreen(activity: Activity) {
    var areModulesLoaded by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        loadKoinModules(settingsModule)
        areModulesLoaded = true
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                unloadKoinModules(settingsModule)
                activity.finishAndRemoveTask()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (areModulesLoaded) {
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
