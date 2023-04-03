package tesler.will.chatassistant._components.speechinput.settingsbutton

data class SettingsButtonViewModel(
    val muteText: String,
    val isExpanded: Boolean,
    val options: List<SettingsOption>
)

data class SettingsOption(val text: String, val onClick: () -> Unit)
