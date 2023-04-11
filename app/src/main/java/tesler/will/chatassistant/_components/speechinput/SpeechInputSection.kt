package tesler.will.chatassistant._components.speechinput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.speechinput.indicator.SpeechInputIndicator
import tesler.will.chatassistant._components.speechinput.loading.SpeechSubmitLoading
import tesler.will.chatassistant._components.speechinput.mutebutton.MuteButtonResolver
import tesler.will.chatassistant._components.speechinput.settingsbutton.SettingsButtonResolver
import tesler.will.chatassistant._components.speechinput.startbutton.SpeechInputStartButton
import tesler.will.chatassistant._components.speechinput.textinput.SpeechInputTextField
import tesler.will.chatassistant._components.speechinput.typebutton.TypeButton
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun SpeechInputSection(
    viewModel: SpeechInputSectionViewModel,
    onStartClicked: () -> Unit,
    onSubmitClicked: () -> Unit,
    onKeyboardClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onTextChanged: (text: String) -> Unit
) {
    val state = viewModel.state
    val text = viewModel.text
    val isSpeaking = viewModel.isSpeaking

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state == State.TEXT_INPUT) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(0.dp, 55.dp, 0.dp, 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SpeechInputTextField(
                        text = text,
                        onSubmitClicked = onSubmitClicked,
                        onTextChanged = onTextChanged
                    )
                }
            } else if (text.isNotBlank()) {
                val hPadding = AppTheme.dimens.xlarge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(hPadding, 50.dp, hPadding, 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = text,
                        style = AppTheme.type.body1
                    )
                }
            }

            if (state != State.TEXT_INPUT) {
                Box(
                    modifier = Modifier.height(100.dp), contentAlignment = Alignment.Center
                ) {
                    when (state) {
                        State.ACTIVE -> SpeechInputIndicator()
                        State.LOADING -> SpeechSubmitLoading(onStopClicked)
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
                .padding(AppTheme.dimens.medium)
        ) {
            SettingsButtonResolver(modifier = Modifier)
        }

        if (viewModel.state != State.LOADING && viewModel.state != State.TEXT_INPUT) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomEnd)
                    .padding(AppTheme.dimens.medium)
            ) {
                TypeButton(modifier = Modifier, R.drawable.keyboard, onKeyboardClicked)
            }
        }

        AnimatedVisibility(
            visible = isSpeaking,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopStart)
                    .padding(AppTheme.dimens.medium)
            ) {
                MuteButtonResolver(modifier = Modifier)
            }
        }
    }
}

@Preview
@Composable
private fun SpeechInputSectionPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputSection(
            SpeechInputSectionViewModel(State.ACTIVE, "Hi, how can I help?", true),
            {},
            {},
            {},
            {},
            {}
        )
    }
}
