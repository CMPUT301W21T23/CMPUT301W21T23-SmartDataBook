package com.example.cmput301w21t23_smartdatabook;

import com.example.cmput301w21t23_smartdatabook.stats.StatisticsModel;

import org.junit.Test;

import java.util.ArrayList;

public class StatisticsModelTest {

    private ArrayList<ArrayList> mockObject(){
        ArrayList<ArrayList> mockArray = new ArrayList<>();
        ArrayList<Object> mockSubArray = new ArrayList<>();
        return mockArray;
    }

    @Test
    private void TestMean(){
        ArrayList<ArrayList> mockArray = mockObject();
        for (int i = 0; i<20; i++){
            ArrayList<Object> mockSubArray = new ArrayList<>();
            mockSubArray.add((double) i);
            mockSubArray.add("2021-04-08T20:22:41.755Z");

        }

    }

}
