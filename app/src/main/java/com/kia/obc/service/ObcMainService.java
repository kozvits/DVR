package com.kia.obc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.lifecycle.LifecycleService;
import com.kia.obc.data.connectivity.BluetoothSocketManager;
import com.kia.obc.data.dvr.CircularBufferRecorder;
import com.kia.obc.data.protocol.ISO9141Handler;
import com.kia.obc.data.vision.ADASPipeline;

public class ObcMainService extends LifecycleService {
    private static final String TAG = "OBC_MainService";
    
    private BluetoothSocketManager btManager;
    private ISO9141Handler protocolHandler;
    private CircularBufferRecorder dvrEngine;
    private ADASPipeline adasPipeline;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeHardware();
    }

    private void initializeHardware() {
        try {
            btManager = new BluetoothSocketManager();
            protocolHandler = new ISO9141Handler(btManager);
            dvrEngine = new CircularBufferRecorder(this);
            // ADASPipeline init requires a TFLite model buffer from assets
            Log.d(TAG, "Hardware components initialized");
        } catch (Exception e) {
            Log.e(TAG, "Critical init failure: " + e.getMessage());
        }
    }

    public void startSystem() {
        // Start DVR loop
        dvrEngine.startRecording();
        
        // Start OBD polling in a separate thread/coroutine
        new Thread(() -> {
            while (true) {
                try {
                    // Implement polling loop using ObdSafeModeManager
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (btManager != null) btManager.disconnect();
        if (dvrEngine != null) dvrEngine.stop();
        Log.d(TAG, "System shutdown: Hardware released");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
