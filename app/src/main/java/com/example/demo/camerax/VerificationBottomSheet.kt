package com.example.demo.camerax

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amplifyframework.ui.liveness.ui.FaceLivenessDetector
import com.amplifyframework.ui.liveness.ui.LivenessColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationBottomSheet(
    checkComplete: () -> Unit,
    checkError: () -> Unit,
    onDismiss: () -> Unit,
    region: String,
    sessionId: String,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        windowInsets = WindowInsets.statusBars,
        dragHandle = null,
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            MaterialTheme(
                colorScheme = LivenessColorScheme.Defaults.darkColorScheme
            ) {
                FaceLivenessDetector(
                    sessionId = sessionId,
                    region = region,
                    onComplete = {
                        Log.d("TTT", "configureFlutterEngine: success 1")
                        // The Face Liveness flow is complete and the session
                        // results are ready. Use your backend to retrieve the
                        // results for the Face Liveness session.
                        checkComplete()
                    },
                    onError = { error ->
                        Log.e("MyApp", "Error during Face Liveness flow", error.throwable)

                        // An error occurred during the Face Liveness flow, such as
                        // time out or missing the required permissions.
                        checkError()
                    }
                )
            }

        }
    }
}