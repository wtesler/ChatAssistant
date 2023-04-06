package tesler.will.chatassistant._components.shadow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule

@Composable
fun OverflowShadow(modifier: Modifier, startColor: Color, endColor: Color) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .drawWithCache {
                onDrawWithContent {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(startColor, endColor),
                    )

                    drawRect(
                        brush = gradient,
                        topLeft = Offset(0f, 0f),
                        size = size,
                    )
                }
            }
    )
}

@Preview
@Composable
private fun OverflowShadowPreview() {
    Previews.Wrap(mainTestModule, true) {
        OverflowShadow(Modifier.height(50.dp), Color.Red, Color.Blue)
    }
}
