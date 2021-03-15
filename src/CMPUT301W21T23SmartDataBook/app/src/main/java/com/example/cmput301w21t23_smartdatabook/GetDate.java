package com.example.cmput301w21t23_smartdatabook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class that returns the current date formatted to "yyyy/MM/dd"
 * @author Bosco Chan
 * @References https://www.javatpoint.com/java-get-current-date
 */
public class GetDate {

    DateTimeFormatter dtf;
    LocalDateTime currentDate;
    String formattedDate;

    public GetDate() {
        this.dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.currentDate = LocalDateTime.now();
        this.formattedDate = dtf.format(currentDate);
    }

    public String getFormattedDate() {
        return formattedDate;
    }

}
