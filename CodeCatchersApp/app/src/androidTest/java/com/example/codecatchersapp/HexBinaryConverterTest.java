package com.example.codecatchersapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class HexBinaryConverterTest {

    @Test
    public void testBytesToHex() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xAB, (byte) 0x12, (byte) 0x34};
        String expected = "FFAB1234";
        String result = HexBinaryConverter.bytesToHex(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void testHexToBinary() {
        String hex = "FFAB1234";
        String expected = "11111111101010110001001000110100";
        String result = HexBinaryConverter.hexToBinary(hex);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSHA256Digest() {
        String input = "test";
        byte[] result = HexBinaryConverter.getSHA256Digest(input);
        assertNotNull(result);
        assertEquals(32, result.length);
    }

    @Test
    public void testGetFirstSixBits() {
        String hex = "FFAB1234";
        String expected = "111111";
        String result = HexBinaryConverter.getFirstSixBits(hex);
        assertEquals(expected, result);
    }
}