package com.kia.obc.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.compose.rememberLauncherForActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.LaunchedEffect;
import androidx.compose.ui.platform.LocalContext;
import android.content.Context;
import androidx.core.content.ContextCompat;

@Composable
fun PermissionHandler(onPermissionsGranted: () -> Unit) {
    val context = LocalContext.current
    
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        boolean allGranted = true;
        for (grant : result.values()) {
            if (!grant) allGranted = false;
        }
        if (allGranted) onPermissionsGranted();
    }

    LaunchedEffect(Unit) {
        launcher.launch(permissions);
    }
}
