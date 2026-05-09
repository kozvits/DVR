package com.kia.obc.ui.settings;

import androidx.compose.runtime.*;
import androidx.compose.material.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.graphics.Color;

@Composable
fun SettingsTab() {
    var useSpeedCamOnline by remember { mutableStateOf(true) }
    var useSpeedControl by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Настройки сигнатур", style = MaterialTheme.typography.h4, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Выберите источники данных:", color = Color.LightGray)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("SpeedCamOnline.ru", color = Color.White)
            Checkbox(checked = useSpeedCamOnline, onCheckedChange = { useSpeedCamOnline = it })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Speed-Control.by", color = Color.White)
            Checkbox(checked = useSpeedControl, onCheckedChange = { useSpeedControl = it })
        }
    }
}
