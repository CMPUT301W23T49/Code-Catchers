/** This activity is used to add comments and geolocation data to a monster's profile.
 * It is accessed by scanning a QR code, which will pass the monster's hash to this activity.
 * The user can then add a comment, and toggle whether or not to add geolocation data.
 * If the user toggles geolocation data, the app will prompt them to share their location.
 * If the user toggles location photos, the app will open the camera and save the photo to the database.
 * The user can then continue to the main menu.
 *  @author [Noah Eglauer]
 *  @editor [kyle]
 *  @version 1.0
 *  @since [Wednesday March 8 2021]
 */
package com.example.codecatchersapp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Score {
    /**
     * The score of the QR code.
     */
    private String score;
    /**
     * Constructs a new Score object with a given QR code contents.
     * @param qr_contents The contents of the QR code.
     * @throws NoSuchAlgorithmException
     */
    public Score(String qr_contents) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(qr_contents.getBytes());

        // Convert Hash to Hex String
        BigInteger bigInt = new BigInteger(1, hash);
        String hexString = bigInt.toString(16);

        // Find all repeated digits and calculate score based on the proposed scoring method in the notes
        int score = 0;
        int consecutive = 1;

        for(int i = 0; i < hexString.length()-1; i++) {
            char c = hexString.charAt(i);
            if(c == hexString.charAt(i+1)){
                consecutive+=1;
            }
            else{
                if (consecutive > 1){
                    int digitValue = getDigitValue(c);
                    score += Math.pow(20, consecutive-2) * digitValue;
                }
                consecutive = 1;
            }
        }
        String stringScore = Integer.toString(score);
        this.score=stringScore;
    }
    /** Returns the value of a hex digit.
     * @param hexDigit The hex digit to be converted.
     * @return The value of the hex digit.
     */
    public static int getDigitValue(char hexDigit) {
        if (hexDigit >= '0' && hexDigit <= '9') {
            return hexDigit - '0';
        } else if (hexDigit >= 'a' && hexDigit <= 'f') {
            return hexDigit - 'a' + 10;
        } else if (hexDigit >= 'A' && hexDigit <= 'F') {
            return hexDigit - 'A' + 10;
        } else {
            throw new IllegalArgumentException("Invalid hex digit: " + hexDigit);
        }
    }
    /**
     * Returns the score of the QR code.
     * @return The score of the QR code.
     */
    public String getScore(){
        return this.score;
    }
}