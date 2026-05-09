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
                // We start the service only after permissions are granted to avoid SecurityExceptions
                LaunchedEffect(Unit) {
                    val serviceIntent = Intent(this@MainActivity, com.kia.obc.service.ObcMainService::class.java)
                    startForegroundService(serviceIntent)
                }
                
                ObdDashboard(obdState, gpsState) { device ->
                    // Connect via service
                    val serviceIntent = Intent(this@MainActivity, com.kia.obc.service.ObcMainService::class.java)
                    // To call a method in service, we'd ideally bind to it. For now, we use an Intent action.
                    serviceIntent.action = "ACTION_CONNECT_OBD"
                    serviceIntent.putExtra("device_extra", device)
                    startService(serviceIntent)
                }
            } else {
                Box(modifier = androidx.compose.ui.Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Ожидание разрешений...", color = androidx.compose.ui.graphics.Color.White)
                }
            }
        }
    }
}


            if (permissionsGranted) {
                if (showDevicePicker) {
                    val pairedDevices = remember {
                        BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                    }
                    
                    BluetoothDevicePicker(pairedDevices) { device ->
                        showDevicePicker = false
                    }
                } else {
                    ObdDashboard(obdState, gpsState)
                }
            } else {
                Box(modifier = androidx.compose.ui.Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Ожидание разрешений...", color = androidx.compose.ui.graphics.Color.White)
                }
            }
        }

        // Start service in a separate thread to avoid blocking UI,’ 
        // but usually, we'd do this after permissions are granted.
        val serviceIntent = Intent(this, com.kia.obc.service.ObcMainService::class.java)
        startForegroundService(serviceIntent)
    }
}


            if (permissionsGranted) {
                if (showDevicePicker) {
                    val pairedDevices = remember {
                        BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                    }
                    
                    BluetoothDevicePicker(pairedDevices) { device ->
                        showDevicePicker = false
                    }
                } else {
                    ObdDashboard(obdState, gpsState)
                }
            } else {
                // Экран ожидания разрешений
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Ожидание разрешений...", color = androidx.compose.ui.graphics.Color.White)
                }
            }
        }

        // Запуск сервиса только после того, как приложение будет готово
        // В реальном приложении лучше переместить это в callback разрешений
        val serviceIntent = Intent(this, com.kia.obc.service.ObcMainService::class.java)
        startForegroundService(serviceIntent)
    }
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
