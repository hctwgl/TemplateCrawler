//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.seveniu.common.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {
    private DateUtil() {
    }

    public static Date formStr(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }

    public static Date stringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return date;
    }

    public static Date stringToDay(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return date;
    }

    public static String curDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String curDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    public static Date plusDay(Date beginDate, int n) {
        LocalDate localDate = asLocalDate(beginDate);
        return asUtilDate(localDate.plusDays((long)n));
    }

    public static Date reduceDay(Date beginDate, int n) {
        LocalDate localDate = asLocalDate(beginDate);
        return asUtilDate(localDate.minusDays((long)n));
    }

    public static int intervalDays(Date startDay, Date endDay) {
        LocalDate startLocalDate = asLocalDate(startDay);
        LocalDate endLocalDate = asLocalDate(endDay);
        return (int)ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
    }

    public static Date formatDateToDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return sdf.parse(sdf.format(date));
        } catch (ParseException var3) {
            return null;
        }
    }

    public static LocalDate asLocalDate(long timestamp) {
        return asLocalDate(timestamp, ZoneId.systemDefault());
    }

    public static LocalDate asLocalDate(long timestamp, ZoneId zone) {
        return Instant.ofEpochMilli(timestamp).atZone(zone).toLocalDate();
    }

    public static LocalDate asLocalDate(Date date) {
        return asLocalDate(date, ZoneId.systemDefault());
    }

    public static LocalDate asLocalDate(Date date, ZoneId zone) {
        return date == null?null:(date instanceof java.sql.Date?((java.sql.Date)date).toLocalDate():asLocalDate(date.getTime(), zone));
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return asLocalDateTime(date, ZoneId.systemDefault());
    }

    public static LocalDateTime asLocalDateTime(Date date, ZoneId zone) {
        return date == null?null:(date instanceof Timestamp?((Timestamp)date).toLocalDateTime():Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime());
    }

    public static Date asUtilDate(Object date) {
        return asUtilDate(date, ZoneId.systemDefault());
    }

    public static Date asUtilDate(Object date, ZoneId zone) {
        if(date == null) {
            return null;
        } else if(!(date instanceof java.sql.Date) && !(date instanceof Timestamp)) {
            if(date instanceof Date) {
                return (Date)date;
            } else if(date instanceof LocalDate) {
                return Date.from(((LocalDate)date).atStartOfDay(zone).toInstant());
            } else if(date instanceof LocalDateTime) {
                return Date.from(((LocalDateTime)date).atZone(zone).toInstant());
            } else if(date instanceof ZonedDateTime) {
                return Date.from(((ZonedDateTime)date).toInstant());
            } else if(date instanceof Instant) {
                return Date.from((Instant)date);
            } else {
                throw new UnsupportedOperationException("Don't know hot to convert " + date.getClass().getName() + " to java.util.Date");
            }
        } else {
            return new Date(((Date)date).getTime());
        }
    }

    public static Instant asInstant(Date date) {
        return date == null?null:Instant.ofEpochMilli(date.getTime());
    }

    public static ZonedDateTime asZonedDateTime(Date date) {
        return asZonedDateTime(date, ZoneId.systemDefault());
    }

    public static ZonedDateTime asZonedDateTime(Date date, ZoneId zone) {
        return date == null?null:asInstant(date).atZone(zone);
    }
}
