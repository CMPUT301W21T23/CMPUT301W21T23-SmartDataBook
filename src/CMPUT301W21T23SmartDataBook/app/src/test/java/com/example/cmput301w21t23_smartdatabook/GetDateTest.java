package com.example.cmput301w21t23_smartdatabook;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to assure GetDate class returns a correctly formatted date
 * string in form of
 */
public class GetDateTest {

    /**
     * Creates a string of the current date-realtime for testing
     * @return mockCurrentDate - A string holding the current date in the form of (yyyy/MM/dd)
     */
    private String mockGetDate() {
        GetDate getDate = new GetDate();
        String mockCurrentDate = getDate.getFormattedDate();
        return mockCurrentDate;
    }

    /**
     * Test the consistency of the getFormattedDate getter in GetDate
     */
    @Test
    void testGetFormattedDate() {
        GetDate getDate = new GetDate();
        String currentDate = getDate.getFormattedDate();
        String mockCurrentDate = mockGetDate();
        assertEquals(currentDate, mockCurrentDate);
    }


}
