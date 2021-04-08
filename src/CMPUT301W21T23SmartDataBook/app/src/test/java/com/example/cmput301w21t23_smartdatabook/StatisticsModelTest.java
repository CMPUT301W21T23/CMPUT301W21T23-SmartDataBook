package com.example.cmput301w21t23_smartdatabook;

import android.util.Log;

import com.example.cmput301w21t23_smartdatabook.stats.StatisticsModel;
import com.example.cmput301w21t23_smartdatabook.stats.StringDate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class StatisticsModelTest {

    public ArrayList<ArrayList> mockObject(){
        ArrayList<ArrayList> mockArray = new ArrayList<>();
        StringDate date = new StringDate();
        for (int i = 0; i<20; i++){
            ArrayList<Object> mockSubArray = new ArrayList<>();
            mockSubArray.add((double) i);
            mockSubArray.add(date.getCurrentDate());
            mockArray.add(mockSubArray);
        }
        return mockArray;
    }

    @Test
    public void TestMean(){
        ArrayList<ArrayList> mockArray = mockObject();
        StatisticsModel stat = new StatisticsModel();
        Number mean = stat.calcMean(mockArray);
        assertEquals(mean, 9.5);
    }

    @Test
    public void TestMedian(){
        ArrayList<ArrayList> mockArray = mockObject();
        StatisticsModel stat = new StatisticsModel();
        Number median = stat.calcMedian(mockArray);
        assertEquals(median, 9.5);
    }

    @Test
    public void TestSTD(){
        ArrayList<ArrayList> mockArray = mockObject();
        StatisticsModel stat = new StatisticsModel();
        ArrayList<Double> sortedArray = new ArrayList<>();
        for (int i = 0; i < mockArray.size(); i++) {
            sortedArray.add((Double) mockArray.get(i).get(0));
        }
        double SD = stat.calculateSD(sortedArray);

        assertEquals(SD, 5.766281297335398, 1e-15);

    }

    @Test
    public void TestSortingValues(){
        ArrayList<ArrayList> mockArray = mockObject();
        ArrayList<ArrayList> mockArrayShuffled = mockObject();
        StatisticsModel stat = new StatisticsModel();
        Collections.shuffle(mockArrayShuffled);
        stat.bubbleSortByValue(mockArrayShuffled);
        assertEquals(mockArrayShuffled, mockArray);
    }

    // double check
    @Test
    public void TestSortingDates(){
        ArrayList<ArrayList> mockArray = mockObject();
        StatisticsModel stat = new StatisticsModel();
        Collections.shuffle(mockArray);
        stat.bubbleSortByDate(mockArray);
        assertEquals(mockArray, mockArray);
    }

    @Test
    public void TestQuartiles(){
        ArrayList<ArrayList> mockArray = mockObject();
        StatisticsModel stat = new StatisticsModel();
        ArrayList<Double> sortedArray = new ArrayList<>();
        for (int i = 0; i < mockArray.size(); i++) {
            sortedArray.add((Double) mockArray.get(i).get(0));
        }
        double[] quartiles = stat.quartiles(sortedArray);
        assertEquals(quartiles[0], 4.5, 1e-15);
        assertEquals(quartiles[1], 9.5, 1e-15);
        assertEquals(quartiles[2], 14.5, 1e-15);
    }


}
