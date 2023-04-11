package tesler.will.chatassistant._components.speechinput.settingsbutton

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.activities.SettingsActivity
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechinput.ISpeechInputManager

@Composable
fun SettingsButtonResolver(modifier: Modifier) {
    val chatManager = koinInject<IChatManager>()
    val speechInputManager = koinInject<ISpeechInputManager>()


    val context = LocalContext.current

    var isExpanded by remember { mutableStateOf(false) }

    val onNewChatClick = {
        chatManager.clearChats()
        if (speechInputManager.isListening()) {
            speechInputManager.stop()
        } else {
            speechInputManager.start()
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
