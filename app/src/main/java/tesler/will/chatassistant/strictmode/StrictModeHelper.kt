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
                    .detectAllExcept(listOf("onUntaggedSocket"))
                    .build()
            )
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAllExcept(listOf("onReadFromDisk"))
                    .build()
            )
        }

        private fun StrictMode.VmPolicy.Builder.detectAllExcept(ignoredViolations: List<String>): StrictMode.VmPolicy.Builder {
            return detectAll()
                .penaltyListener(
                    Executors.newSingleThreadExecutor()
                ) {
                    it.filter(ignoredViolations)
                }
        }

        private fun StrictMode.ThreadPolicy.Builder.detectAllExcept(ignoredViolations: List<String>): StrictMode.ThreadPolicy.Builder {
            return detectAll()
                .penaltyListener(
                    Executors.newSingleThreadExecutor()
                ) {
                    it.filter(ignoredViolations)
                }
        }

        private fun Violation.filter(ignoredViolations: List<String>) {
            val violationMethodName = stackTrace[0].methodName
            if (!ignoredViolations.contains(violationMethodName)) {
                for (item in stackTrace) {
                    Log.d("StrictMode", item.toString())
                }
                Log.d("StrictMode", "----------")
            }
        }
    }
}
