package com.crowdshelf.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Barcode Result";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view

        ArrayList<BarcodeFormat> supportedBarCodeFormats = new ArrayList<>();
        supportedBarCodeFormats.add(BarcodeFormat.EAN_13);

        mScannerView.setFormats(supportedBarCodeFormats);

        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        System.out.println("handleResult");
        //Log.v(TAG, rawResult.getText()); // Prints scan results
        //Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        String ISBN = rawResult.getText(); // Stores the ISBN in a variable
        Toast.makeText(getApplicationContext(), "Book scanned: " + ISBN, Toast.LENGTH_SHORT).show();

        super.onBackPressed();
        MainActivity.bookScanned(ISBN);

//        Intent intent = new Intent(this, MainActivity.class);
//
//        intent.putExtra("ISBN", ISBN);
//
//        startActivity(intent); // Return to start page
    }
}
