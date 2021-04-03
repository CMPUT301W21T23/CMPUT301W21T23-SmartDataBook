package com.example.cmput301w21t23_smartdatabook;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class that returns the current date formatted to "yyyy/MM/dd"
 * @author Bosco Chan
 * @References https://www.javatpoint.com/java-get-current-date
 */
public class Date {
    Timestamp currentDate;

    /**
     * Public Constructor on the Date class
     */
    public Date() {
        this.currentDate = Timestamp.now();
    }

    /**
     * functions obtains the formattedDate
     * @return formattedDate: a string that consists the formatted date
     */
    public Timestamp getCurrentDate(){
        return currentDate;
    }

}
