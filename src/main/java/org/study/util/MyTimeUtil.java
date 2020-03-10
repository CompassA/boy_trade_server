package org.study.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author fanqie
 * @date 2020/1/29
 */
public final class MyTimeUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private MyTimeUtil() {
    }

    public static String toString(final Timestamp timestamp) {
        return new SimpleDateFormat(DATE_FORMAT).format(timestamp);
    }

    public static Timestamp getCurrentTimestamp() {
        return localDateTimeToTimestamp(LocalDateTime.now());
    }

    public static Timestamp parseStr(final String dateStr) {
        return Timestamp.valueOf(dateStr);
    }

    public static Timestamp getDeadline(final Timestamp createTimestamp, final int hourPeriod) {
        final LocalDateTime deadline = createTimestamp.toLocalDateTime().plusHours(hourPeriod);
        return localDateTimeToTimestamp(deadline);
    }

    public static long getDeadlineMills(final Timestamp createTimestamp, final int hourPeriod) {
        final LocalDateTime deadline = createTimestamp.toLocalDateTime().plusHours(hourPeriod);
        return deadline.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static String getDeadlineStr(final Timestamp createTimestamp, final int hourPeriod) {
        return toString(getDeadline(createTimestamp, hourPeriod));
    }

    public static Timestamp localDateTimeToTimestamp(final LocalDateTime localDateTime) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return Timestamp.valueOf(localDateTime.format(formatter));
    }
}
