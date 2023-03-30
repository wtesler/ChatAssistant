package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.preview.Previews
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Chat(chatModel: ChatModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp, 0.dp, 0.dp, MaterialTheme.spacing.xxlarge),
        contentAlignment = Center
    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth(),
            text = chatModel.text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Preview
@Composable
fun ChatPreview() {
    Previews.Wrap(true) {
        Chat(ChatModel("Hi, how can I help?", ChatModel.State.CREATED))
    }
}
