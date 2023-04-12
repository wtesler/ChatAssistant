package tesler.will.chatassistant._components.settings.voice

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.SettingsOption
import tesler.will.chatassistant._components.settings.SettingsRow
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun <T : SettingsOption> VoiceSettingsRow(
    options: List<T>,
    selected: T,
    onOptionSelect: (T) -> Unit
) {
    SettingsRow(
        title = "Voice:",
        options = options,
        selected = selected
    ) { option, _ ->
        val isChecked = option == selected

        DropdownMenuItem(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(0.dp),
            onClick = {
                onOptionSelect(option)
            },
            content = {
                Row(modifier = Modifier, verticalAlignment = CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onOptionSelect(option) },
                        colors = CheckboxDefaults.colors(
                            AppTheme.colors.iconPrimary,
                            AppTheme.colors.iconSecondary,
                            AppTheme.colors.background
                        )
                    )
                    Text(
                        text = (option.displayName),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        style = AppTheme.type.option1
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun VoiceSettingsRowPreview() {
    Previews.Wrap(mainTestModule, false) {
        VoiceSettingsRow(
            listOf(
                VoiceOption("name1", "displayName1")
            ), VoiceOption("defaultName", "defaultDisplayName"),
            {})
    }
}
