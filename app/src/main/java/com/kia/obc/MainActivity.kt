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

class MainActivity : ComponentActivity() {
    private val obdState = mutableStateOf(ObdData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    private val gpsState = mutableStateOf(0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(this, com.kia.obc.service.ObcMainService::class.java)
        startForegroundService(serviceIntent)

        setContent {
            var showDevicePicker by remember { mutableStateOf(true) }

            PermissionHandler {
                // Permissions granted
            }

            if (showDevicePicker) {
                val pairedDevices = BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                
                BluetoothDevicePicker(pairedDevices) { device ->
                    showDevicePicker = false
                }
            } else {
                ObdDashboard(obdState, gpsState)
            }
        }
    }
}


            if (showDevicePicker) {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val pairedDevices = bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
                
                // Manually create a java.util.List to avoid Kotlin List mismatch in Java-defined UI components
                val deviceList = java.util.ArrayList<BluetoothDevice>()
                deviceList.addAll(pairedDevices)
                
                BluetoothDevicePicker(deviceList) { device ->
                    showDevicePicker = false
                }
            } else {
                ObdDashboard(obdState, gpsState)
            }
        }
    }
}
