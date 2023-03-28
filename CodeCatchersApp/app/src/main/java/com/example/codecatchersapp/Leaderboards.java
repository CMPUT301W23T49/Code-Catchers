/**
 * The Leaderboards class contains methods for
 * a Leaderboard object so it can be stored in an Arraylist<Leaderboards>
 * @author [Noah Silva]
 * @version 1.0
 * @since [Monday March 13 2023]
 */
package com.example.codecatchersapp;
/**
 * Leaderboards implements Comparable
 * It is responsible for being a Leaderboard object, returning
 *  and comparing values when needed
 */
public class Leaderboards implements Comparable<Leaderboards> {

    private String username;
    private String score;

    // Score will need to be a double again after testing
    public Leaderboards(String username, String score){
        this.username = username;
        this.score = score;
    }
    
    /**
     * Gets username
     * @return username of this object
     */
    public String getUsername(){
        return username;
    }

    /*
    public Double getScore(){
        return score;
    }
     */

    /**
     * Gets score value as a string
     * @return score of this object
     */
    public String getScoreStringRepresentation(){
        String scoreString = score.toString();
        return scoreString;
    }

    /**
     * Compares Leaderboards objects based upon their score value
     * @return -1 if this Leaderboard is larger than one it is being compared to, 0 if even, 1 otherwise
     */
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
