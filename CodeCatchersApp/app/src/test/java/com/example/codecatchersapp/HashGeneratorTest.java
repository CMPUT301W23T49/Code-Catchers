package com.example.codecatchersapp;

import org.junit.Test;
import static org.junit.Assert.*;

/**

 A class responsible for generating SHA-256 hashes and writing them to a Firestore database.
 This class provides a static method to generate the SHA-256 hash of an input string.
 The hash is returned as a hexadecimal string.
 This class is used to generate the hash of a user's password before storing it in the database.

 */

public class HashGeneratorTest {

    @Test
    public void testHashGenerator() {
        String input = "password";
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        String actualHash = HashGenerator.generateAndWriteHash(input);
        assertEquals(expectedHash, actualHash);
    }
}