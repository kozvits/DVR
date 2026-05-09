package com.kia.obc.data.connectivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BluetoothSocketManager {
    private static final String TAG = "BT_SocketMgr";
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private final ExecutorService connectionExecutor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    public interface ConnectionCallback {
        void onConnected();
        void onDisconnected(Exception e);
        void onDataReceived(byte[] data);
    }

    public void connect(BluetoothDevice device, ConnectionCallback callback) {
        connectionExecutor.execute(() -> {
            try {
                socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(SPP_UUID));
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                isConnected.set(true);
                callback.onConnected();
                startReading(callback);
            } catch (IOException e) {
                Log.e(TAG, "Connection failed: " + e.getMessage());
                callback.onDisconnected(e);
            }
        });
    }

    private void startReading(ConnectionCallback callback) {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (isConnected.get()) {
                try {
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead > 0) {
                        byte[] data = new byte[bytesRead];
                        System.arraycopy(buffer, 0, data, 0, bytesRead);
                        callback.onDataReceived(data);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Read error: " + e.getMessage());
                    disconnect();
                    callback.onDisconnected(e);
                    break;
                }
            }
        }).start();
    }

    public void send(byte[] data) throws IOException {
        if (!isConnected.get() || outputStream == null) {
            throw new IOException("Not connected to device");
        }
        outputStream.write(data);
        outputStream.flush();
    }

    public void disconnect() {
        isConnected.set(false);
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Disconnect error: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return isConnected.get();
    }
}
