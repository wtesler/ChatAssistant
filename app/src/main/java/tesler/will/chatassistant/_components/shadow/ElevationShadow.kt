package tesler.will.chatassistant._components.shadow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule

@Composable
fun ElevationShadow(modifier: Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val overflowHeight = (10).dp
        val shadowColor = Color(0x45000000)

        OverflowShadow(
            modifier = Modifier
                .height(overflowHeight),
            startColor = Color.Transparent,
            endColor = shadowColor
        )
        
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height((0.75).dp)
                .background(MaterialTheme.colors.secondary)
        )
    }
}

@Preview
@Composable
private fun ElevationShadowPreview() {
    Previews.Wrap(mainTestModule, true) {
        ElevationShadow(Modifier)
    }
}
