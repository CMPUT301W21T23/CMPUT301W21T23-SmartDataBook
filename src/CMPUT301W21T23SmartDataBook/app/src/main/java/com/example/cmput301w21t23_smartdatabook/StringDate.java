package com.example.cmput301w21t23_smartdatabook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A class that returns the current date formatted to "yyyy/MM/dd"
 * @author Bosco Chan
 * @References https://www.javatpoint.com/java-get-current-date
 */
public class StringDate {

    /**
     * Public Constructor on the StringDate class
     */
    public StringDate() {
    }

    /**
     * functions obtains the formattedDate
     * @return formattedDate: a string that consists the formatted date
     */
    public String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

}
