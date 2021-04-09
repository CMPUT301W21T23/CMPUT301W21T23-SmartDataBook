package com.example.cmput301w21t23_smartdatabook.stats;

import java.text.DateFormat;
import java.text.ParseException;
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
        return makeFormattedDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public String getShortDate() {
        return makeFormattedDate("yyyy-MM-dd");
    }

    public String getCustomDate(String pattern) {
        return makeFormattedDate(pattern);
    }

    private String makeFormattedDate(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CANADA);
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * This function gets the date back from the string, opposite for the getCurrentDate() method
     * @param dateString: a string variable showing date
     * @return result1, the date object showing the date
     */
    // https://stackoverflow.com/questions/2201925/converting-iso-8601-compliant-string-to-java-util-date
    public Date getDate(String dateString){
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date result1 = null;
        try {
            result1 = df1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result1;
    }

}
