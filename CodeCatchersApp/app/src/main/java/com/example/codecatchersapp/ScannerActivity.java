package com.example.codecatchersapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScannerActivity extends AppCompatActivity {
    private static final String TAG = "ScannerActivity";
    private static final int REQUEST_CODE = 100; // https://stackoverflow.com/questions/38507965/what-does-camera-request-code-mean-in-android
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;
    private String qrCodeValue;

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
    private void startScanner() {
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(scannerView, result.getText(), Snackbar.LENGTH_LONG).show();

                        // RETURN THE QR CODE
                        qrCodeValue = result.getText();
<<<<<<< HEAD
=======

                        Intent successIntent = new Intent(ScannerActivity.this, ScoreRevealActivity.class);
                        successIntent.putExtra("contents", qrCodeValue);
                        startActivity(successIntent);
>>>>>>> main
                    }
                });

            }
        });

        codeScanner.setErrorCallback(error -> {
            Log.e(TAG, "Scan error", error);
            /*Toast.makeText(ScannerActivity.this, "Scan error: " + error.getMessage(),
                    Toast.LENGTH_LONG).show();*/

            Intent errorIntent = new Intent(ScannerActivity.this, ScanErrorActivity.class);
            startActivity(errorIntent);
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap screenshot = takeScreenshot();
                saveScreenshotToInternalStorage(screenshot);

                System.out.println("QR CODE = " + qrCodeValue);

                Intent successIntent = new Intent(ScannerActivity.this, ScoreRevealActivity.class);
                successIntent.putExtra("qrCodeValue", qrCodeValue);
                startActivity(successIntent);

                codeScanner.startPreview();
            }
        });
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

    private Bitmap takeScreenshot() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setDrawingCacheEnabled(true);
        System.out.println("YOU GOT A SCREENSHOT!!!!!!");  // FOR TESTING PURPOSES
        return rootView.getDrawingCache();
    }

    private void saveScreenshotToInternalStorage(Bitmap screenshot) {
        try {
            // Create a subdirectory within the internal storage directory
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "qr_screenshots");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create a file object for the screenshot with the current date and time as the file name
            String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".png";
            File file = new File(directory, fileName);

            // Write the screenshot to the file
            FileOutputStream fos = new FileOutputStream(file);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            // Show a success message to the user
            Toast.makeText(this, "Screenshot saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                // Permissions denied, handle the error here
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getQRCode() {
        return qrCodeValue;
    }
}