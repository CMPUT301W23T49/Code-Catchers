/**
 * The ScannerActivity class contains methods for
 * scanning QR code related operations
 * @author [Kyle Karpyshyn]
 * @version 1.0
 * @since [Monday March 13 2021]
 */
package com.example.codecatchersapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * UserAccountActivity is an extends AppCompatActivity.
 */
public class ScannerActivity extends AppCompatActivity {
    // ERROR HANDLING VARS
    private static final String TAG = "ScannerActivity";
    private static final int REQUEST_CODE = 100; // https://stackoverflow.com/questions/38507965/what-does-camera-request-code-mean-in-android

    // CODE SCANNING VARS
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;

    // EXTRA VARS
    private String qrCodeValue;
    private Bitmap qrBitmap;

    /**
     * Called when the activity is starting.
     * Connects to the scanner.xml and sets it as the content view.
     * Requests the user for permissions
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        Intent intent = getIntent();

        scannerView = findViewById(R.id.scanner_view);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    REQUEST_CODE);
        } else {
            // Permissions already granted, start
            startScanner();
        }
    }

    /**
     * When called starts scanning for qr codes
     * on successful scan:
     * convert the qr code value into a string and pass the value to ScoreRevealActivity
     * on failed scan:
     * Swap intent to ScanErrorActivity
     */
    private void startScanner() {
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPause();

                        // RETURN THE QR CODE
                        qrCodeValue = result.getText();

                        Intent successIntent = new Intent(ScannerActivity.this, ScoreRevealActivity.class);
                        successIntent.putExtra("contents", qrCodeValue);
                        startActivity(successIntent);
                    }
                });
            }
        });

        codeScanner.setErrorCallback(error -> {
            Log.e(TAG, "Scan error", error);

            Intent errorIntent = new Intent(ScannerActivity.this, ScanErrorActivity.class);
            startActivity(errorIntent);
        });

        codeScanner.startPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (codeScanner != null) {
            codeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if (codeScanner != null) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }

    /**
     * When called handles permissions
     */

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                // Permissions denied, handle the error here
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}