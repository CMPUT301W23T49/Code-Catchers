package com.example.codecatchersapp;


public class Leaderboards implements Comparable<Leaderboards> {

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

    @Override
    public int compareTo(Leaderboards otherScore) {
        if (Integer.parseInt(this.score) > Integer.parseInt(otherScore.score)){
            return -1;
        } else if (Integer.parseInt(this.score) == Integer.parseInt(otherScore.score)) {
            return 0;
        } else {
            return 1;
        }
    }
}
