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
        Score score = new Score("BFG5DGW54");
        assertEquals("19", score.getScore());

        score = new Score("0");
        assertEquals("0", score.getScore());

        score = new Score("1");
        assertEquals("0", score.getScore());

        score = new Score("33");
        assertEquals("0", score.getScore());

        score = new Score("aa");
        assertEquals("0", score.getScore());

        score = new Score("aabbccddeeff");
        assertEquals("18640", score.getScore());

        score = new Score("aabbccddeeffAABBCCDDEEFF");
        assertEquals("334936", score.getScore());
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
