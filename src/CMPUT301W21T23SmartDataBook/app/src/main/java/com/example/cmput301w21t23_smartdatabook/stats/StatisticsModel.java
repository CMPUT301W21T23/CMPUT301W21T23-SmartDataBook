package com.example.cmput301w21t23_smartdatabook.stats;

import com.example.cmput301w21t23_smartdatabook.StringDate;

import java.util.ArrayList;
import java.util.Date;

/**
 * this class will calculate various values needed in the statistics view
 * @author Afaq, Bosco
 */
public class StatisticsModel {

    StringDate dateClass = new StringDate();

    public StatisticsModel() {
    }

    public Number calcMean(ArrayList<ArrayList> statsDataList){
        double sum = 0;
        for (int i = 0; i < statsDataList.size(); i++){
            sum += (double) statsDataList.get(i).get(0);
        }
        if (statsDataList.size() == 0) {
            return 0;
        }else{
            return sum/statsDataList.size();
        }

    }

    public Number calcMedian(ArrayList<ArrayList> statsDataList) {
        int middle = statsDataList.size()/2; // index
        if (statsDataList.size()%2 == 1) {
            return (Number) statsDataList.get(middle).get(0);
        } else {
            return (((double) statsDataList.get(middle-1).get(0) + (double) statsDataList.get(middle).get(0)) / 2.0);
        }
    }

    public void bubbleSortByValue(ArrayList<ArrayList> statsDataList){
        for (int i = 0; i < statsDataList.size(); i++) {
            for (int j = 0; j < statsDataList.size()-i-1; j++){
                double num1 = (double) statsDataList.get(j).get(0);
                double num2 = (double) statsDataList.get(j+1).get(0);
                if (num1 > num2){
                    ArrayList temp = statsDataList.get(j);
                    statsDataList.set(j, statsDataList.get(j+1));
                    statsDataList.set(j+1,temp);
                }
            }
        }
    }

    public void bubbleSortByDate(ArrayList<ArrayList> statsDataList){
        for (int i = 0; i < statsDataList.size(); i++) {
            for (int j = 0; j < statsDataList.size()-i-1; j++){
                Date d1 =  dateClass.getDate(statsDataList.get(j).get(1).toString());
                Date d2 =  dateClass.getDate(statsDataList.get(j+1).get(1).toString());
                if (d1.compareTo(d2) > 0){
                    ArrayList temp = statsDataList.get(j);
                    statsDataList.set(j, statsDataList.get(j+1));
                    statsDataList.set(j+1,temp);
                }
            }
        }//for
    }//bubbleSortByDate

    // https://stackoverflow.com/questions/42381759/finding-first-quartile-and-third-quartile-in-integer-array-using-java
    public double[] quartiles(ArrayList<Double> val) {
        double ans[] = new double[3];
        for (int quartileType = 1; quartileType < 4; quartileType++) {
            double length = val.size() + 1;
            double quartile;
            double newArraySize = (length * ((double) (quartileType) * 25 / 100)) - 1;
//            Arrays.sort(val);
            if (newArraySize % 1 == 0) {
                quartile = val.get((int) (newArraySize));
            } else {
                int newArraySize1 = (int) (newArraySize);
                quartile = (val.get(newArraySize1) + val.get(newArraySize1 + 1)) / 2;
            }
            ans[quartileType - 1] =  quartile;
        }
        return ans;
    }

    // https://www.programiz.com/java-programming/examples/standard-deviation
    public static double calculateSD(ArrayList<Double> numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();
        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return (double) Math.sqrt(standardDeviation/length);
    }


}
