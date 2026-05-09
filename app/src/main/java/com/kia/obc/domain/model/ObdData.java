package com.kia.obc.domain.model;

public class ObdData {
    public final double rpm;
    public final double coolantTemp;
    public final double vehicleSpeed;
    public final double fuelLevel;
    public final double instantFuel;
    public final double avgFuel;
    public final double totalFuel;
    public final double tripDistance;
    public final double tripTime;
    public final double ecuVoltage;
    public final long timestamp;

    public ObdData(double rpm, double coolantTemp, double vehicleSpeed, double fuelLevel, 
                  double instantFuel, double avgFuel, double totalFuel, 
                  double tripDistance, double tripTime, double ecuVoltage) {
        this.rpm = rpm;
        this.coolantTemp = coolantTemp;
        this.vehicleSpeed = vehicleSpeed;
        this.fuelLevel = fuelLevel;
        this.instantFuel = instantFuel;
        this.avgFuel = avgFuel;
        this.totalFuel = totalFuel;
        this.tripDistance = tripDistance;
        this.tripTime = tripTime;
        this.ecuVoltage = ecuVoltage;
        this.timestamp = System.currentTimeMillis();
    }
}
