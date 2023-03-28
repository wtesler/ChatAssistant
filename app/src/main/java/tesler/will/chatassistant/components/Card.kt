package tesler.will.chatassistant.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Card() {
    var state by remember { mutableStateOf(State.RECEIVING_INPUT) }
    var text by remember { mutableStateOf("") }

    val shape = RoundedCornerShape(MaterialTheme.spacing.large)

    fun onText(s: String) {
        text = s
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(0.dp)
            .border(BorderStroke(0.dp, Color.Black), shape)
            .clip(shape),
        color = MaterialTheme.colors.surface
    ) {
        when (state) {
            State.RECEIVING_INPUT -> ReceivingInput({ state = State.SENDING_INPUT }, ::onText)
            State.SENDING_INPUT -> SendingInput(text, { state = State.PLAYING_RESPONSE }, ::onText)
            State.PLAYING_RESPONSE -> PlayingResponse { state = State.RECEIVING_INPUT }
        }
    }
}

enum class State {
    RECEIVING_INPUT,
    SENDING_INPUT,
    PLAYING_RESPONSE
}
