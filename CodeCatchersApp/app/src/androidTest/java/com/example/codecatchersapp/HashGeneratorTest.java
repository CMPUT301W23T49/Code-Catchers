package com.example.codecatchersapp;

import org.junit.Test;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

public class HashGeneratorTest {

    @Test
    public void testGenerateSHA256Hash() throws NoSuchAlgorithmException {
        HashGenerator generator = new HashGenerator();
        String input = "test";
        String expectedOutput = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String actualOutput = generator.generateAndWriteHash(input);
        assertEquals(expectedOutput.substring(0, 6), actualOutput);
    }
}