package com.sublime.visionaid.android.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sublime.visionaid.android.ui.helpers.ActionType
import com.sublime.visionaid.android.ui.viewmodel.CameraViewModel
import com.sublime.visionaid.android.ui.widgets.CameraContent

@Suppress("ktlint:standard:function-naming")
@Composable
fun CameraScreen(
    isLoading: Boolean,
    onCaptureImage: (Uri, ActionType) -> Unit,
    onCaptureError: (String) -> Unit,
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel,
) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted -> hasCameraPermission = granted },
        )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            CameraContent(
                viewModel = cameraViewModel,
                isLoading = isLoading,
                onCaptureImage = onCaptureImage,
                onCaptureError = onCaptureError,
            )
        } else {
            NoPermissionScreen(
                onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun NoPermissionScreen(onRequestPermission: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            "Camera permission is required to use this feature",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Camera Permission")
        }
    }
}
