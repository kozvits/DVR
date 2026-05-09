package com.kia.obc.domain

import com.kia.obc.domain.model.ObdData
import com.kia.obc.domain.vision.VisionAlert

data class SystemState(
    val obdData: ObdData = ObdData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    val activeAlert: VisionAlert? = null,
    val isConnected: Boolean = false,
    val isRecording: Boolean = false,
    val batteryTemp: Float = 0.0f
)
