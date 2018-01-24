package com.jazasoft.util;

/**
 * @author Md Zahid Raza
 */
public class StringUtils {
    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean hasLength(String str) {
        return str != null && str.length() > 0;
    }
}
