package com.example.codecatchersapp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Score {

    private String score;
    private String hexString;

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
        String stringScore = Integer.toString(score);
        this.score=stringScore;
    }

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

    public String getScore(){
        return this.score;
    }
}