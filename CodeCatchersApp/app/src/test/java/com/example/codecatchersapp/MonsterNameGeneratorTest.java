package com.example.codecatchersapp;
import org.junit.Test;

import static org.junit.Assert.*;

/**

 The MonsterNameGeneratorTest class contains test methods for the MonsterNameGenerator class.
 It uses the JUnit testing framework to test the generateNameFromBinary method of the MonsterNameGenerator class.
 The test cases cover various scenarios such as generating names from binary inputs of different lengths,
 and ensuring that an IllegalArgumentException is thrown when the input length is too long.
 */

public class MonsterNameGeneratorTest {

    @Test
    public void testGenerateNameFromBinary() {
        MonsterNameGenerator generator = new MonsterNameGenerator();

        // Test generating name from binary input of length 0
        assertEquals("MegaHypeHexTronFuryBarnZ", generator.generateNameFromBinary(""));

        // Test generating name from binary input of length 1
        assertEquals("MegaHypeHexTronFuryBarnZ", generator.generateNameFromBinary("0"));
        assertEquals("ZapHypeHexTronFuryBarnZ", generator.generateNameFromBinary("1"));

        // Test generating name from binary input of length 2
        assertEquals("MegaHypeHexTronFuryBarnZ", generator.generateNameFromBinary("00"));
        assertEquals("MegaGloHexTronFuryBarnZ", generator.generateNameFromBinary("01"));
        assertEquals("ZapHypeHexTronFuryBarnZ", generator.generateNameFromBinary("10"));
        assertEquals("ZapGloHexTronFuryBarnZ", generator.generateNameFromBinary("11"));


        // Test generating name from binary input of length 6
        assertEquals("MegaHypeHexFloXBarnE", generator.generateNameFromBinary("000111"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateNameFromBinaryTooLong() {
        MonsterNameGenerator generator = new MonsterNameGenerator();

        // Test generating name from binary input of length 7 (should throw IllegalArgumentException)
        generator.generateNameFromBinary("0000000");
    }
}
