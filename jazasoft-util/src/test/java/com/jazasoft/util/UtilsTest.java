package com.jazasoft.util;

import org.testng.annotations.Test;

/**
 * Created by mdzahidraza on 01/09/17.
 */
@Test
public class UtilsTest {

    public void testRandomNumber() {
        System.out.println(Utils.getRandomNumber(8));
        System.out.println(Utils.getRandomNumber(6));
        System.out.println(Utils.getRandomNumber(10));
    }

    public void testRandomAlphaNumeric() {
        System.out.println(Utils.getRandomAlphaNemeric(8));
        System.out.println(Utils.getRandomAlphaNemeric(6));
        System.out.println(Utils.getRandomAlphaNemeric(10));
    }
}
