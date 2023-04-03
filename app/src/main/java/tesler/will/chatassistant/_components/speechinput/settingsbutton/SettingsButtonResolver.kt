package tesler.will.chatassistant._components.speechinput.settingsbutton

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.activities.SettingsActivity
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.store.ISettingsService

@Composable
fun SettingsButtonResolver(modifier: Modifier) {
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val settingsService = koinInject<ISettingsService>()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isExpanded by remember { mutableStateOf(false) }

    var isMute by remember { mutableStateOf(false) }
    val muteText by remember(isMute) {
        derivedStateOf {
            if (isMute) "Unmute" else "Mute"
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            settingsService.observeSettings().collect { settings ->
                isMute = settings.isMute
                speechOutputManager.setMuted(isMute)
            }
        }
    }

    val onMuteToggle = {
        coroutineScope.launch {
            settingsService.updateIsMute(!isMute)
        }
        isExpanded = false
    }

    val onSettingsClick = {
        isExpanded = false
        val intent = Intent(context, SettingsActivity::class.java)
        val activity: Activity = context as Activity
        activity.finish()
        context.startActivity(intent)
    }

    val onIconClick = {
        isExpanded = true
    }

    val onSettingsDismiss = {
        isExpanded = false
    }

    val options = listOf(
        SettingsOption(muteText) { onMuteToggle() },
        SettingsOption("Settings") { onSettingsClick() },
    )

    SettingsButton(
        modifier = modifier,
        SettingsButtonViewModel(muteText, isExpanded, options),
        onIconClick,
        onSettingsDismiss
    )
}

@Preview
@Composable
private fun SettingsButtonResolverPreview() {
    Previews.Wrap(mainTestModule, true) {
        SettingsButtonResolver(Modifier)
    }
}
