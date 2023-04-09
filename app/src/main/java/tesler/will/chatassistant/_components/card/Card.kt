package tesler.will.chatassistant._components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.chat.ChatSection
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.speechinput.SpeechInputSectionResolver
import tesler.will.chatassistant.modifiers.noRippleClickable
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.ui.theme.spacing
import tesler.will.chatassistant.window.WindowHelper

@Composable
fun Card(defaultVisible: Boolean = false) {
    val ENTRANCE_ANIM_SPEED_MS = 350

    val context = LocalContext.current

    var visible by remember { mutableStateOf(defaultVisible) }

    val navBarHeight = remember { WindowHelper.getNavBarHeightDp(context) }

    val shape =
        RoundedCornerShape(MaterialTheme.spacing.large, MaterialTheme.spacing.large, 0.dp, 0.dp)

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible,
        enter = slideInVertically(
            tween(ENTRANCE_ANIM_SPEED_MS, easing = LinearOutSlowInEasing),
            initialOffsetY = { fullHeight -> fullHeight }
        )
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .fillMaxWidth()
                .widthIn(Dp.Unspecified, 500.dp)
                .wrapContentHeight()
                .noRippleClickable {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colors.surface)
                    .padding(0.dp, 0.dp, 0.dp, navBarHeight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Box(
                        modifier = Modifier.weight(1f, false)
                    ) {
                        ChatSection()
                    }

                    Box(
                        modifier = Modifier
                    ) {
                        SpeechInputSectionResolver()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CardPreview() {
    Previews.Wrap(mainTestModule, true) {
        Card(true)
    }
}
