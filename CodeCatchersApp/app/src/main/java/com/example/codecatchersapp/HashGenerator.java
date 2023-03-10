package com.example.codecatchersapp;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    public static String generateSHA256Hash(String qrCode) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(qrCode.getBytes(StandardCharsets.UTF_8));
        String hexHash = bytesToHex(encodedHash);
        return hexHash;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        // Return only the first 6 characters of the hash string
        return hexString.substring(0, 6);
    }

    // Test method
//    public static void main(String[] args) {
//        String testQR = "test qr code";
//        try {
//            String hash = HashGenerator.generateSHA256Hash(testQR);
//            System.out.println("SHA-256 hash: " + hash);
//            System.out.flush();
//        } catch (NoSuchAlgorithmException e) {
//            System.out.println("Error generating hash: " + e.getMessage());
//            System.out.flush();
//        }
//    }
}

