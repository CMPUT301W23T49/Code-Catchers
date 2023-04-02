/**
 * The ScannerActivity class contains methods for
 * scanning QR code related operations
 * @author [Kyle Karpyshyn]
 * @version 1.0
 * @since [Monday March 13 2021]
 */
package com.example.codecatchersapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.Result;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
            // scan QR code, and onDecoded decode the QR code value
            @Override
            public void onDecoded(@NonNull Result result) {
                Log.d(TAG, "onDecoded called"); //TODO: remove this
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPause();
                        try {
                            qrCodeValue = result.getText();
                            // Generate SHA-256 hash for the qrCodeValue
                            MessageDigest md = MessageDigest.getInstance("SHA-256");
                            byte[] hashBytes = md.digest(qrCodeValue.getBytes(StandardCharsets.UTF_8));
                            String hash = HexBinaryConverter.bytesToHex(hashBytes);
                            System.out.println("QR Code SHA-256 Hash: " + hash); //TODO: remove this
                            Log.d(TAG, "QR Code SHA-256 Hash: " + hash);   //TODO: remove this

                            // Convert the hash to binary representation
                            String binaryHash = HexBinaryConverter.hexToBinary(hash);
                            System.out.println("binaryHashCONVERT: " + binaryHash); //TODO: remove this

                            // Start ScoreRevealActivity with hash value
                            Intent successIntent = new Intent(ScannerActivity.this, ScoreRevealActivity.class);
                            successIntent.putExtra("hash", hash);
                            successIntent.putExtra("binaryHash", binaryHash);
                            startActivity(successIntent);
                        } catch (Exception e) {
                            Log.e(TAG, "onDecoded: ", e);
                        }

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
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    /**
     * Converts a hexadecimal string to a binary string.
     * @param hex the hexadecimal string to be converted
     * @return the binary string representation of the input hexadecimal string
     */
    // Takes a hexadecimal string as input and converts it to a binary string.
    public static String hexToBinary(String hex) {
        BigInteger bigInt = new BigInteger(hex, 16);
        String bin = bigInt.toString(2);
        int padding = hex.length() * 4 - bin.length();
        if (padding > 0) {
            bin = String.format("%0" + padding + "d", 0) + bin;
        }
        return bin;
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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