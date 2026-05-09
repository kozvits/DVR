package com.kia.obc.ui;

import androidx.compose.runtime.Composable;
import androidx.compose.runtime.State;
import androidx.compose.material.Text;
import androidx.compose.material.MaterialTheme;
import androidx.compose.foundation.layout.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.graphics.Color;
import com.kia.obc.domain.model.ObdData;

@Composable
fun ObdDashboard(dataState: State<ObdData>) {
    val data = dataState.value
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // High-contrast layout for automotive visibility
        MetricView("RPM", String.format("%.0f", data.rpm), Color.Green)
        MetricView("Speed", String.format("%.0f km/h", data.vehicleSpeed), Color.Cyan)
        MetricView("Temp", String.format("%.1f °C", data.coolantTemp), Color.Red)
        MetricView("Fuel", String.format("%.1f %%", data.fuelLevel), Color.Yellow)
    }
}

@Composable
fun MetricView(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.h5, color = Color.White)
        Text(text = value, style = MaterialTheme.typography.h4, color = color)
    }
}
