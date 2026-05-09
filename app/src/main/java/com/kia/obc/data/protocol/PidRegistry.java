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
        // Using standard OBD-II placeholders; 2001 KIA Rio specific offsets 
        // may vary based on whether it's using KWP2000 or generic ISO 9141.
        PIDS.put("RPM", new PidDefinition(new byte[]{0x01, 0x0C}, "Engine RPM", 0.25, 0));
        PIDS.put("SPEED", new PidDefinition(new byte[]{0x01, 0x0D}, "Vehicle Speed", 1.0, 0));
        PIDS.put("COOLANT", new PidDefinition(new byte[]{0x01, 0x05}, "Coolant Temp", 1.0, -40));
        PIDS.put("FUEL", new PidDefinition(new byte[]{0x01, 0x2F}, "Fuel Level", 100.0/255.0, 0));
    }

    public static PidDefinition getPid(String key) {
        return PIDS.get(key);
    }
}
