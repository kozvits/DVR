package com.kia.obc.data.protocol;

import android.util.Log;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ObdSafeModeManager {
    private static final String TAG = "OBD_SafeMode";
    private static final long MIN_REQUEST_INTERVAL_MS = 50; // Max 20 requests/sec to avoid ECU flood
    
    private final AtomicLong lastRequestTime = new AtomicLong(0);
    private final PriorityBlockingQueue<ObdRequest> requestQueue = new PriorityBlockingQueue<>();

    public static class ObdRequest implements Comparable<ObdRequest> {
        public final String pidKey;
        public final int priority; // Lower is higher priority
        public final Runnable action;

        public ObdRequest(String pidKey, int priority, Runnable action) {
            this.pidKey = pidKey;
            this.priority = priority;
            this.action = action;
        }

        @Override
        public int compareTo(ObdRequest other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public void enqueueRequest(ObdRequest request) {
        requestQueue.offer(request);
    }

    public void processNext() {
        ObdRequest request = requestQueue.poll();
        if (request == null) return;

        long now = System.currentTimeMillis();
        long timeSinceLast = now - lastRequestTime.get();

        if (timeSinceLast < MIN_REQUEST_INTERVAL_MS) {
            try {
                Thread.sleep(MIN_REQUEST_INTERVAL_MS - timeSinceLast);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lastRequestTime.set(System.currentTimeMillis());
        request.action.run();
    }
}
