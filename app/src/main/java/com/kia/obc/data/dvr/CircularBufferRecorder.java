package com.kia.obc.data.dvr;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CircularBufferRecorder {
    private static final String TAG = "DVR_Engine";
    private static long MAX_STORAGE_MB = 2048; // 2GB Loop
    private static int CLIP_DURATION_SEC = 60;

    private final Context context;
    private MediaRecorder mediaRecorder;
    private File currentFile;
    private final List<File> recordedFiles = new ArrayList<>();

    public CircularBufferRecorder(Context context) {
        this.context = context;
    }

    public void startRecording() {
        try {
            currentFile = createNewFile();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setVideoEncodingBitRate(5000000);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setOutputFile(currentFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            
            recordedFiles.add(currentFile);
            startRotationTimer();
        } catch (Exception e) {
            Log.e(TAG, "DVR Start Error: " + e.getMessage());
        }
    }

    private void startRotationTimer() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                rotateClip();
            }
        }, CLIP_DURATION_SEC * 1000, CLIP_DURATION_SEC * 1000);
    }

    private synchronized void rotateClip() {
        stopRecording();
        checkStorageLimit();
        startRecording();
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
            } catch (Exception e) {
                Log.e(TAG, "Stop Error: " + e.getMessage());
            }
        }
    }

    private void checkStorageLimit() {
        long currentSize = 0;
        for (File f : recordedFiles) currentSize += f.length();

        while (currentSize > MAX_STORAGE_MB * 1024 * 1024 && !recordedFiles.isEmpty()) {
            File oldest = recordedFiles.remove(0);
            if (oldest.delete()) {
                currentSize -= oldest.length();
            }
        }
    }

    private File createNewFile() {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        return new File(dir, "DVR_" + System.currentTimeMillis() + ".mp4");
    }

    public void markEvent() {
        Log.d(TAG, "Event triggered: saving current clip as permanent");
        if (currentFile != null) {
            File eventFile = new File(currentFile.getParent(), "EVENT_" + currentFile.getName());
            currentFile.renameTo(eventFile);
            recordedFiles.remove(currentFile);
            recordedFiles.add(eventFile);
            currentFile = eventFile;
        }
    }

    public void stop() {
        stopRecording();
        if (mediaRecorder != null) mediaRecorder.release();
    }
}
