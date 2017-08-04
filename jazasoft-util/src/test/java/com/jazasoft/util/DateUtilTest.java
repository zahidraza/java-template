package com.jazasoft.util;

import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by mdzahidraza on 26/07/17.
 */
public class DateUtilTest {

    @Test
    public void testDaysBetween() {
        Date now = LocalDate.now().toDate();
        Date future = LocalDate.now().plusDays(80).toDate();
        Assert.assertEquals(80, DateUtils.daysBetween(future,now));
    }
}
