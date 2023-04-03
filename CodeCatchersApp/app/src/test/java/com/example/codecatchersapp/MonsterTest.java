package com.example.codecatchersapp;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import static org.junit.Assert.*;

public class MonsterTest {

    @Test
    public void testMonsterConstructor() {
        String hash = "0123456789abcdef";
        Monster monster = new Monster(hash);
        assertEquals(hash, monster.getMonsterSHAHash());
        assertNotNull(monster.getMonsterBinaryHash());
        assertNotNull(monster.getMonsterName());
        assertNotNull(monster.getMonsterScore());
        assertNull(monster.getGeoPoint());
    }

    @Test
    public void testMonsterConstructorWithGeoloc() {
        String hash = "0123456789abcdef";
        GeoPoint geoloc = new GeoPoint(37.422, -122.084);
        Monster monster = new Monster(hash, geoloc);
        assertEquals(hash, monster.getMonsterSHAHash());
        assertNotNull(monster.getMonsterBinaryHash());
        assertNotNull(monster.getMonsterName());
        assertNotNull(monster.getMonsterScore());
        assertNotNull(monster.getGeoPoint());
        assertEquals(geoloc, monster.getGeoPoint());
    }
}