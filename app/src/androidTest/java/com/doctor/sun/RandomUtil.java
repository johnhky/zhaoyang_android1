package com.doctor.sun;

import java.util.Random;

/**
 * Created by rick on 13/10/2016.
 */
public class RandomUtil {
    private static final String chars = "abcdefghijklmnopqrstuvwxyz";

    public static String generateString(int length) {
        return generateString(chars, length);
    }

    private static String generateString(String characters, int length) {
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
