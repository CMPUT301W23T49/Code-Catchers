package com.example.codecatchersapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HashGenerator {

    private FirebaseFirestore db;



    public HashGenerator() {
        db = FirebaseFirestore.getInstance();
    }

    public void generateAndWriteHash(String qrCode) throws NoSuchAlgorithmException {
        // Take the first 6 characters of qrCode
        qrCode = qrCode.substring(0, Math.min(qrCode.length(), 6));

        String sixBitHash = generateSHA256Hash(qrCode);
        System.out.println("Six Bit Hash: " + sixBitHash);

        String chunk;

        List<DocumentReference> docRefs = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        int writeCount = 0;
//        for (int i = 0; i < sixBitHash.length(); i += 2) {

        chunk = sixBitHash.substring(0, 2);
        System.out.println("Chunk: " + chunk);
        DocumentReference docRef = db.collection("hashes").document(chunk);
        Map<String, Object> docData = new HashMap<>();
        docData.put("sixBitHash", chunk);
        docRefs.add(docRef);
        data.add(docData);

        if (docRefs.size() == MainActivity.BATCH_SIZE) {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    for (int j = 0; j < docRefs.size(); j++) {
                        transaction.set(docRefs.get(j), data.get(j));
                    }
                    return null;
                }
            });
            writeCount += MainActivity.BATCH_SIZE;
            docRefs.clear();
            data.clear();
        }

        if (docRefs.size() > 0) {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    for (int j = 0; j < docRefs.size(); j++) {
                        transaction.set(docRefs.get(j), data.get(j));
                    }
                    return null;
                }
            });
            writeCount += docRefs.size();
        }
        System.out.println("Number of writes: " + writeCount);
    }



    private String generateSHA256Hash(String qrCode) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(qrCode.getBytes(StandardCharsets.UTF_8));
        String hexHash = bytesToHex(encodedHash);
        return hexHash.substring(0, 6); // return only the first 6 characters of the hash string
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
