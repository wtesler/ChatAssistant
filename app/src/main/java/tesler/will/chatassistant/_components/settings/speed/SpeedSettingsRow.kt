package tesler.will.chatassistant._components.settings.speed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.SettingsOption
import tesler.will.chatassistant._components.settings.SettingsRow
import tesler.will.chatassistant._components.settings.voice.VoiceOption
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun <T : SettingsOption> SpeedSettingsRow(
    options: List<T>,
    selected: T,
    onOptionSelect: (T) -> Unit
) {
    SettingsRow(
        title = "Speed:",
        options = options,
        selected = selected
    ) { option, setExpanded ->
        DropdownMenuItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onOptionSelect(option)
                setExpanded(false)
            },
            content = {
                Text(
                    text = (option.displayName),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTheme.type.body1
                )
            }
        )
    }
}

@Preview
@Composable
private fun SpeedSettingsRowPreview() {
    Previews.Wrap(mainTestModule, false) {
        SpeedSettingsRow(
            listOf(
                VoiceOption("name1", "displayName1")
            ), VoiceOption("defaultName", "defaultDisplayName"),
            {})
    }
}
