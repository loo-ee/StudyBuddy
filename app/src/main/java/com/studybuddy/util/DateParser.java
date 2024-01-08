package com.studybuddy.util;

public class DateParser {
    private final static String[] months = {
            "January", "February", "March", "April",
            "May", "June", "July",  "August",
            "September", "October", "November", "December"
    };

    public static String parseDate(String jsonDate) {
        String[] dateParts = jsonDate.split("-");

        return months[Integer.parseInt(dateParts[1]) -1] + " " + dateParts[2] + ", " + dateParts[0];
    }
}
