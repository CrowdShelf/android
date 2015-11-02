package com.crowdshelf.app.io;

/**
 * Created by markus on 18.10.2015.
 */
public class ScannerEvent {
    private ScannerEventType scannerEventType;
    private String scannerObjectId;

    public ScannerEvent(ScannerEventType scannerEventType, String scannerObjectId) {
        this.scannerEventType = scannerEventType;
        this.scannerObjectId = scannerObjectId;
    }

    public ScannerEventType getScannerEventType() {
        return scannerEventType;
    }

    public String getScannerObjectId() {
        return scannerObjectId;
    }
}
