package com.example.codecatchersapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeaderboardsTest {


    // Test get username
    @Test
    public void testGetUsername() {
        Leaderboards lb = new Leaderboards("user123", "100");
        assertEquals("user123", lb.getUsername());
    }

    // Test get score representation
    @Test
    public void testGetScoreStringRepresentation() {
        Leaderboards lb = new Leaderboards("user123", "100.5");
        assertEquals("100.5", lb.getScoreStringRepresentation());
    }


    // Test compare to
    @Test
    public void testCompareTo() {
        Leaderboards lb1 = new Leaderboards("user1", "100");
        Leaderboards lb2 = new Leaderboards("user2", "50");
        Leaderboards lb3 = new Leaderboards("user3", "150");
        assertEquals(-1, lb3.compareTo(lb1));
        assertEquals(1, lb2.compareTo(lb1));
        assertEquals(0, lb1.compareTo(lb1));
    }
}

