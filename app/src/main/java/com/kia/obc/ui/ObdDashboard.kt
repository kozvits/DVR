package com.kia.obc.ui;

import androidx.compose.runtime.*;
import androidx.compose.material.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.Alignment;
import com.kia.obc.domain.model.ObdData;
import com.kia.obc.domain.model.DashboardTab;
import com.kia.obc.ui.settings.SettingsTab;

@Composable
fun ObdDashboard(dataState: State<ObdData>, gpsState: State<Double>) {
    var selectedTab by remember { mutableStateOf(DashboardTab.MAIN) }
    val data = dataState.value
    val gpsSpeed = gpsState.value

    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = Color.Black) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Main", tint = Color.White) },
                    label = { Text("Main", color = Color.White) },
                    selected = selectedTab == DashboardTab.MAIN,
                    onClick = { selectedTab = DashboardTab.MAIN }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "OBD", tint = Color.White) },
                    label = { Text("OBD", color = Color.White) },
                    selected = selectedTab == DashboardTab.METRICS,
                    onClick = { selectedTab = DashboardTab.METRICS }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Videocam, contentDescription = "ADAS", tint = Color.White) },
                    label = { Text("ADAS", color = Color.White) },
                    selected = selectedTab == DashboardTab.ADAS,
                    onClick = { selectedTab = DashboardTab.ADAS }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Cams", tint = Color.White) },
                    label = { Text("Cams", color = Color.White) },
                    selected = selectedTab == DashboardTab.CAMS,
                    onClick = { selectedTab = DashboardTab.CAMS }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Set", tint = Color.White) },
                    label = { Text("Set", color = Color.White) },
                    selected = selectedTab == DashboardTab.SETTINGS,
                    onClick = { selectedTab = DashboardTab.SETTINGS }
                )
            }
        },
        backgroundColor = Color.Black
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                DashboardTab.MAIN -> MainTab(gpsSpeed)
                DashboardTab.METRICS -> MetricsTab(data)
                DashboardTab.ADAS -> ADASTab()
                DashboardTab.CAMS -> CamsTab()
                DashboardTab.SETTINGS -> SettingsTab()
            }
        }
    }
}

@Composable
fun MainTab(gpsSpeed: Double) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("GPS SPEED", style = MaterialTheme.typography.h6, color = Color.LightGray, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        MetricView("KM/H", String.format("%.1f", gpsSpeed), Color.Cyan)
    }
}

@Composable
fun MetricsTab(data: ObdData) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("OBD Telemetry", style = MaterialTheme.typography.h4, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
        MetricView("RPM", String.format("%.0f", data.rpm), Color.Green)
        MetricView("OBD Speed", String.format("%.0f km/h", data.vehicleSpeed), Color.Cyan)
        MetricView("Temp", String.format("%.1f °C", data.coolantTemp), Color.Red)
        MetricView("Fuel", String.format("%.1f %%", data.fuelLevel), Color.Yellow)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        MetricView("Inst. Fuel", String.format("%.2f L/h", data.instantFuel), Color.White)
        MetricView("Avg Fuel", String.format("%.2f L/100km", data.avgFuel), Color.White)
        MetricView("Total Fuel", String.format("%.2f L", data.totalFuel), Color.White)
        MetricView("Trip Dist", String.format("%.1f km", data.tripDistance), Color.White)
        MetricView("Trip Time", String.format("%.0f min", data.tripTime), Color.White)
        MetricView("ECU Volt", String.format("%.2f V", data.ecuVoltage), Color.Magenta)
    }
}

@Composable
fun ADASTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("ADAS Vision Feed", color = Color.White, style = MaterialTheme.typography.h5)
    }
}

@Composable
fun CamsTab() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("取締 Speed Cams BY", style = MaterialTheme.typography.h4, color = Color.Yellow, modifier = Modifier.padding(bottom = 16.dp))
        Text("Источники баз сигнатур:", style = MaterialTheme.typography.h6, color = Color.White)
        Text("• SpeedCamOnline.ru (PoliScan, RoadEye)", color = Color.LightGray)
        Text("• Speed-Control.by (Стационарные ГАИ)", color = Color.LightGray)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Статус: Загрузка координат...", color = Color.Cyan)
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

        },
        backgroundColor = Color.Black
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                DashboardTab.MAIN -> MainTab(data)
                DashboardTab.METRICS -> MetricsTab(data)
                DashboardTab.ADAS -> ADASTab()
                DashboardTab.CAMS -> CamsTab()
            }
        }
    }
}

@Composable
fun CamsTab() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("取締 Speed Cams BY", style = MaterialTheme.typography.h4, color = Color.Yellow, modifier = Modifier.padding(bottom = 16.dp))
        Text("Источники баз сигнатур:", style = MaterialTheme.typography.h6, color = Color.White)
        Text("• SpeedCamOnline.ru (PoliScan, RoadEye)", color = Color.LightGray)
        Text("• Speed-Control.by (Стационарные ГАИ)", color = Color.LightGray)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Статус: Загрузка координат...", color = Color.Cyan)
    }
}

        },
        backgroundColor = Color.Black
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                DashboardTab.MAIN -> MainTab(data)
                DashboardTab.METRICS -> MetricsTab(data)
                DashboardTab.ADAS -> ADASTab()
            }
        }
    }
}

@Composable
fun MainTab(data: ObdData) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        MetricView("RPM", String.format("%.0f", data.rpm), Color.Green)
        MetricView("Speed", String.format("%.0f km/h", data.vehicleSpeed), Color.Cyan)
        MetricView("Temp", String.format("%.1f °C", data.coolantTemp), Color.Red)
        MetricView("Fuel", String.format("%.1f %%", data.fuelLevel), Color.Yellow)
    }
}

@Composable
fun MetricsTab(data: ObdData) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Extended Metrics", style = MaterialTheme.typography.h4, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
        MetricView("Inst. Fuel", String.format("%.2f L/h", data.instantFuel), Color.White)
        MetricView("Avg Fuel", String.format("%.2f L/100km", data.avgFuel), Color.White)
        MetricView("Total Fuel", String.format("%.2f L", data.totalFuel), Color.White)
        MetricView("Trip Dist", String.format("%.1f km", data.tripDistance), Color.White)
        MetricView("Trip Time", String.format("%.0f min", data.tripTime), Color.White)
        MetricView("ECU Volt", String.format("%.2f V", data.ecuVoltage), Color.Magenta)
    }
}

@Composable
fun ADASTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        Text("ADAS Vision Feed", color = Color.White, style = MaterialTheme.typography.h5)
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
