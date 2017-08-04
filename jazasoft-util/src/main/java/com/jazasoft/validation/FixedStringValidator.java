package com.jazasoft.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by razamd on 3/30/2017.
 */
public class FixedStringValidator implements ConstraintValidator<FixedString, String> {

    private Collection<String> collection = new HashSet<>();

    @Override
    public void initialize(FixedString teams) {
        Class<? extends FixedStringValue> c = teams.fixClass();
        try {
            Method method = c.getMethod("getFixedValues");
            Object obj=  method.invoke(c.getConstructor().newInstance());
            collection = (Collection<String>) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;
        String[] vals = value.split(",");
        for (String val: vals) {
            if (!collection.contains(val)) {
                StringBuilder builder = new StringBuilder();
                builder.append("[");
                collection.forEach(v -> builder.append(v + ","));
                if (builder.length() > 1)
                    builder.setLength(builder.length()-1);
                builder.append("].");
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Value(s) must be from " + builder.toString()).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
