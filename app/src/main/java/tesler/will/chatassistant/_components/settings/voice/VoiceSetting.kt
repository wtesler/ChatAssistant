package tesler.will.chatassistant._components.settings.voice

import android.speech.tts.Voice
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.SettingsOption
import tesler.will.chatassistant._components.settings.SettingsRow
import tesler.will.chatassistant.modules.settings.settingsTestModule
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager
import tesler.will.chatassistant.store.ISettingsService
import java.util.*

data class VoiceOption(val name: String, val display: String) : SettingsOption(display)

@Composable
fun VoiceSetting() {
    val settingsService = koinInject<ISettingsService>()
    val speechOutputManager = koinInject<ISpeechOutputManager>()

    var selectedOption by remember { mutableStateOf(VoiceOption("", "")) }
    val options = remember { mutableStateListOf<VoiceOption>() }
    val coroutineScope = rememberCoroutineScope()

    fun toVoiceOption(voice: Voice): VoiceOption {
        val name = voice.name
        val locale = voice.locale
        var gender = ""
        for (feature in voice.features) {
            if (feature.startsWith("gender")) {
                gender = feature.split("=")[1]
                gender =
                    gender.replaceFirstChar { it.titlecase(Locale.getDefault()) }
            }
        }
        var displayName = "${locale.displayLanguage} - ${locale.displayCountry}"

        if (gender.isNotEmpty()) {
            displayName += " - $gender"
        }
        return VoiceOption(name, displayName)
    }

    val speechOutputListener = remember {
        object : ISpeechOutputManager.Listener {
            override fun onTtsReady() {
                val defaultVoice = speechOutputManager.getDefaultVoice()
                selectedOption = toVoiceOption(defaultVoice)

                val tempOptions = arrayListOf<VoiceOption>()
                val voices = speechOutputManager.getVoices()
                for (voice in voices) {
                    tempOptions.add(toVoiceOption(voice))
                }

                tempOptions.sortBy { x -> x.displayName }

                for (i in 0 until tempOptions.size) {
                    val option = tempOptions[i]
                    var duplicates = 0
                    for (j in 0 until i) {
                        val other = tempOptions[j]
                        if (option.displayName == other.displayName) {
                            duplicates++
                        }
                    }
                    if (duplicates > 0) {
                        val copy =
                            option.copy(display = "${option.displayName} (${duplicates + 1})")
                        options.add(copy)
                    } else {
                        options.add(option)
                    }
                }

                coroutineScope.launch {
                    settingsService.observeSettings().collect { settings ->
                        if (settings.voice != null) {
                            for (option in options) {
                                if (option.name == settings.voice) {
                                    selectedOption = option
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        speechOutputManager.addListener(speechOutputListener)

        onDispose {
            speechOutputManager.removeListener(speechOutputListener)
        }
    }

    fun onOptionSelected(option: VoiceOption) {
        coroutineScope.launch {
            settingsService.updateVoice(option.name)
        }
        selectedOption = option
    }

    SettingsRow(title = "Voice:", options = options, selected = selectedOption, ::onOptionSelected)
}

@Preview
@Composable
private fun VoiceSettingPreview() {
    Previews.Wrap(settingsTestModule, false) {
        VoiceSetting()
    }
}
