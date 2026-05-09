package com.kia.obc.domain.vision;

public class VisionAlert {
    public enum AlertType {
        LANE_DEPARTURE,
        COLLISION_WARNING,
        DROWSINESS_DETECTED
    }

    public final AlertType type;
    public final float severity; // 0.0 to 1.0
    public final long timestamp;

    public VisionAlert(AlertType type, float severity) {
        this.type = type;
        this.severity = severity;
        this.timestamp = System.currentTimeMillis();
    }
}
