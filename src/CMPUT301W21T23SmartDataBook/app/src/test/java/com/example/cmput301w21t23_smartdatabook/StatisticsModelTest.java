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

    @Test
    public void TestSortingDates(){
        ArrayList<ArrayList> mockArray = mockObject();
        ArrayList<ArrayList> mockArrayCopy = new ArrayList<>();
        for (int i = 0; i<mockArray.size(); i++){
            ArrayList<Object> temp = new ArrayList<>();
            temp.add(mockArray.get(i).clone());
            mockArrayCopy.add(temp);
            temp.clear();
        }
        StatisticsModel stat = new StatisticsModel();
        Collections.shuffle(mockArrayCopy);
        stat.bubbleSortByDate(mockArrayCopy);
        assertEquals(mockArrayCopy, mockArray);
    }



}
