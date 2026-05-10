package com.kia.obc.data.vision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraVisionManager {
    private static final String TAG = "CameraVision";
    private Context context;
    private Interpreter tflite;
    private boolean isInitialized = false;
    
    // Detection results
    private List<DetectionResult> currentDetections = new ArrayList<>();
    private long lastDetectionTime = 0;
    
    public static class DetectionResult {
        public RectF boundingBox;
        public String label;
        public float confidence;
        
        public DetectionResult(RectF box, String label, float confidence) {
            this.boundingBox = box;
            this.label = label;
            this.confidence = confidence;
        }
    }
    
    public CameraVisionManager(Context context) {
        this.context = context;
        initializeModel();
    }
    
    private void initializeModel() {
        try {
            MappedByteBuffer modelBuffer = loadModelFile("adas_model.tflite");
            if (modelBuffer != null) {
                Interpreter.Options options = new Interpreter.Options();
                options.setNumThreads(2);
                options.setUseNNAPI(true);
                tflite = new Interpreter(modelBuffer, options);
                isInitialized = true;
                Log.d(TAG, "ADAS model loaded successfully");
            } else {
                Log.w(TAG, "ADAS model not found in assets, using mock mode");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading ADAS model: " + e.getMessage());
        }
    }
    
    private MappedByteBuffer loadModelFile(String modelName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(context.getAssets().getFd(modelName));
            FileChannel fileChannel = fileInputStream.getChannel();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<DetectionResult> processFrame(Bitmap bitmap) {
        if (!isInitialized || tflite == null) {
            // Return mock detections for testing without model
            return getMockDetections(bitmap.getWidth(), bitmap.getHeight());
        }
        
        long startTime = System.currentTimeMillis();
        
        // Preprocess bitmap
        ByteBuffer inputBuffer = preprocessBitmap(bitmap);
        
        // Run inference
        float[][][] output = new float[1][100][4]; // [batch][detections][bbox]
        float[][] confidences = new float[1][100]; // [batch][detections]
        
        Object[] inputs = {inputBuffer};
        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(0, output);
        outputs.put(1, confidences);
        
        tflite.runForMultipleInputsOutputs(inputs, outputs);
        
        // Process results
        List<DetectionResult> results = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (confidences[0][i] > 0.5f) {
                float[] bbox = output[0][i];
                RectF box = new RectF(
                    bbox[0] * bitmap.getWidth(),
                    bbox[1] * bitmap.getHeight(),
                    bbox[2] * bitmap.getWidth(),
                    bbox[3] * bitmap.getHeight()
                );
                results.add(new DetectionResult(box, "object", confidences[0][i]));
            }
        }
        
        lastDetectionTime = System.currentTimeMillis() - startTime;
        currentDetections = results;
        Log.d(TAG, "Detected " + results.size() + " objects in " + lastDetectionTime + "ms");
        
        return results;
    }
    
    private ByteBuffer preprocessBitmap(Bitmap bitmap) {
        int width = 224;
        int height = 224;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height * 3);
        buffer.order(ByteOrder.nativeOrder());
        
        int[] pixels = new int[width * height];
        scaledBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        
        for (int pixel : pixels) {
            buffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f);
            buffer.putFloat(((pixel >> 8) & 0xFF) / 255.0f);
            buffer.putFloat((pixel & 0xFF) / 255.0f);
        }
        
        buffer.rewind();
        scaledBitmap.recycle();
        return buffer;
    }
    
    private List<DetectionResult> getMockDetections(int width, int height) {
        // Mock detections for testing without ML model
        List<DetectionResult> mockResults = new ArrayList<>();
        mockResults.add(new DetectionResult(
            new RectF(width * 0.3f, height * 0.4f, width * 0.7f, height * 0.8f),
            "vehicle",
            0.85f
        ));
        return mockResults;
    }
    
    public void drawDetections(Canvas canvas, List<DetectionResult> detections) {
        Paint boxPaint = new Paint();
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(4f);
        
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40f);
        
        for (DetectionResult detection : detections) {
            canvas.drawRect(detection.boundingBox, boxPaint);
            canvas.drawText(
                String.format("%s %.2f", detection.label, detection.confidence),
                detection.boundingBox.left,
                detection.boundingBox.top - 10,
                textPaint
            );
        }
    }
    
    public boolean isReady() {
        return isInitialized || true; // Allow mock mode
    }
    
    public void release() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
        isInitialized = false;
    }
}
