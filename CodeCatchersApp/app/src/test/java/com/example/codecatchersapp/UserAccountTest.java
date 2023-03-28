package com.example.codecatchersapp;


import org.junit.Test;
import static org.junit.Assert.*;

public class UserAccountTest {

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUser";
        UserAccount user = new UserAccount(expectedUsername, "test@test.com");

        String actualUsername = user.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetContactInfo() {
        String expectedContactInfo = "test@test.com";
        UserAccount user = new UserAccount("testUser", expectedContactInfo);

        String actualContactInfo = user.getContactInfo();

        assertEquals(expectedContactInfo, actualContactInfo);
    }

    @Test
    public void testSetUsername() {
        String expectedUsername = "newUser";
        UserAccount user = new UserAccount("oldUser", "test@test.com");

        user.setUsername(expectedUsername);

        String actualUsername = user.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testSetContactInfo() {
        String expectedContactInfo = "new@test.com";
        UserAccount user = new UserAccount("testUser", "old@test.com");

        user.setContactInfo(expectedContactInfo);

        String actualContactInfo = user.getContactInfo();

        assertEquals(expectedContactInfo, actualContactInfo);
    }
}
