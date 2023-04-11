package tesler.will.chatassistant._components.speechinput.textinput

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.speechinput.submitbutton.SpeechSubmitButton
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun SpeechInputTextField(
    text: String,
    onSubmitClicked: () -> Unit,
    onTextChanged: (text: String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var textState by remember { mutableStateOf(TextFieldValue(text = text)) }
    val scope = rememberCoroutineScope()

    val TEXT_STYLE = AppTheme.type.body1

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = textState,
        onValueChange = { textState = it.copy(); onTextChanged(it.text) },
        textStyle = TEXT_STYLE,
        colors = TextFieldDefaults.textFieldColors(
            textColor = TEXT_STYLE.color,
            cursorColor = TEXT_STYLE.color,
            backgroundColor = AppTheme.colors.textFieldPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        trailingIcon = { SpeechSubmitButton(onSubmitClicked) },
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()

        scope.launch {
            if (text.isNotEmpty()) {
                textState = textState.copy(selection = TextRange(text.length, text.length))
            }
        }
    }
}

@Preview
@Composable
private fun SpeechInputTextFieldPreview() {
    Previews.Wrap(mainTestModule, true) {
        SpeechInputTextField("This is test input", {}, {})
    }
}
