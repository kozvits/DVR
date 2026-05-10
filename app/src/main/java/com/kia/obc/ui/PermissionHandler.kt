package com.kia.obc.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.compose.rememberLauncherForActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.LaunchedEffect;
import androidx.compose.runtime.getValue;
import androidx.compose.runtime.mutableStateOf;
import androidx.compose.runtime.setValue;
import androidx.compose.ui.platform.LocalContext;
import androidx.core.content.ContextCompat;

@Composable
fun PermissionHandler(onPermissionsGranted: () -> Unit) {
    val context = LocalContext.current
    
    // Track if we've already checked permissions to avoid infinite loop
    var permissionCheckTriggered by remember { mutableStateOf(false) }
    
    // Define all permissions we need to request
    val allPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        android.util.Log.d("PermissionHandler", "Permission result: $result")
        // Check if critical permissions are granted
        val criticalPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            listOf(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val allCriticalGranted = criticalPermissions.all { perm -> result[perm] == true }
        android.util.Log.d("PermissionHandler", "All critical granted: $allCriticalGranted")
        if (allCriticalGranted) {
            onPermissionsGranted()
        } else {
            // Even if not all permissions granted, allow user to proceed with limited functionality
            onPermissionsGranted()
        }
    }

    LaunchedEffect(Unit) {
        // Only trigger permission check once
        if (permissionCheckTriggered) return@LaunchedEffect
        permissionCheckTriggered = true
        
        // Define critical permissions based on API level
        val criticalPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            listOf(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Check if all critical permissions are already granted
        val alreadyGranted = criticalPermissions.all { 
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED 
        }

        android.util.Log.d("PermissionHandler", "Already granted: $alreadyGranted")
        
        if (alreadyGranted) {
            onPermissionsGranted()
        } else {
            try {
                launcher.launch(allPermissions)
            } catch (e: Exception) {
                android.util.Log.e("PermissionHandler", "Launch failed: ${e.message}")
                // If launch fails, still try to proceed - the app might work with limited functionality
                onPermissionsGranted() 
            }
        }
    }
}
