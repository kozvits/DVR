package com.kia.obc.ui.settings;

import androidx.compose.runtime.Composable;
import androidx.compose.runtime.State;
import androidx.compose.runtime.mutableStateOf;
import androidx.compose.runtime.remember;
import androidx.compose.material.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.graphics.Color;

@Composable
fun SettingsScreen() {
    var useSpeedCamOnline by remember { mutableStateOf(true) }
    var useSpeedControlBy by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Настройки сигнатур", style = MaterialTheme.typography.h4, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = useSpeedCamOnline, onCheckedChange = { useSpeedCamOnline = it }, colors = CheckboxDefaults.colors(checkedColor = Color.Green))
            Text("SpeedCamOnline.ru (BY)", color = Color.White)
        }
        
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = useSpeedControlBy, onCheckedChange = { useSpeedControlBy = it }, colors = CheckboxDefaults.colors(checkedColor = Color.Green))
            Text("Speed-Control.by (Офиц. ГАИ)", color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("GPS Конфигурация", style = MaterialTheme.typography.h6, color = Color.LightGray)
        Text("Активен встроенный GPS для ADAS и Скорости", color = Color.Cyan)
    }
}
