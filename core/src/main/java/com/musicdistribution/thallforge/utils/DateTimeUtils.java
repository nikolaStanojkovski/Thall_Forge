package com.musicdistribution.thallforge.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public final class DateTimeUtils {

    public static LocalDateTime getLocalDateTimeFromCalendar(Calendar date) {
        Instant instant = Instant.ofEpochMilli(date.getTimeInMillis());
        ZoneOffset zoneOffset = date.getTimeZone().toZoneId().getRules().getOffset(instant);
        return LocalDateTime.ofInstant(instant, zoneOffset);
    }

    public static String getFormattedDate(String format, Calendar date) {
        LocalDateTime localDateTime = getLocalDateTimeFromCalendar(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(localDateTime);
    }
}
