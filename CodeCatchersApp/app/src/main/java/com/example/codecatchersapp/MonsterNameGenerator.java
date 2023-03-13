/**

 This class provides a MonsterNameGenerator that generates a random name for a monster based on a given input string.
 The name is generated using a dictionary of word fragments and a hash function to select the appropriate fragments.
 @author [Josie Matalski]
 @version 1.0
 @since [Sunday March 5 2021]
 */
package com.example.codecatchersapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;

public class MonsterNameGenerator {

    private HashMap<Integer, String[]> nameDict;
    private Random random;
    /**
     * Constructor for the MonsterNameGenerator class.
     * Initializes the dictionary of word fragments and a random number generator.
     */
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
    /**
     * Generates a name for a monster based on a given input string.
     * The name is generated using a hash function and the dictionary of word fragments.
     * @param input a string used to generate the name
     * @return the generated name
     */
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
    /**
     * Generates a random name for a monster using a random input string and the generateName() method.
     * @return the generated name
     */
    public String generateRandomName() {
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        return generateName(new String(bytes));
    }
    /**
     * The main method that demonstrates the functionality of the MonsterNameGenerator class.
     * Generates a name based on a given input string and a random name using the generateRandomName() method.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        MonsterNameGenerator generator = new MonsterNameGenerator();
        System.out.println(generator.generateName("example"));
        System.out.println(generator.generateRandomName());
    }

}
