package com.kia.obc;

import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import androidx.compose.runtime.*;
import com.kia.obc.domain.model.ObdData;
import com.kia.obc.ui.ObdDashboard;
import com.kia.obc.ui.PermissionHandler;
import com.kia.obc.ui.BluetoothDevicePicker;
import java.util.Set;

class MainActivity : ComponentActivity() {
    private val obdState = mutableStateOf(ObdData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    private val gpsState = mutableStateOf(0.0)
    private var isDeviceSelected by remember { mutableStateOf(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start the brain of the app as a Foreground Service
        Intent serviceIntent = Intent(this, com.kia.obc.service.ObcMainService::class.java)
        startForegroundService(serviceIntent)

        setContent {
            var showDevicePicker by remember { mutableStateOf(true) }

            PermissionHandler {
                // Permissions granted, can now scan and track
            }

            if (showDevicePicker) {
                val pairedDevices = BluetoothAdapter.getDefaultAdapter()?.bondedDevices ?: emptySet()
                BluetoothDevicePicker(pairedDevices.toList()) { device ->
                    // Connect to the selected ELM327
                    val service = getSystemService(com.kia.obc.service.ObcMainService::class.java) // This needs corrected binding
                    // Simulating connection for now
                    showDevicePicker = false
                }
            } else {
                ObdDashboard(obdState, gpsState)
            }
        }
    }
}

            ObdDashboard(obdState, gpsState)
        }
    }
}
