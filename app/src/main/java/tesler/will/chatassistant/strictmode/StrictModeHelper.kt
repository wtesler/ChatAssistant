package tesler.will.chatassistant.strictmode

import android.os.StrictMode
import android.os.strictmode.Violation
import android.util.Log
import java.util.concurrent.Executors

class StrictModeHelper {
    companion object {
        fun setup() {
            StrictMode.enableDefaults()
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAllExcept("onUntaggedSocket")
                    .build()
            )
        }

        private fun StrictMode.VmPolicy.Builder.detectAllExcept(ignoredViolation: String): StrictMode.VmPolicy.Builder {
            return detectAll()
                .penaltyListener(
                    Executors.newSingleThreadExecutor()
                ) {
                    it.filter(ignoredViolation)
                }
        }

        private fun Violation.filter(ignoredViolation: String) {
            val violationMethodName = stackTrace[0].methodName
            if (!violationMethodName.equals(ignoredViolation, true)) {
                for (item in stackTrace) {
                    Log.d("StrictMode", item.toString())
                }
            }
        }
    }
}
