package com.example.cmput301w21t23_smartdatabook.stats;

import java.util.ArrayList;

/**
 * this classs will calculate various values needed in the statistics view
 * @author Afaq, Bosco
 */
public class StatisticsModel {

    public StatisticsModel() {
    }

    public Number calcMean(ArrayList<ArrayList> statsDataList){
        float sum = 0;
        for (int i = 0; i < statsDataList.size(); i++){
            sum += (float) statsDataList.get(i).get(0);
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
            return (((Float) statsDataList.get(middle-1).get(0) + (Float) statsDataList.get(middle).get(0)) / 2.0);
        }
    }

    public void bubbleSort(ArrayList<ArrayList> statsDataList){
        for (int i = 0; i < statsDataList.size(); i++) {
            for (int j = 0; j < statsDataList.size()-i-1; j++){
//                        Log.d("First", "" + statsDataList.get(j).get(0));
//                        Log.d("Second", "" + statsDataList.get(j+1).get(0));

                float num1 = (float) statsDataList.get(j).get(0);
                float num2 = (float) statsDataList.get(j+1).get(0);

                if (num1 > num2){
                    ArrayList temp = statsDataList.get(j);
                    statsDataList.set(j, statsDataList.get(j+1));
                    statsDataList.set(j+1,temp);
                }
            }
        }
    }

    // https://stackoverflow.com/questions/42381759/finding-first-quartile-and-third-quartile-in-integer-array-using-java
    public float[] quartiles(ArrayList<Float> val) {
        float ans[] = new float[3];

        for (int quartileType = 1; quartileType < 4; quartileType++) {
            float length = val.size() + 1;
            float quartile;
            float newArraySize = (length * ((float) (quartileType) * 25 / 100)) - 1;
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
    public static float calculateSD(ArrayList<Float> numArray)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return (float) Math.sqrt(standardDeviation/length);
    }
}
