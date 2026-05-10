package com.kia.obc;

import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import androidx.compose.runtime.*;
import androidx.compose.foundation.layout.Box;
import androidx.compose.foundation.layout.fillMaxSize;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.material.Text;
import androidx.compose.ui.graphics.Color;
import androidx.compose.material.CircularProgressIndicator;
import com.kia.obc.domain.model.ObdData;
import com.kia.obc.ui.ObdDashboard;
import com.kia.obc.ui.PermissionHandler;
import com.kia.obc.ui.BluetoothDevicePicker;

class MainActivity : ComponentActivity() {
    private val obdState = mutableStateOf(ObdData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    private val gpsState = mutableStateOf(0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var permissionsGranted by remember { mutableStateOf(false) }
            var showDevicePicker by remember { mutableStateOf(true) }

            PermissionHandler {
                permissionsGranted = true
            }

            if (permissionsGranted) {
                if (showDevicePicker) {
                    val pairedDevices = remember {
                        BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                    }
                    
                    BluetoothDevicePicker(pairedDevices) { device ->
                        // Start service only when device is selected
                        val serviceIntent = Intent(this@MainActivity, com.kia.obc.service.ObcMainService::class.java)
                        startForegroundService(serviceIntent)
                        showDevicePicker = false
                    }
                } else {
                    ObdDashboard(obdState, gpsState) { device ->
                        val serviceIntent = Intent(this@MainActivity, com.kia.obc.service.ObcMainService::class.java)
                        serviceIntent.action = "ACTION_CONNECT_OBD"
                        serviceIntent.putExtra("device_extra", device)
                        startService(serviceIntent)
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}
