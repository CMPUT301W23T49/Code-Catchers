package com.example.codecatchersapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class MonsterNameGenerator {

    private HashMap<Integer, String[]> nameDict;
    private Random random;

    public MonsterNameGenerator() {
        nameDict = new HashMap<Integer, String[]>();
        nameDict.put(0, new String[]{"Mega", "Super"});
        nameDict.put(1, new String[]{"Saiyan", "Glo"});
        nameDict.put(2, new String[]{"Tron", "Hype"});
        nameDict.put(3, new String[]{"Mega", "Ultra"});
        nameDict.put(4, new String[]{"Spectral", "Sonic"});
        nameDict.put(5, new String[]{"BarnZ", "Barney"});
        random = new Random();
    }

    public String generateName(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int index = (hash[i] & 0xFF) % 2;
                sb.append(nameDict.get(i)[index]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateRandomName() {
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        String name = generateName(new String(bytes));
        if (name == null || name.isEmpty()) {
            Log.e(TAG, "Error: Generated monster name is null or empty");
        } else {
            Log.d(TAG, "Generated monster name: " + name);
        }
        return name;
    }

    public static void main(String[] args) {
        MonsterNameGenerator generator = new MonsterNameGenerator();
        System.out.println(generator.generateName("example"));
        System.out.println(generator.generateRandomName());
    }

}
