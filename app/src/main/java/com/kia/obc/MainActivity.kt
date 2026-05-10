package com.kia.obc

import android.content.Intent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.material.CircularProgressIndicator
import com.kia.obc.domain.model.ObdData
import com.kia.obc.ui.ObdDashboard
import com.kia.obc.ui.PermissionHandler

class MainActivity : ComponentActivity() {
    private val obdState = mutableStateOf(ObdData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    private val gpsState = mutableStateOf(0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var permissionsGranted by remember { mutableStateOf(false) }

            PermissionHandler {
                permissionsGranted = true
            }

            if (permissionsGranted) {
                // Start service automatically after permissions granted
                val serviceIntent = Intent(this@MainActivity, com.kia.obc.service.ObcMainService::class.java)
                startForegroundService(serviceIntent)
                
                ObdDashboard(obdState, gpsState) { device ->
                    val serviceIntentConnect = Intent(this@MainActivity, com.kia.obc.service.ObcMainService::class.java)
                    serviceIntentConnect.action = "ACTION_CONNECT_OBD"
                    serviceIntentConnect.putExtra("device_extra", device)
                    startService(serviceIntentConnect)
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}
