package org.sandbox;

import java.util.Random;

public class Utils {

	final private static Random random = new Random();
	
    public static String randomIntToString(int size) {
        StringBuilder value = new StringBuilder(size);

        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z'};

        for (int i = 0; i < size; i++) {
            value.append(chars[random.nextInt(chars.length)]);
        }

        return value.toString();
    }
}
