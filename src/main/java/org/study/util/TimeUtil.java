package org.study.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fanqie
 * @date 2020/1/29
 */
public final class TimeUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private TimeUtil() {
    }

    public static String toString(final Timestamp timestamp) {
        return new SimpleDateFormat(DATE_FORMAT).format(timestamp);
    }

    public static Timestamp getCurrentTimestamp() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return Timestamp.valueOf(LocalDateTime.now().format(formatter));
    }

    public static Timestamp parseStr(final String dateStr) {
        return Timestamp.valueOf(dateStr);
    }
}
