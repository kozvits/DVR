package com.kia.obc.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import com.kia.obc.data.connectivity.BluetoothSocketManager;
import com.kia.obc.data.dvr.CircularBufferRecorder;
import com.kia.obc.data.protocol.ISO9141Handler;
import com.kia.obc.data.vision.ADASPipeline;
import com.kia.obc.data.location.GpsManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class ObcMainService extends LifecycleService {
    private static final String TAG = "OBC_MainService";
    private static final String CHANNEL_ID = "OBC_Channel";

    private BluetoothSocketManager btManager;
    private ISO9141Handler protocolHandler;
    private CircularBufferRecorder dvrEngine;
    private ADASPipeline adasPipeline;
    private GpsManager gpsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, getNotification());
        initializeHardware();
    }

    private void initializeHardware() {
        btManager = new BluetoothSocketManager();
        protocolHandler = new ISO9141Handler(btManager);
        dvrEngine = new CircularBufferRecorder(this);
        gpsManager = new GpsManager();
        
        // Start GPS tracking automatically as GPS and ADAS depend on it
        gpsManager.startTracking(this, location -> {
            Log.d(TAG, "GPS Update: " + location.getSpeed());
            // Here we would update a StateFlow/LiveEvent for the UI
        });

        dvrEngine.startRecording();
    }

    public void connectToDevice(BluetoothDevice device) {
        btManager.connect(device, new BluetoothSocketManager.ConnectionCallback() {
            @Override
            public void onConnected() {
                Log.d(TAG, "Connected to OBD!");
                protocolHandler.performHandshake(new ISO9141Handler.HandshakeCallback() {
                    @Override public void onSuccess() { Log.d(TAG, "KWP2000 Handshake OK"); }
                    @Override public void onFailure(Exception e) { Log.e(TAG, "Handshake failed"); }
                });
            }
            @Override public void onDisconnected(Exception e) { Log.e(TAG, "Disconnected"); }
            @Override public void onDataReceived(byte[] data) { /* Parse data and update UI */ }
        });
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
            CHANNEL_ID, "OBC Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) manager.createNotificationChannel(serviceChannel);
    }

    private Notification getNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("KIA Rio OBC Active")
            .setContentText("Monitoring Engine, GPS and ADAS...")
            .setSmallIcon(android.R.drawable.ic_menu_directions)
            .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (btManager != null) btManager.disconnect();
        if (dvrEngine != null) dvrEngine.stop();
        if (gpsManager != null) gpsManager.stopTracking();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
