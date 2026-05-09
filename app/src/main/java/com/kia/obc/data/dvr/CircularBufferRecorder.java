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
