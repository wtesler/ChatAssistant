package tesler.will.chatassistant._components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.settings.speed.SpeedSettingRowResolver
import tesler.will.chatassistant._components.settings.voice.VoiceSettingRowResolver
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(AppTheme.colors.background)
            .pointerInput("tap_input") {
                detectTapGestures(
                    onPress = {  },
                    onTap = {  },
                    onLongPress = {  },
                    onDoubleTap = { }
                )
            }
            .pointerInput("drag_input") {
                detectDragGestures(
                    onDrag = { _, _ -> },
                    onDragStart = { },
                )
            },
    ) {
        VoiceSettingRowResolver()
        SpeedSettingRowResolver()
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    Previews.Wrap(mainTestModule, false) {
        SettingsScreen()
    }
}
