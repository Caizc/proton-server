package com.zicongcai;

import org.apache.commons.lang3.RandomUtils;

public class NullPointerTest {

    public static void main(String[] args) {

        int randomInt = RandomUtils.nextInt(1, 10);

        int[] array = {123};

        if (randomInt >= 5) {
            nullTest(array);
        } else {
            nullTest(null);
        }
    }

    private static void nullTest(int[] array) {
        System.out.println(array[0]);
    }
}
