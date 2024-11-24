package dev.archimedes.excel4j.enums;

import java.util.regex.Pattern;

public enum DateTypeRegex {
    LOCAL_DATE_TIME("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$"),
    LOCAL_TIME("^\\d{4}-\\d{2}-\\d{2}$"),
    LOCAL_DATE("^\\d{2}:\\d{2}:\\d{2}$");

    public final String pattern;

    DateTypeRegex(String pattern) {
        this.pattern = pattern;
    }

    public static boolean validate(String value, DateTypeRegex regex) {
        return Pattern.compile(regex.pattern).matcher(value).matches();
    }
}
