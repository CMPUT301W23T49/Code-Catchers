package com.example.codecatchersapp;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class ScoreTest {

    @Test
    public void testGetDigitValue() {
        assertEquals(0, Score.getDigitValue('0'));
        assertEquals(9, Score.getDigitValue('9'));
        assertEquals(10, Score.getDigitValue('a'));
        assertEquals(15, Score.getDigitValue('f'));
        assertEquals(10, Score.getDigitValue('A'));
        assertEquals(15, Score.getDigitValue('F'));
    }

    @Test
    public void testScore() throws NoSuchAlgorithmException {
        Score score = new Score("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals("111", score.getScore());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDigitValueInvalid() {
        Score.getDigitValue('g');
    }

    @Test(expected = NoSuchAlgorithmException.class)
    public void testScoreInvalidAlgorithm() throws NoSuchAlgorithmException {
        Score score = new Score("BFG5DGW54");
        score.getScore();
    }
}
