package tesler.will.chatassistant.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import tesler.will.chatassistant.speech.ISpeechManager

@Composable
fun SendingInput() {
    val speechManager = koinInject<ISpeechManager>()

    var text by remember { mutableStateOf("") }

    val speechListener = remember {
        object: ISpeechManager.Listener {
            override fun onText(value: String?) {
                if (value != null) {
                    text = value
                }
            }
        }
    }

    DisposableEffect(Unit) {
        speechManager.addListener(speechListener)
        onDispose {
            speechManager.removeListener(speechListener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(

        ) {
            Text(text = text)
            Text(text = "\nSending Input...")
        }
    }
}
