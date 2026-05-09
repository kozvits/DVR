package com.kia.obc.domain.model;

public class ObdData {
    public final double rpm;
    public final double coolantTemp;
    public final double vehicleSpeed;
    public final double fuelLevel;
    public final long timestamp;

    public ObdData(double rpm, double coolantTemp, double vehicleSpeed, double fuelLevel) {
        this.rpm = rpm;
        this.coolantTemp = coolantTemp;
        this.vehicleSpeed = vehicleSpeed;
        this.fuelLevel = fuelLevel;
        this.timestamp = System.currentTimeMillis();
    }
}
