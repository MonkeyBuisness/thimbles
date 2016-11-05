package com.monkeybuisness.thimbles.utils;

import java.util.Random;

public class RandomUtil {

    private static Random random = new Random();

    public static int nextInt(int min, int max) {
        if (min > max)
            return max;
        return random.nextInt((max - min) + 1) + min;
    }
}