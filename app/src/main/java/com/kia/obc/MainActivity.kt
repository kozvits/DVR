package com.kia.obc;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import androidx.compose.runtime.mutableStateOf;
import androidx.compose.runtime.State;
import com.kia.obc.domain.model.ObdData;
import com.kia.obc.ui.ObdDashboard;
import com.kia.obc.ui.PermissionHandler;

class MainActivity : ComponentActivity() {
    private val obdState = mutableStateOf(ObdData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    private val gpsState = mutableStateOf(0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandler {
                // Start service and bind logic here
            }
            ObdDashboard(obdState, gpsState)
        }
    }
}
