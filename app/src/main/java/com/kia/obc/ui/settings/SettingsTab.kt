package com.kia.obc.ui.settings;

import androidx.compose.runtime.*;
import androidx.compose.material.*;
import androidx.compose.foundation.clickable;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.rememberScrollState;
import androidx.compose.foundation.verticalScroll;
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

    // ADAS Settings
    var adasEnabled by remember { mutableStateOf(true) }
    var adasSensitivity by remember { mutableStateOf(2) } // 1-3
    var laneKeepingEnabled by remember { mutableStateOf(false) }

    // DVR Settings
    var dvrResolution by remember { mutableStateOf("1080p") }
    var dvrLoopInterval by remember { mutableStateOf(5) } // minutes
    var dvrGForceSensitivity by remember { mutableStateOf(2) }

    // Radar Settings
    var radarEnabled by remember { mutableStateOf(true) }
    var radarRange by remember { mutableStateOf(100) } // meters
    var radarAlertVolume by remember { mutableStateOf(70) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
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
            Divider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(16.dp))

            // ADAS Section
            Text("Система ADAS", style = MaterialTheme.typography.h5, color = Color.Cyan)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Включить ADAS", color = Color.White)
                Switch(checked = adasEnabled, onCheckedChange = { adasEnabled = it })
            }
            if (adasEnabled) {
                Text("Чувствительность: $adasSensitivity", color = Color.White)
                Slider(value = adasSensitivity.toFloat(), onValueChange = { adasSensitivity = it.toInt() }, valueRange = 1f..3f, steps = 2)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Контроль полосы", color = Color.White)
                    Switch(checked = laneKeepingEnabled, onCheckedChange = { laneKeepingEnabled = it })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(16.dp))

            // DVR Section
            Text("Видеорегистратор", style = MaterialTheme.typography.h5, color = Color.Cyan)
            Text("Разрешение:", color = Color.White)
            Row {
                listOf("720p", "1080p", "2K", "4K").forEach { res ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(selected = (dvrResolution == res), onClick = { dvrResolution = res })
                        Text(res, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Text("Интервал записи: $dvrLoopInterval мин", color = Color.White)
            Slider(value = dvrLoopInterval.toFloat(), onValueChange = { dvrLoopInterval = it.toInt() }, valueRange = 1f..15f, steps = 14)
            Text("Чувствительность G-сенсора: $dvrGForceSensitivity", color = Color.White)
            Slider(value = dvrGForceSensitivity.toFloat(), onValueChange = { dvrGForceSensitivity = it.toInt() }, valueRange = 1f..5f, steps = 4)

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(16.dp))

            // Radar Section
            Text("Сигнатурный радар", style = MaterialTheme.typography.h5, color = Color.Cyan)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Включить радар", color = Color.White)
                Switch(checked = radarEnabled, onCheckedChange = { radarEnabled = it })
            }
            if (radarEnabled) {
                Text("Дистанция обнаружения: ${radarRange}м", color = Color.White)
                Slider(value = radarRange.toFloat(), onValueChange = { radarRange = it.toInt() }, valueRange = 50f..500f, steps = 9)
                Text("Громкость оповещений: $radarAlertVolume%", color = Color.White)
                Slider(value = radarAlertVolume.toFloat(), onValueChange = { radarAlertVolume = it.toInt() }, valueRange = 0f..100f, steps = 99)
            }

            Spacer(modifier = Modifier.height(32.dp))
            
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
