package com.example.cmput301w21t23_smartdatabook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class that returns the current date formatted to "yyyy/MM/dd"
 * @author Bosco Chan
 * @References https://www.javatpoint.com/java-get-current-date
 */
public class Date {

    DateTimeFormatter dtf;
    LocalDateTime currentDate;
    String formattedDate;

    /**
     * Public Constructor on the Date class
     */
    public Date() {
        this.dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.currentDate = LocalDateTime.now();
        this.formattedDate = dtf.format(currentDate);
    }

    /**
     * functions obtains the formattedDate
     * @return formattedDate: a string that consists the formatted date
     */
    public String getDate() {
        return formattedDate;
    }

}
