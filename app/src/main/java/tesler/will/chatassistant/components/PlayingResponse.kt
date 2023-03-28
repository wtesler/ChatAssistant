package tesler.will.chatassistant.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun PlayingResponse(onFinished: ()->Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        Button(onClick = onFinished) {
            Text(text = "Playing Response")
        }
    }
}
