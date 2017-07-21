package com.jazasoft.mtdb.audit;

import org.springframework.data.auditing.DateTimeProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mdzahidraza on 01/07/17.
 */
public class AuditingDateTimeProvider implements DateTimeProvider {

    @Override
    public Calendar getNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        return calendar;
    }
}
