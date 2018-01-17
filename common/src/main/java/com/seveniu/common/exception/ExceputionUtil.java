package com.seveniu.common.exception;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
public class ExceputionUtil {
    public static String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
