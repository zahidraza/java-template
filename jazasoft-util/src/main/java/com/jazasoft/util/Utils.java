package com.jazasoft.util;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdzahidraza on 25/08/17.
 */
public class Utils {
    /**
     * Get List From comma separated String value
     * @param csv
     * @return
     */
    public static List<String> getListFromCsv(@NotNull String csv) {
        List<String> result = new ArrayList<>();
        String[] values = csv.split(",");
        for (String value : values) {
            if (value.trim().length() != 0) {
                result.add(value.trim());
            }
        }
        return result;
    }

    public static String getCsvFromIterable(@NotNull Iterable<String> iterable) {
        StringBuilder builder = new StringBuilder();
        iterable.forEach(itr -> builder.append(itr).append(","));
        if (builder.length() > 0)
            builder.setLength(builder.length()-1);
        return builder.toString();
    }

    /**
     * Genarate Random Number of specified length.
     * @param length
     * @return
     */
    public static String getRandomNumber(int length) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0','9')
                .build();
        return generator.generate(length);
    }

    public static String getRandomAlphaNemeric(int length) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0','z')
                .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
                .build();
        return generator.generate(length);
    }
}
