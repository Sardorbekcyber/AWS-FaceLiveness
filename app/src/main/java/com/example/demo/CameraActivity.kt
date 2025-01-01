package com.example.demo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.AmplifyException
import com.example.demo.camerax.CameraPreviewScreen
import com.example.demo.camerax.OvalShape
import com.example.demo.camerax.VerificationBottomSheet

class CameraActivity : ComponentActivity() {

    private var renderCamera by mutableStateOf(false)
    private var showVerificationSheet by mutableStateOf(false)

    private var sessionId: String? = null
    private val region = "us-east-1"

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle Permission granted/rejected
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS && !it.value)
                permissionGranted = false
        }
        if (!permissionGranted) {
            Toast.makeText(
                baseContext,
                "Permission request denied",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            renderCamera = true
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data
            val bool = data?.getBooleanExtra("result", false)
            Log.i("MyApp", "Result code: ${result.resultCode}")
            Log.i("MyApp", "Bool $bool")
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Retrieve session_id from the intent
        sessionId =  "239823f89238912909"  //intent.getStringExtra("session_id")

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {
                // Camera permission already granted
                // Implement camera related code
                renderCamera = true
            }

            else -> {
                requestPermissions()
            }
        }

        try {
            // Add the Auth plugin
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("AmplifyInit", "Amplify initialized successfully")
        } catch (error: AmplifyException) {
            Log.e("AmplifyInit", "Failed to initialize Amplify", error)
        }


        // Display session_id as a Toast (for testing purposes)
        Toast.makeText(this, "Session ID: $sessionId", Toast.LENGTH_SHORT).show()


        setContent {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Verify via Photo",
                                lineHeight = 21.6.sp,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFAFAFA)
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color(0xFF111111)
                        )
                    )
                },

                containerColor = Color(0xFF111111),
                contentColor = Color(0xFFF1F1F1),
                modifier = Modifier
            ) {
                Box(modifier = Modifier.padding(it)) {
                    Divider(
                        color = Color(0xFF6A7370),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                    Spacer(Modifier.height(32.dp))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Visual Verification for Added \nSecurity",
                            lineHeight = 29.7.sp,
                            fontSize = 22.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xFFF1F1F1),
                            modifier = Modifier
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Upload a photo of yourself for visual verification, adding an extra layer of security to your account.",
                            lineHeight = 20.25.sp,
                            fontSize = 15.sp,
                            fontWeight = FontWeight(300),
                            color = Color(0xFFFAFAFA),
                            modifier = Modifier.alpha(0.8f)
                        )

                        Spacer(Modifier.height(64.dp))

                        if (renderCamera) {
                            CameraPreviewScreen(
                                modifier = Modifier
                                    .padding(horizontal = 64.dp)
                                    .clip(OvalShape())
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF6A7370),
                                        shape = OvalShape()
                                    )
                                    .weight(1f)
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }

                        Spacer(Modifier.height(64.dp))

                        Text(
                            text = "Make sure there’s enough light to take a clear photo.",
                            lineHeight = 21.6.sp,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight(300),
                            minLines = 2,
                            color = Color(0xFFFAFAFA),
                            modifier = Modifier
                                .padding(horizontal = 82.dp)
                                .alpha(0.8f)
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { showVerificationSheet = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB0B3B4),
                                contentColor = Color(0xFF111111),

                                disabledContainerColor = Color(0xFFB0B3B4).copy(alpha = 0.32f),
                                disabledContentColor = Color(0xFF111111)
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Verify",
                                lineHeight = 21.6.sp,
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                            )
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }

            if (showVerificationSheet) {
                VerificationBottomSheet(
                    checkComplete = ::checkComplete,
                    checkError = ::checkError,
                    onDismiss = { showVerificationSheet = false },
                    region = region,
                    sessionId = sessionId.orEmpty(),
                    modifier = Modifier
                )
            }
        }
    }

    private fun checkComplete() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", true)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun checkError() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", false)
        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }

    private fun requestPermissions() {
        permissionRequestLauncher.launch(REQUIRED_PERMISSIONS)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

}