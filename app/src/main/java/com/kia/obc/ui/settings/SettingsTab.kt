package com.kia.obc.ui.settings;

import androidx.compose.runtime.*;
import androidx.compose.material.*;
import androidx.compose.foundation.clickable;
import androidx.compose.foundation.layout.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.graphics.Color;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import com.kia.obc.ui.BluetoothDevicePicker;

@Composable
fun SettingsTab(onConnectObd: (BluetoothDevice) -> Unit) {
    var useSpeedCamOnline by remember { mutableStateOf(true) }
    var useSpeedControl by remember { mutableStateOf(false) }
    var showBtPicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Настройки", style = MaterialTheme.typography.h4, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Источники сигнатур:", color = Color.LightGray)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("SpeedCamOnline.ru", color = Color.White)
                Checkbox(checked = useSpeedCamOnline, onCheckedChange = { useSpeedCamOnline = it })
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Speed-Control.by", color = Color.White)
                Checkbox(checked = useSpeedControl, onCheckedChange = { useSpeedControl = it })
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { showBtPicker = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
            ) {
                Text("Выбрать OBD-II адаптер", color = Color.White)
            }
        }

        if (showBtPicker) {
            Box(
                modifier = Modifier.fillMaxSize().clickable { showBtPicker = false },
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    color = Color.Black,
                    elevation = 8.dp
                ) {
                    Column {
                        TextButton(onClick = { showBtPicker = false }) {
                            Text("Закрыть", color = Color.Red)
                        }
                        val pairedDevices = remember {
                            BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                        }
                        BluetoothDevicePicker(pairedDevices) { device ->
                            onConnectObd(device)
                            showBtPicker = false
                        }
                    }
                }
            }
        }
    }
}

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Speed-Control.by", color = Color.White)
                Checkbox(checked = useSpeedControl, onCheckedChange = { useSpeedControl = it })
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { showBtPicker = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
            ) {
                Text("Выбрать OBD-II адаптер", color = Color.White)
            }
        }

        if (showBtPicker) {
            Box(
                modifier = Modifier.fillMaxSize().clickable { showBtPicker = false },
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    color = Color.Black,
                    elevation = 8.dp
                ) {
                    Column {
                        TextButton(onClick = { showBtPicker = false }) {
                            Text("Закрыть", color = Color.Red)
                        }
                        val pairedDevices = remember {
                            BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.toList() ?: emptyList()
                        }
                        BluetoothDevicePicker(pairedDevices) { device ->
                            onConnectObd(device)
                            showBtPicker = false
                        }
                    }
                }
            }
        }
    }
}
