/**

 This class provides a MonsterNameGenerator that generates a random name for a monster based on a given input string.
 The name is generated using a dictionary of word fragments and a hash function to select the appropriate fragments.
 @author [Josie Matalski]
 @version 1.0
 @since [Sunday March 5 2021]
 */
package com.example.codecatchersapp; // The package containing the MonsterNameGenerator class
import java.util.HashMap;            // The HashMap class
/**
 * The MonsterNameGenerator class provides a method for generating a name for a monster based on a given input string.
 */
public class MonsterNameGenerator { //
    /**
     * The dictionary of word fragments used to generate a monster name.
     * The key is the index of the word fragment in the name, and the value is an array of two word fragments.
     * The first word fragment is used if the hash value is even, and the second word fragment is used if the hash value is odd.
     * For example, if the hash value is 0, the first word fragment is used, and if the hash value is 1, the second word fragment is used.
     * The word fragments are stored in an array to make it easier to select the appropriate word fragment.
     * The word fragments are stored in a HashMap to make it easier to access the word fragments based on the index of the word fragment in the name.
     * The HashMap is initialized in the constructor.
     */
    private HashMap<Integer, String[]> nameDict; // The dictionary of word fragments used to generate a monster name
    /**
     * Constructor for the MonsterNameGenerator class.
     * Initializes the dictionary of word fragments and a random number generator.
     */
    public MonsterNameGenerator() {                    // Constructor for the MonsterNameGenerator class
        nameDict = new HashMap<Integer, String[]>();   // Initialize the dictionary of word fragments
        nameDict.put(0, new String[]{"Mega", "Zap"});      // Add word fragments to the dictionary
        nameDict.put(1, new String[]{"Hype", "Glo"});      // Add word fragments to the dictionary
        nameDict.put(2, new String[]{"Hex", "Vex"});       // Add word fragments to the dictionary
        nameDict.put(3, new String[]{"Tron", "Flo"});      // Add word fragments to the dictionary
        nameDict.put(4, new String[]{"Fury", "X"});        // Add word fragments to the dictionary
        nameDict.put(5, new String[]{"BarnZ", "BarnE"});   // Add word fragments to the dictionary
    }
    /**
     * Generates a name for a monster based on a given input binary string. The input binary string
     * should have a length of at most 6. Each character in the binary string is used to determine
     * a word fragment from the nameDict, which are then combined to form the monster name.
     *
     * @param binary The input binary string used for generating the monster name.
     * @return The generated monster name.
     * @throws IllegalArgumentException if the binary string has a length greater than 6.
     */
    public String generateNameFromBinary(String binary) {
        if (binary.length() > 6) {                                                              // Check if the binary string is too long
            throw new IllegalArgumentException("Binary string must have a length of at most 6");// If the binary string is too long, throw an exception
        }
        StringBuilder sb = new StringBuilder();                                                 // String builder for the name
        for (int i = 0; i < 6; i++) {                                                           // Loop through the binary string
            int index = 0;                                                                      // Default fragment index is 0
            if (i < binary.length()) {                                                          // Check if the binary string is long enough
                index = binary.charAt(i) == '0' ? 0 : 1;                                        // If the binary string is long enough, set the fragment index based on the binary value
            }
            sb.append(nameDict.get(i)[index]);                                                  // Append the appropriate word fragment to the name
        }
        return sb.toString();                                                                   // Return the name
    }



}
