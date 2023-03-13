package com.example.codecatchersapp;


public class Leaderboards {

    private String username;
    private String score;

    // Score will need to be a double again after testing
    public Leaderboards(String username, String score){
        this.username = username;
        this.score = score;
    }

    public String getUsername(){
        return username;
    }

    /*
    public Double getScore(){
        return score;
    }
     */

    public String getScoreStringRepresentation(){
        String scoreString = score.toString();
        return scoreString;
    }

}
