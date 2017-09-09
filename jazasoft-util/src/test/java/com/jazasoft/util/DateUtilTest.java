package com.jazasoft.util;

import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mdzahidraza on 26/07/17.
 */
@Test
public class DateUtilTest {


    public void testDaysBetween() {
        Date now = LocalDate.now().toDate();
        Date future = LocalDate.now().plusDays(80).toDate();
        Assert.assertEquals(80, DateUtils.daysBetween(future,now));
    }

    public void testPlusDays() {
        Date now = LocalDate.now().toDate();
        Date plusDate = LocalDate.now().plusDays(10).toDate();
        Assert.assertEquals(plusDate, DateUtils.plusDays(now, 10));
    }

    public void testMinusDays() {
        Date now = LocalDate.now().toDate();
        Date plusDate = LocalDate.now().minusDays(10).toDate();
        Assert.assertEquals(plusDate, DateUtils.minusDays(now, 10));
    }

    public void minutesSinceMidnight() {
        System.out.println(DateUtils.getMinutesSinceMidnight(new Date()));
    }
}
