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

    private String username;                                 // Username of the user
    private String score;                                    // Score of the user

    // Score will need to be a double again after testing
    public Leaderboards(String username, String score){     // Constructor
        this.username = username;                           // Sets username
        this.score = score;                                 // Sets score
    }
    
    /**
     * Gets username
     * @return username of this object
     */
    public String getUsername(){
        return username;
    }


    /**
     * Gets score value as a string
     * @return score of this object
     */
    public String getScoreStringRepresentation(){           // Returns score as a string
        String scoreString = score.toString();              // Converts score to a string
        return scoreString;                                 // Returns score as a string
    }

    /**
     * Compares Leaderboards objects based upon their score value
     * @return -1 if this Leaderboard is larger than one it is being compared to, 0 if even, 1 otherwise
     */
    @Override
    public int compareTo(Leaderboards otherScore) {                                 // Compares Leaderboards objects based upon their score value
        if (Integer.parseInt(this.score) > Integer.parseInt(otherScore.score)){     // If this Leaderboard is larger than one it is being compared to
            return -1;                                                              // Return -1
        } else if (Integer.parseInt(this.score) == Integer.parseInt(otherScore.score)) { // If this Leaderboard is equal to one it is being compared to
            return 0;                                                                // Return 0
        } else {
            return 1;                                                                // Return 1
        }
    }
}
