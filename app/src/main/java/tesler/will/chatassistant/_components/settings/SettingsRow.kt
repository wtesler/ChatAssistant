package tesler.will.chatassistant._components.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.voice.VoiceOption
import tesler.will.chatassistant.modules.settings.settingsTestModule
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun <T : SettingsOption> SettingsRow(
    title: String,
    options: List<T>,
    selected: T,
    onOptionSelect: (T) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(MaterialTheme.spacing.xlarge, MaterialTheme.spacing.xlarge)
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth(.25f)
                .align(CenterVertically),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.body1
        )

        val shape = RoundedCornerShape(10.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterVertically)
                .border(BorderStroke(0.dp, Color.Black), shape)
                .clip(shape)
        ) {
            Text(
                text = selected.displayName,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false),
                        onClick = { isExpanded = !isExpanded }
                    )
                    .background(MaterialTheme.colors.primaryVariant)
                    .padding(MaterialTheme.spacing.medium, MaterialTheme.spacing.large + 2.dp)

            )

            DropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(.653f)
                    .background(MaterialTheme.colors.secondaryVariant),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                offset = DpOffset(0.dp, 5.dp)
            ) {
                for (i in options.indices) {
                    val option = options[i]
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onOptionSelect(option)
                            isExpanded = false
                        },
                        content = {
                            Text(
                                text = (option.displayName),
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colors.onSurface,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsRowPreview() {
    Previews.Wrap(settingsTestModule, false) {
        SettingsRow(
            "Voice:",
            listOf(
                VoiceOption("name1", "displayName1")
            ), VoiceOption("defaultName", "defaultDisplayName")
        ) {}
    }
}