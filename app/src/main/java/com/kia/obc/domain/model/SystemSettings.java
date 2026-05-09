package com.kia.obc.domain.model;

public class SystemSettings {
    public static class SpeedCamSettings {
        public boolean autoUpdate = true;
        public String region = "RF";
        public int alertDistanceMeters = 500;
        public boolean monitorStationary = true;
        public boolean monitorMobile = true;
        public boolean monitorAverageSpeed = true;
        public int repeatIntervalSec = 10;
        public float gpsFilterThreshold = 5.0f;
        public String voiceProfile = "default_ru";
        public boolean volumeAdaptiveToSpeed = true;
    }

    public static class DvrSettings {
        public String resolution = "1080p";
        public int fps = 30;
        public int cycleDurationMin = 3;
        public boolean saveOnAdasEvent = true;
        public boolean saveOnSpeedCamEvent = true;
        public float gSensorSensitivity = 1.5f;
        public boolean showSpeedOsd = true;
        public boolean showLimitOsd = true;
        public boolean autoFormatStorage = true;
    }

    public static class AdasSettings {
        public float laneSensitivity = 0.7f;
        public float laneDepartureAngle = 15.0f;
        public int minLdwSpeedKmh = 60;
        public String fcwDistanceMode = "Medium"; // Short, Medium, Long
        public boolean detectPedestrians = true;
        public boolean ignoreRoadMarkers = true;
        public String visualStyle = "Skeleton"; // Skeleton, Filled, Minimal
        public boolean aggressiveAlertOnOverSpeed = true;
    }
}
