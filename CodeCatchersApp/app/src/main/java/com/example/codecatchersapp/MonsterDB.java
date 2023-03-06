package com.example.codecatchersapp;

import static android.content.ContentValues.TAG;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;


public class MonsterDB {

    private static final String TAG = "MonsterDB";
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    public static String hexToBinary(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);
        int padding = hex.length() * 4 - bin.length();
        if(padding > 0) {
            bin = String.format("%0" + padding + "d", 0) + bin;
        }
        return bin;
    }

    // Define the dictionary of monster features
    private static HashMap<String, HashMap<String, String>> features = new HashMap<>();
    static {
        HashMap<String, String> eyes = new HashMap<>();
        eyes.put("0", "closed eyes");
        eyes.put("1", "bright eyes");
        features.put("eyes", eyes);

        HashMap<String, String> eyebrows = new HashMap<>();
        eyebrows.put("0", "mean eyebrows");
        eyebrows.put("1", "no eyebrows");
        features.put("eyebrows", eyebrows);

        HashMap<String, String> face = new HashMap<>();
        face.put("0", "round face");
        face.put("1", "square face");
        features.put("face", face);

        HashMap<String, String> nose = new HashMap<>();
        nose.put("0", "big nose");
        nose.put("1", "no nose");
        features.put("nose", nose);

        HashMap<String, String> mouth = new HashMap<>();
        mouth.put("0", "smile");
        mouth.put("1", "frown");
        features.put("mouth", mouth);

        HashMap<String, String> ears = new HashMap<>();
        ears.put("0", "ears");
        ears.put("1", "no ears");
        features.put("ears", ears);
    }


    private String eyes;
    private String eyebrows;
    private String face;
    private String nose;
    private String mouth;
    private String ears;

    public MonsterDB(Canvas canvas, Paint paint) {
        generateRandomMonster(canvas, paint);
    }

    public String getEyes() {
        return this.eyes;
    }

    public String getEyebrows() {
        return this.eyebrows;
    }

    public String getFace() {
        return this.face;
    }

    public String getNose() {
        return this.nose;
    }

    public String getMouth() {
        return this.mouth;
    }

    public String getEars() {
        return this.ears;
    }

    public static void generateRandomMonster(Canvas canvas, Paint paint) {
        // Generate random hash value
        Random rand = new Random();
        String hashInput = Integer.toString(rand.nextInt());

        // Compute hash value using SHA-256
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(hashInput.getBytes("UTF-8"));
            String hashValue = bytesToHex(hashBytes);
            Log.d(TAG, "Hash value: " + hashValue);

            // Get the first 6 bits of the hash value as binary string
            String binaryString = hexToBinary(hashValue.substring(0, 2)).substring(2) +
                    hexToBinary(hashValue.substring(2, 4)) +
                    hexToBinary(hashValue.substring(4, 6));


            // Ensure that at least one feature of each type is included
            String[] featureTypes = {"eyes", "eyebrows", "face", "nose", "mouth", "ears"};
            for (String featureType : featureTypes) {
                boolean foundFeature = false;
                for (int i = 0; i < binaryString.length(); i++) {
                    String featureValue = Character.toString(binaryString.charAt(i));
                    if (features.get(featureType).get(featureValue) != null) {
                        foundFeature = true;
                        break;
                    }
                }
                if (!foundFeature) {
                    // Select a random feature value of this type and replace the existing one in binaryString
                    int randomIndex = rand.nextInt(binaryString.length());
                    String featureValue = features.get(featureType).keySet().iterator().next();
                    binaryString = binaryString.substring(0, randomIndex) + featureValue + binaryString.substring(randomIndex + 1);
                }
            }

            // Monster features based on the bits of binary string
            String eyeType = features.get("eyes").get(Character.toString(binaryString.charAt(0)));
            String eyebrowType = features.get("eyebrows").get(Character.toString(binaryString.charAt(1)));
            String faceType = features.get("face").get(Character.toString(binaryString.charAt(2)));
            String noseType = features.get("nose").get(Character.toString(binaryString.charAt(3)));
            String mouthType = features.get("mouth").get(Character.toString(binaryString.charAt(4)));
            String earType = features.get("ears").get(Character.toString(binaryString.charAt(5)));

            // Draw the monster using the selected features
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFFFFFFFF);
            canvas.drawRect(new RectF(750, 750, 1000, 1000), paint);
            if (faceType.equals("round face")) {
                paint.setColor(0xFFD3D3D3);
                canvas.drawOval(new RectF(200, 250, 800, 850), paint); // adjust the height to 850
            } else if (faceType.equals("square face")) { // changed to else if
                paint.setColor(0xFFD3D3D3);
                canvas.drawRect(new RectF(200, 250, 800, 850), paint); // adjust the height to 850
            }
            if (earType.equals("ears")) {
                paint.setColor(0xFFD3D3D3);
                int earSize = 75; // adjust this value to resize the ears
                int earTop = 200 + earSize/2;
                int earBottom = 250 + earSize/2;
                canvas.drawCircle(325, earTop, earSize/2, paint);
                canvas.drawCircle(675, earTop, earSize/2, paint);
            }
            if (eyebrowType.equals("mean eyebrows")) {
                paint.setColor(0xFF000000);
                int eyebrowHeight = 10; // adjust this value to make the eyebrows thicker or thinner
                int eyebrowTop = 325; // adjust this value to position the eyebrows
                int eyebrowBottom = 360; // adjust this value to position the eyebrows
                canvas.drawRect(325, eyebrowTop, 425, eyebrowBottom + eyebrowHeight, paint);
                canvas.drawRect(575, eyebrowTop, 675, eyebrowBottom + eyebrowHeight, paint);
            } else if (eyebrowType.equals("no eyebrows")) {
                paint.setColor(0xFFD3D3D3);
                canvas.drawRect(325, 300, 425, 305, paint);
                canvas.drawRect(575, 300, 675, 305, paint);
            }

            if (eyeType.equals("bright eyes")) {
                // draw the whites of the eyes
                paint.setColor(0xFFFFFFFF);
                canvas.drawCircle(400, 400, 50, paint);
                canvas.drawCircle(600, 400, 50, paint);

                // draw the pupils
                paint.setColor(0xFF000000);
                canvas.drawCircle(400, 400, 25, paint);
                canvas.drawCircle(600, 400, 25, paint);

                // draw the highlights
                paint.setColor(0xFFFFFFFF);
                canvas.drawCircle(390, 385, 8, paint);
                canvas.drawCircle(590, 385, 8, paint);
            } else {
                paint.setColor(0xFF000000);
                canvas.drawCircle(400, 400, 25, paint);
                canvas.drawCircle(600, 400, 25, paint);
                paint.setColor(0xFFFFFFFF);
                canvas.drawRect(375, 375, 425, 425, paint);
                canvas.drawRect(575, 375, 625, 425, paint);
            }

            if (noseType.equals("big nose")) {
                paint.setColor(0xFFFF4500);
                canvas.drawOval(new RectF(425, 475, 525, 575), paint);
            }

            if (mouthType.equals("smile")) {
                paint.setColor(0xFF000000);
                canvas.drawArc(new RectF(350, 575, 650, 675), 0, 180, false, paint);
            } else {
                paint.setColor(0xFF000000);
                canvas.drawArc(new RectF(350, 575, 650, 675), 0, -180, false, paint);
            }

            // Print the monster features
            System.out.println("Eye Type: " + eyeType);
            System.out.println("Eyebrow Type: " + eyebrowType);
            System.out.println("Face Type: " + faceType);
            System.out.println("Nose Type: " + noseType);
            System.out.println("Mouth Type: " + mouthType);
            System.out.println("Ear Type: " + earType);
//            Log.d(TAG, "Eye Type: " + eyeType);
//            Log.d(TAG, "Eyebrow Type: " + eyebrowType);
//            Log.d(TAG, "Face Type: " + faceType);
//            Log.d(TAG, "Nose Type: " + noseType);
//            Log.d(TAG, "Mouth Type: " + mouthType);
//            Log.d(TAG, "Ear Type: " + earType);
        } catch (Exception e) {
            e.printStackTrace();
        }}}