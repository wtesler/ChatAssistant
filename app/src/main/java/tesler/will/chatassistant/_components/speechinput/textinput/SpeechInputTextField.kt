package tesler.will.chatassistant._components.speechinput.textinput

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.speechinput.submitbutton.SpeechSubmitButton
import tesler.will.chatassistant.modules.main.mainTestModule

@Composable
fun SpeechInputTextField(
    text: String,
    onSubmitClicked: () -> Unit,
    onTextChanged: (text: String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = text,
        onValueChange = onTextChanged,
        textStyle = MaterialTheme.typography.body1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            cursorColor = MaterialTheme.colors.onSurface
        ),
        trailingIcon = { SpeechSubmitButton(onSubmitClicked) }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
private fun SpeechInputTextFieldPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputTextField("This is test input", {}, {})
    }
}
