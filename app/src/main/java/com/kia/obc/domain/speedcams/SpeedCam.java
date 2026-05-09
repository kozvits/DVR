package com.kia.obc.domain.speedcams;

public class SpeedCam {
    public final double latitude;
    public final double longitude;
    public final String type; // e.g., "PoliScan", "RoadEye", "Stationary"
    public final String description;

    public SpeedCam(double latitude, double longitude, String type, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
    }
}
