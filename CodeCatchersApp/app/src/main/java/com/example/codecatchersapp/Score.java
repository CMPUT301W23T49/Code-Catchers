/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;

import java.security.NoSuchAlgorithmException;

/**
 * The Score class represents a score calculated based on a SHA-256 hash of a given QR code content.
 * The score is determined by finding repeated digits in the hex string representation of the hash.
 */
public class Score {

    /**
     * The hex string representation of the SHA-256 hash of the QR code content.
     */
    private String score;
    private String hexString;

    /**
     * Constructs a Score object based on a given QR code content.
     * @param hexString the content of the QR code to calculate the score from.
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not supported by the current Java runtime environment.
     */
    public Score(String hexString) throws NoSuchAlgorithmException {

        this.hexString = hexString;
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

        /**
         * Returns the numerical score calculated from the QR code content hash.
         * @return the score calculated from the QR code content hash.
         */
        String stringScore = Integer.toString(score);
        this.score=stringScore;
    }

    /**
     * Helper method to get the decimal value of a hex digit.
     * @param hexDigit the hex digit to convert to a decimal value.
     * @return the decimal value of the hex digit.
     * @throws IllegalArgumentException if the provided character is not a valid hex digit.
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
     * getter
     * @return the score as a string type
     */
    public String getScore() {
        return this.score;
    }
}