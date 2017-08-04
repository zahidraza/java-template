package com.jazasoft.util;


import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.Date;

/**
 * Created by mdzahidraza on 26/07/17.
 */
public class DateUtils {

    public static int daysBetween(Date finalDate, Date initialDate) {
        LocalDate fDate = LocalDate.fromDateFields(finalDate);
        LocalDate iDate = LocalDate.fromDateFields(initialDate);
        return Days.daysBetween(iDate,fDate).getDays();
    }
}
