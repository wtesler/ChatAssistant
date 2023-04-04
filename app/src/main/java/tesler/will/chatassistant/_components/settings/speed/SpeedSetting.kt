package tesler.will.chatassistant._components.settings.speed

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.SettingsOption
import tesler.will.chatassistant._components.settings.SettingsRow
import tesler.will.chatassistant.modules.settings.settingsTestModule
import tesler.will.chatassistant.store.ISettingsService

data class SpeedOption(val value: Float) : SettingsOption(String.format("%.1f", value))

@Composable
fun SpeedSetting() {
    val settingsService = koinInject<ISettingsService>()

    var selectedOption by remember { mutableStateOf(SpeedOption(1f)) }
    val options = remember { mutableStateListOf<SpeedOption>() }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        options.add(SpeedOption(.5f))
        options.add(SpeedOption(.6f))
        options.add(SpeedOption(.8f))
        options.add(SpeedOption(1f))
        options.add(SpeedOption(1.1f))
        options.add(SpeedOption(1.2f))
        options.add(SpeedOption(1.3f))
        options.add(SpeedOption(1.4f))
        options.add(SpeedOption(2.0f))

        coroutineScope.launch {
            settingsService.observeSettings().collect { settings ->
                val speed = settings.speed
                if (speed != null) {
                    selectedOption = SpeedOption(speed)
                } else {
                    selectedOption = SpeedOption(1f)
                }
            }
        }
    }

    fun onOptionSelected(option: SpeedOption) {
        coroutineScope.launch {
            settingsService.updateSpeed(option.value)
        }
        selectedOption = option
    }

    SettingsRow(title = "Speed:", options = options, selected = selectedOption, ::onOptionSelected)
}

@Preview
@Composable
private fun SpeedSettingPreview() {
    Previews.Wrap(settingsTestModule, false) {
        SpeedSetting()
    }
}
