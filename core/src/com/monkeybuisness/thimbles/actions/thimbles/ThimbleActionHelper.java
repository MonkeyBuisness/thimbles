package com.monkeybuisness.thimbles.actions.thimbles;

import com.monkeybuisness.thimbles.actors.Ball;
import com.monkeybuisness.thimbles.actors.Thimble;
import com.monkeybuisness.thimbles.utils.RandomUtil;

public class ThimbleActionHelper {

    public static final int RANDOM_VALUE = -1;

    public static boolean isRandomValue(int value) {
        return value == RANDOM_VALUE;
    }

    public static int offset(int value) {
        return RandomUtil.nextInt(0, 1) == 1 ? value : -value;
    }

    public static void swapThimbles(Thimble[][] thimbles, int firstThimbleRowIndex, int firstThimbleColIndex,
                                     int secondThimbleRowIndex, int secondThimbleColIndex) {
        Thimble thimble = thimbles[firstThimbleRowIndex][firstThimbleColIndex];
        thimbles[firstThimbleRowIndex][firstThimbleColIndex] = thimbles[secondThimbleRowIndex][secondThimbleColIndex];
        thimbles[secondThimbleRowIndex][secondThimbleColIndex] = thimble;
    }

    public static void swapBalls(Thimble firstThimble, Thimble secondThimble) {
        Ball ball = firstThimble.getBall();
        firstThimble
                .ball(secondThimble.getBall());
        secondThimble
                .ball(ball);
    }
}