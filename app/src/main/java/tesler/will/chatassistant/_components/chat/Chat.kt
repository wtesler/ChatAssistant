package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant._components.markdown.MarkdownText
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.ChatModel.State.CREATED
import tesler.will.chatassistant.chat.ChatModel.State.ERROR
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun Chat(modifier: Modifier, chatModel: ChatModel) {
    var backgroundColor = Color.Transparent
    if (chatModel.state == ERROR) {
        backgroundColor = AppTheme.colors.chatError
    } else if (chatModel.state == CREATED) {
        if (chatModel.isUser) {
            backgroundColor = AppTheme.colors.chatPrimary
        } else {
            backgroundColor = AppTheme.colors.chatSecondary
        }
    }

    val hPadding = AppTheme.dimens.xlarge
    val vPadding = AppTheme.dimens.xlarge

    if (chatModel.text.isNotBlank()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(backgroundColor)
                .padding(hPadding, vPadding),
            contentAlignment = Center
        ) {
            MarkdownText(
                modifier = Modifier.wrapContentWidth(),
                markdown = chatModel.text,
                style = AppTheme.type.body1
            )
        }
    }
}

@Preview
@Composable
private fun ChatPreview() {
    Previews.Wrap(true) {
        Chat(
            Modifier,
            ChatModel(
                "As an AI language model, I don't have feelings or emotions",
                ChatModel.State.CREATED
            )
        )
    }
}
