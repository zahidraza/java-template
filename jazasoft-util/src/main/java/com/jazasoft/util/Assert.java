package com.jazasoft.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Assertion utility class that assists in validating arguments.
 *
 * <p>Useful for identifying programmer errors early and clearly at runtime.
 *
 * <p>For example, if the contract of a public method states it does not
 * allow {@code null} arguments, {@code Assert} can be used to validate that
 * contract. Doing this clearly indicates a contract violation when it
 * occurs and protects the class's invariants.
 *
 * <p>Typically used to validate method arguments rather than configuration
 * properties, to check for cases that are usually programmer errors rather
 * than configuration errors. In contrast to configuration initialization
 * code, there is usually no point in falling back to defaults in such methods.
 *
 *
 */
public abstract class Assert {

    /**
     * Throw an {@code IllegalStateException}, if the expression evaluates to {@code false}.
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     * @throws IllegalStateException if {@code expression} is {@code false}
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Throw an {@code IllegalStateException}, if the expression evaluates to {@code false}.
     * @param expression a boolean expression
     * @param messageSupplier a supplier for the exception message to use if the
     * assertion fails
     * @throws IllegalStateException if {@code expression} is {@code false}
     */
    public static void state(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalStateException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if the expression evaluates to {@code false}.
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if the expression evaluates to {@code false}.
     * @param expression a boolean expression
     * @param messageSupplier a supplier for the exception message to use
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if object is not {@code null}.
     * @param object an object
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if {@code object} is not {@code null}
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if object is not {@code null}.
     * @param object
     * @param messageSupplier
     * @throws IllegalArgumentException
     */
    public static void isNull(Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if object is {@code nul}.
     * @param object an object
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if {@code object} is {@code null}
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if object is {@code null}.
     * @param object
     * @param messageSupplier
     * @throws IllegalArgumentException
     */
    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if text is {@code null} or length is zero.
     * @param text
     * @param message
     * @throws IllegalArgumentException
     */
    public static void hasLength(String text, String message) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if text is {@code null} or length is zero.
     * @param text
     * @param messageSupplier
     * @throws IllegalArgumentException
     */
    public static void hasLength(String text, Supplier<String> messageSupplier) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if text is {@code null} or text is empty.
     * @param text
     * @param message
     * @throws IllegalArgumentException
     */
    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, if text is {@code null} or text is empty.
     * @param text
     * @param messageSupplier
     * @throws IllegalArgumentException
     */
    public static void hasText(String text, Supplier<String> messageSupplier) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, If {@code textToSearch} contains {@code substring}
     * @param textToSearch
     * @param substring
     * @param message
     * @throws IllegalArgumentException
     */
    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, If {@code textToSearch} contains {@code substring}
     * @param textToSearch
     * @param substring
     * @param messageSupplier
     * @throws IllegalArgumentException
     */
    public static void doesNotContain(String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, If collection is Empty
     * @param collection
     * @param message
     * @throws IllegalArgumentException
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, If collection is Empty
     * @param collection
     * @param messageSupplier
     * @throws IllegalArgumentException
     */
    public static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, If map is Empty
     * @param map
     * @param message
     * @throws IllegalArgumentException
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throw an {@code IllegalArgumentException}, If map is Empty
     * @param map
     * @param messageSupplier
     * @throws IllegalArgumentException
     */
    public static void notEmpty(Map<?, ?> map, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

}

