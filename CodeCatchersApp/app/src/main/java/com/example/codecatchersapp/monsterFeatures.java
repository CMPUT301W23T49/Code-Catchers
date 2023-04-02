/**
 The monsterFeatures class defines a set of features for a monster and generates a monster using these features.
 The class uses a canvas and paint object to draw the generated monster.
 The class contains log messages for debugging purposes.
 @author [Josie Matalski]
 @version 1.0
 @since [Saturday March 4 2021]
 */
package com.example.codecatchersapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Random;

// Define a set of features for a monster and set the values for each feature
public class monsterFeatures {

    private Context mContext;
    private Paint mPaint;

    private String faceType;



    // Types of features with values for each feature
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

    /**
     * Constructor for the monsterFeatures class.
     *
     * @param context Context object is used to access resources and services.

     * @param paint   Paint object is used to apply colors and styles to the monster.
     */
    // Constructor
    public monsterFeatures(Context context, Paint paint) {
        mContext = context;
        mPaint = paint;
        mPaint.setStyle(Paint.Style.FILL);
        faceType = "round face";
    }



    //, String binaryHash
    public void generateMonster(Canvas canvas, String binaryHash) {
        if(binaryHash == null || binaryHash.isEmpty()) {
            return;
        }


        System.out.println("binaryHashAGAIN: " + binaryHash);

        faceType = features.get("face").get(Character.toString(binaryHash.charAt(2)));

        // Use the mPaint instance variable instead of the paint parameter
        Paint paint = mPaint;


        // Monster features based on the bits of binary hash
        String eyeType = features.get("eyes").get(Character.toString(binaryHash.charAt(0)));
        String eyebrowType = features.get("eyebrows").get(Character.toString(binaryHash.charAt(1)));
        String faceType = features.get("face").get(Character.toString(binaryHash.charAt(2)));
        String noseType = features.get("nose").get(Character.toString(binaryHash.charAt(3)));
        String mouthType = features.get("mouth").get(Character.toString(binaryHash.charAt(4)));
        String earType = features.get("ears").get(Character.toString(binaryHash.charAt(5)));

        // Include one feature of each type
        String[] featureTypes = {"eyes", "eyebrows", "face", "nose", "mouth", "ears"};
        for (String featureType : featureTypes) {
            boolean foundFeature = false;
            for (int i = 0; i < binaryHash.length(); i++) {
                String featureValue = Character.toString(binaryHash.charAt(i));
                if (features.get(featureType).get(featureValue) != null) {
                    foundFeature = true;
                    break;
                }
            }

        }

        // Draw the monster using the selected features
        paint.setStyle(Paint.Style.FILL);

        if (faceType.equals("round face")) {
            paint.setColor(0xFFac1db8);
            canvas.drawOval(new RectF(200, 250, 800, 850), paint); // adjust the height to 850
        } else if (faceType.equals("square face")) { // changed to else if
            paint.setColor(0xFFac1db8);
            canvas.drawRect(new RectF(200, 250, 800, 850), paint); // adjust the height to 850

        }

        if (eyebrowType.equals("mean eyebrows")) {
            paint.setColor(0xFF000000);

            int eyebrowHeight = 10; // Eyebrows thick or thinner
            int eyebrowTop = 325; // adjust this value to position the eyebrows
            int eyebrowBottom = 360; // adjust this value to position the eyebrows
            canvas.drawRect(325, eyebrowTop, 425, eyebrowBottom + eyebrowHeight, paint);
            canvas.drawRect(575, eyebrowTop, 675, eyebrowBottom + eyebrowHeight, paint);
        } else if (eyebrowType.equals("no eyebrows")) {
//            paint.setColor(0xFFac1db8);
//            canvas.drawRect(325, 300, 425, 305, paint);
//            canvas.drawRect(575, 300, 675, 305, paint);
        }
        if (earType.equals("ears")) {
            paint.setColor(0xFF68d058);

            int earSize = 125; // adjust this value to resize the ears
            int earTop = 185 + earSize / 2; // adjust this value to lower the ear on the top of the face
            int earBottom = 270 + earSize / 2;
            canvas.drawCircle(325, earTop, earSize / 2, paint);
            canvas.drawCircle(675, earTop, earSize / 2, paint);
        } else if (earType.equals("no ears")) {
            paint.setColor(0xFFFFFFFF);
            // do nothing, no ears will be drawn
        }

        float startAngle = 0;
        float sweepAngle = -180;

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
            paint.setColor(0xFF933f94);

            canvas.drawArc(new RectF(550, 350, 650, 450), startAngle, -sweepAngle, true, paint);
            canvas.drawArc(new RectF(350, 350, 450, 450), startAngle, -sweepAngle, true, paint);
        }

        if (noseType.equals("big nose")) {
            paint.setColor(0xFFFF4500);

            canvas.drawOval(new RectF(425, 475, 525, 575), paint);
        }

        if (mouthType.equals("smile")) {
            paint.setColor(0xFF000000);

            canvas.drawArc(new RectF(300, 600, 700, 750), 0, 180, false, paint);
            paint.setColor(0xFFFFFFFF);

            canvas.drawArc(new RectF(375, 650, 625, 700), 0, 180, false, paint);
        } else {
            paint.setColor(0xFF000000);

            canvas.drawArc(new RectF(300, 600, 700, 750), 0, -180, false, paint);
            paint.setColor(0xFF000000);

            canvas.drawArc(new RectF(375, 650, 625, 700), 0, -180, false, paint);
        }



        // Print the monster features to terminal
        System.out.println("Eye Type: " + eyeType);
        System.out.println("Eyebrow Type: " + eyebrowType);
        System.out.println("Face Type: " + faceType);
        System.out.println("Nose Type: " + noseType);
        System.out.println("Mouth Type: " + mouthType);
        System.out.println("Ear Type: " + earType);
    }
}



