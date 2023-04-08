package tesler.will.chatassistant._components.speechinput

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.shadow.ElevationShadow
import tesler.will.chatassistant._components.speechinput.indicator.SpeechInputIndicator
import tesler.will.chatassistant._components.speechinput.keyboardbutton.KeyboardButton
import tesler.will.chatassistant._components.speechinput.settingsbutton.SettingsButtonResolver
import tesler.will.chatassistant._components.speechinput.startbutton.SpeechInputStartButton
import tesler.will.chatassistant._components.speechinput.submitbutton.SpeechSubmitButton
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun SpeechInputSection(
    viewModel: SpeechInputSectionViewModel,
    onStartClicked: () -> Unit,
    onSubmitClicked: () -> Unit,
    onKeyboardClicked: () -> Unit,
    onTextChanged: (text: String) -> Unit,
    focusRequester: FocusRequester
) {
    val state = viewModel.state
    val text = viewModel.text
    val numChats = viewModel.numChats

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
            if (numChats > 0) {
                ElevationShadow()
            }

            val hPadding = MaterialTheme.spacing.xlarge
            val bottomPadding = MaterialTheme.spacing.small

            if (state == State.TEXT_INPUT) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(hPadding, 70.dp, hPadding, 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = text,
                        onValueChange = onTextChanged,
                        trailingIcon = { SpeechSubmitButton(onSubmitClicked) }
                    )
                }
            } else if (text.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(hPadding, 55.dp, hPadding, bottomPadding),
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

            if (state != State.TEXT_INPUT) {
                Box(
                    modifier = Modifier.height(100.dp), contentAlignment = Alignment.Center
                ) {
                    when (state) {
                        State.ACTIVE -> SpeechInputIndicator()
                        State.LOADING -> CircularProgressIndicator(
                            color = MaterialTheme.colors.onSurface, strokeWidth = 5.dp
                        )
                        State.READY -> SpeechInputStartButton(onStartClicked)
                        State.TEXT_INPUT -> {}
                    }
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

        if (viewModel.state != State.LOADING) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.spacing.medium)
            ) {
                KeyboardButton(modifier = Modifier, onKeyboardClicked)
            }
        }
    }
}

@Preview
@Composable
private fun SpeechInputSectionPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSection(
            SpeechInputSectionViewModel(State.TEXT_INPUT, "Hi, how can I help?"),
            {},
            {},
            {},
            {},
            FocusRequester()
        )
    }
}
