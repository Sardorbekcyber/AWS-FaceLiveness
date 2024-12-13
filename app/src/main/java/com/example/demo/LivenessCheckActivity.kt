package com.example.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.amplifyframework.ui.liveness.ui.FaceLivenessDetector
import com.amplifyframework.ui.liveness.ui.LivenessColorScheme


class LivenessCheckActivity: ComponentActivity() {

    private val region = "us-east-1"
    private val sessionId = "fef3514c-45e2-40ef-9fb8-5c3e0fc1ef86"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = LivenessColorScheme.default()
            ) {
                FaceLivenessDetector(
                    sessionId = sessionId,
                    region = region,
                    onComplete = {
                        Log.i("MyApp", "Face Liveness flow is complete")
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

    private fun checkComplete() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", true)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun checkError() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", false)
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }

}
