package com.example.demo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    lateinit var cameraPermission: ActivityResultLauncher<String>
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            Log.i("MyApp", "Permission $isGranted")
        }
        cameraPermission.launch(Manifest.permission.CAMERA)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data
            val bool = data?.getBooleanExtra("result", false)
            Log.i("MyApp", "Result code: ${result.resultCode}")
            Log.i("MyApp", "Bool $bool")
        }

        setContent {
            MaterialTheme {
                Scaffold { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, LivenessCheckActivity::class.java)
                                activityResultLauncher.launch(intent)
                            },
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                                .width(180.dp)
                                .height(60.dp)
                        ) {
                            Text(
                                text = "Start verification"
                            )
                        }
                    }
                }
            }
        }
    }

}
