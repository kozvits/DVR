# KIA Rio (2001) OBC & ADAS System

Professional Android application providing On-Board Computing and Advanced Driver Assistance for 2001 KIA Rio.

## Technical Specifications
- **OBD Protocol**: ISO 9141-2 / KWP2000
- **Connection**: Bluetooth SPP
- **Vision**: TFLite GPU Accelerated LDW/FCW
- **DVR**: Circular Buffer Loop Recording

## Setup & Build
1. Clone this repository.
2. Open in Android Studio Flamingo or newer.
3. Add `adas_model.tflite` to `app/src/main/assets/`.
4. Build and deploy to an Android 13+ device.
