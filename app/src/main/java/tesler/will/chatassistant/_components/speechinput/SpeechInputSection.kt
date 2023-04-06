package tesler.will.chatassistant._components.speechinput

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.shadow.ElevationShadow
import tesler.will.chatassistant._components.speechinput.indicator.SpeechInputIndicator
import tesler.will.chatassistant._components.speechinput.settingsbutton.SettingsButtonResolver
import tesler.will.chatassistant._components.speechinput.startbutton.SpeechInputStartButton
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun SpeechInputSection(viewModel: SpeechInputSectionViewModel, onStartClicked: () -> Unit) {
    val state = viewModel.state
    val text = viewModel.text

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state == State.READY || (state == State.ACTIVE && text.isEmpty())) {
                ElevationShadow()
            }

            if (text.isBlank()) {
                ElevationShadow()
            } else {
                val hPadding = MaterialTheme.spacing.large
                val bottomPadding = MaterialTheme.spacing.small

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(hPadding, 50.dp, hPadding, bottomPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = text,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier.height(90.dp), contentAlignment = Alignment.Center
            ) {
                when (state) {
                    State.ACTIVE -> SpeechInputIndicator()
                    State.LOADING -> CircularProgressIndicator(
                        color = MaterialTheme.colors.onSurface, strokeWidth = 5.dp
                    )
                    State.READY -> SpeechInputStartButton(onStartClicked)
                }
            }
        }

        Box(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(Alignment.TopEnd)
                .padding(MaterialTheme.spacing.medium)
        ) {
            SettingsButtonResolver(modifier = Modifier)
        }
    }
}

@Preview
@Composable
private fun SpeechInputSectionPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSection(SpeechInputSectionViewModel(State.ACTIVE, "Hi, how can I help?")) {}
    }
}
