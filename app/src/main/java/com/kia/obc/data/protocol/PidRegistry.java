package com.kia.obc.data.protocol;

import java.util.HashMap;
import java.util.Map;

public class PidRegistry {
    public static class PidDefinition {
        public final byte[] command;
        public final String description;
        public final double scale;
        public final double offset;

        public PidDefinition(byte[] command, String description, double scale, double offset) {
            this.command = command;
            this.description = description;
            this.scale = scale;
            this.offset = offset;
        }
    }

    private static final Map<String, PidDefinition> PIDS = new HashMap<>();

    static {
        // Standard OBD-II PIDs for KIA Rio 2001 (KWP2000/ISO 9141-2)
        PIDS.put("RPM", new PidDefinition(new byte[]{0x01, 0x0C}, "Engine RPM", 0.25, 0));
        PIDS.put("SPEED", new PidDefinition(new byte[]{0x01, 0x0D}, "Vehicle Speed", 1.0, 0));
        PIDS.put("COOLANT", new PidDefinition(new byte[]{0x01, 0x05}, "Coolant Temp", 1.0, -40));
        PIDS.put("FUEL", new PidDefinition(new byte[]{0x01, 0x2F}, "Fuel Level", 100.0/255.0, 0));
        
        // Added parameters
        PIDS.put("INST_FUEL", new PidDefinition(new byte[]{0x01, 0x5C}, "Instant Fuel Consumption", 0.01, 0)); 
        PIDS.put("AVG_FUEL", new PidDefinition(new byte[]{0x01, 0x5D}, "Average Fuel Consumption", 0.01, 0));
        PIDS.put("FUEL_TOTAL", new PidDefinition(new byte[]{0x01, 0x5E}, "Total Fuel Used", 1.0, 0));
        PIDS.put("TRIP_DIST", new PidDefinition(new byte[]{0x01, 0x1F}, "Trip Distance", 1.0, 0));
        PIDS.put("TRIP_TIME", new PidDefinition(new byte[]{0x01, 0x20}, "Trip Time", 1.0, 0));
        PIDS.put("ECU_VOLT", new PidDefinition(new byte[]{0x01, 0x42}, "Control Unit Voltage", 0.1, 0));
    }

    public static PidDefinition getPid(String key) {
        return PIDS.get(key);
    }
}
