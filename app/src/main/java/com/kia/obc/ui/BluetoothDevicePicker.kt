package com.kia.obc.ui;

import androidx.compose.runtime.*;
import androidx.compose.material.*;
import androidx.compose.foundation.clickable;
import androidx.compose.foundation.layout.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.graphics.Color;
import android.bluetooth.BluetoothDevice;
import java.util.List;

@Composable
fun BluetoothDevicePicker(devices: List<BluetoothDevice>, onDeviceSelected: (BluetoothDevice) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Выберите OBD-II адаптер", style = MaterialTheme.typography.h5, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (devices.isEmpty()) {
            Text("Устройства не найдены...", color = Color.LightGray)
        } else {
            devices.forEach { device ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDeviceSelected(device) }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = device.name ?: "Unknown Device", color = Color.White)
                    Text(text = "Подключить", color = Color.Cyan)
                }
            }
        }
    }
}
