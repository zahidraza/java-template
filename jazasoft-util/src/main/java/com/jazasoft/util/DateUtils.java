package com.jazasoft.util;


import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mdzahidraza on 26/07/17.
 */
public class DateUtils {

    public static int daysBetween(Date finalDate, Date initialDate) {
        LocalDate fDate = LocalDate.fromDateFields(finalDate);
        LocalDate iDate = LocalDate.fromDateFields(initialDate);
        return Days.daysBetween(iDate,fDate).getDays();
    }

    public static Date plusDays(Date date, int days) {
        return LocalDate.fromDateFields(date).plusDays(days).toDate();
    }

    public static Date minusDays(Date date, int days) {
        return LocalDate.fromDateFields(date).minusDays(days).toDate();
    }

    public static int getMinutesSinceMidnight(Date date){
        long timeNow = date.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(timeNow);
        long millisToday = (timeNow - TimeUnit.DAYS.toMillis(days));  //GMT:This number of milliseconds after 12:00 today
        int minutesToday = (int )TimeUnit.MILLISECONDS.toMinutes(millisToday);  //GMT
        minutesToday += (60*5) + 30;  //GMT+05:30 minutesToday
        minutesToday = minutesToday % (24*60);
        return minutesToday;
    }
}
