package tesler.will.chatassistant.window

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class WindowHelper {
    companion object {
        fun getNavBarHeightDp(context: Context): Dp {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val heightPx: Int

            if (Build.VERSION.SDK_INT >= 30) {
                heightPx = windowManager
                    .currentWindowMetrics
                    .windowInsets
                    .getInsets(android.view.WindowInsets.Type.navigationBars())
                    .bottom
            } else {
                @Suppress("DEPRECATION")
                val currentDisplay = windowManager.defaultDisplay
                val appUsableSize = Point()
                val realScreenSize = Point()
                currentDisplay?.apply {
                    @Suppress("DEPRECATION")
                    getSize(appUsableSize)
                    @Suppress("DEPRECATION")
                    getRealSize(realScreenSize)
                }

                if (appUsableSize.x < realScreenSize.x) {
                    // navigation bar on the side
                    heightPx = realScreenSize.x - appUsableSize.x
                } else if (appUsableSize.y < realScreenSize.y)  {
                    heightPx = realScreenSize.y - appUsableSize.y
                } else {
                    heightPx = 0
                }
            }

            return (heightPx / context.resources.displayMetrics.density).dp
        }
    }
}
