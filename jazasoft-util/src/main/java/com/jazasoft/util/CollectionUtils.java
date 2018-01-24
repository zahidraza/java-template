package com.jazasoft.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Md Zahid Raza
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
