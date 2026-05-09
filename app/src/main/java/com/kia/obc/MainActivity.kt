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
import java.util.ArrayList;
import java.util.List;

class MainActivity : ComponentActivity() {
    private val obdState = mutableStateOf(ObdData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    private val gpsState = mutableStateOf(0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start the brain of the app as a Foreground Service
        val serviceIntent = Intent(this, com.kia.obc.service.ObcMainService::class.java)
        startForegroundService(serviceIntent)

        setContent {
            var showDevicePicker by remember { mutableStateOf(true) }

            PermissionHandler {
                // Permissions granted
            }

            if (showDevicePicker) {
                val pairedDevices = BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                // Explicitly cast to java.util.List to satisfy the BluetoothDevicePicker signature
                val deviceList: java.util.List<BluetoothDevice> = ArrayList(pairedDevices)
                
                BluetoothDevicePicker(deviceList) { device ->
                    showDevicePicker = false
                }
            } else {
                ObdDashboard(obdState, gpsState)
            }
        }
    }
}


            if (showDevicePicker) {
                val pairedDevices = BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                BluetoothDevicePicker(pairedDevices) { device ->
                    // Connection logic will be handled via Service binding in next iteration
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
