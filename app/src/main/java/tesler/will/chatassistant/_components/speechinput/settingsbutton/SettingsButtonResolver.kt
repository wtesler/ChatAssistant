package tesler.will.chatassistant._components.speechinput.settingsbutton

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.stack.BackStackManager
import tesler.will.chatassistant.stack.IBackStackManager
import tesler.will.chatassistant.stack.State

@Composable
fun SettingsButtonResolver(modifier: Modifier) {
    val chatManager = koinInject<IChatManager>()
    val speechInputManager = koinInject<ISpeechInputManager>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()
    val backStackManager = koinInject<IBackStackManager>()

    var isExpanded by remember { mutableStateOf(false) }

    val onNewChatClick = {
        chatManager.clearChats()
        if (speechInputManager.isListening()) {
            speechInputManager.stop()
        } else {
            speechOutputManager.stop()
            speechInputManager.start()
        }
        isExpanded = false
    }

    val onSettingsClick = {
        isExpanded = false
        speechInputManager.stop()
        speechOutputManager.stop()
        backStackManager.push(State.SETTINGS)
    }

    val onIconClick = {
        isExpanded = true
    }

    val onSettingsDismiss = {
        isExpanded = false
    }

    val options = listOf(
        SettingsOption("New Chat") { onNewChatClick() },
        SettingsOption("Settings") { onSettingsClick() },
    )

    SettingsButton(
        modifier = modifier,
        SettingsButtonViewModel(isExpanded, options),
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
