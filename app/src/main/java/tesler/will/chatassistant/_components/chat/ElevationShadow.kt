package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule

@Composable
fun ElevationShadow() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height((0.75).dp)
            .background(MaterialTheme.colors.secondary)
    )

    val overflowHeight = (6.5).dp
    val shadowColor = Color(0x90000000)

    OverflowShadow(
        modifier = Modifier
            .height(overflowHeight)
            .offset(0.dp, -overflowHeight),
        startColor = Color.Transparent,
        endColor = shadowColor
    )
}

@Preview
@Composable
private fun ElevationShadowPreview() {
    Previews.Wrap(mainTestModule, true) {
        ElevationShadow()
    }
}
