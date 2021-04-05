package com.example.cmput301w21t23_smartdatabook.stats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.StringDate;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
//import com.google.firebase.Timestamp;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.slider.LabelFormatter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * this class will show the various stats required for the experiments and trials
 * @author Afaq, Bosco
 */
public class StatsView extends AppCompatActivity {

    private ArrayList<ArrayList> statsDataList;
    private User user;
    private Database database;
    private FirebaseFirestore db;
    private HashMap<Object, Integer> bins = new HashMap<>();
    StringDate dateClass = new StringDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);

        database = new Database();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment");

        statsDataList = new ArrayList<>();

        statsDataList.clear();

        LineChart lineChart = (LineChart) findViewById(R.id.plotChart);
        BarChart histogram = (BarChart) findViewById(R.id.barChart);

        histogram.getDescription().setEnabled(false);

        StatisticsModel stats = new StatisticsModel();

        TextView meanView = findViewById(R.id.meanTextView);
        TextView medianView = findViewById(R.id.medianTextView);
        TextView SDView = findViewById(R.id.stdDeviationTextView);

        // https://weeklycoding.com/mpandroidchart-documentation/getting-started/
        //Source: Erwin Kurniawan A; https://stackoverflow.com/users/7693494/erwin-kurniawan-a
        //Code: https://stackoverflow.com/questions/61930061/how-to-return-a-value-from-oncompletelistener-while-creating-user-with-email-and
        database.fillStatsList(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {

                ArrayList DataList = (ArrayList) returnedObject;

                ArrayList<Date> dates = new ArrayList<>();

                statsDataList = DataList;

                stats.bubbleSortByDate(statsDataList); //sort list by date

                List<Entry> lineEntries = new ArrayList<Entry>();
                List<BarEntry> barEntries = new ArrayList<BarEntry>();

                String trialValue;
                String date;

                //Populate bins HashMap for histogram
                for (int i = 0; i<statsDataList.size(); i++) {
                    String key = statsDataList.get(i).get(0).toString();

                    //Source: gregory; https://stackoverflow.com/users/10204/gregory
                    //Code: https://stackoverflow.com/questions/81346/most-efficient-way-to-increment-a-map-value-in-java#:~:text=Map%20map,a%20value%20with%20simple%20code.
                    Integer count = bins.containsKey(key) ? bins.get(key) : 0;
                    bins.put(key, count + 1);
                }

                Log.d("statsDataListSize", "" + statsDataList.size());
                Log.d("BinSize", ""+bins.size());

                //Adds entries to the linechart. Requires that the statsDataList is sorted by date
                for (int i = 0; i<statsDataList.size(); i++){

                    trialValue = statsDataList.get(i).get(0).toString();
                    date = statsDataList.get(i).get(1).toString();

                    lineEntries.add(new Entry(i, Float.parseFloat( trialValue)) );
                    dates.add( dateClass.getDate( date ) );
                }

                //Add entries to barChart (Histogram)
                int j = 0; //index counter
                for (Object key: bins.keySet().toArray()) {
                    Log.d("Bin", "" + key.toString() + "|" + bins.get(key.toString()).toString() );
                    barEntries.add( new BarEntry(j, Float.parseFloat( bins.get(key).toString() )) );
                    j+=1;
                }

                //Source: sidcgithub; https://github.com/sidcgithub
                //Code: https://github.com/PhilJay/MPAndroidChart/issues/3705
                ValueFormatter dateAxisFormatter = new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        Date date = dates.get( (int) value );
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
//                        Log.d("Date", ""+ (int) value + "|" + sdf.format( dateClass.getDate( statsDataList.get( (int)value ).get(1).toString() )));
                        return sdf.format( date );
                    }
                };

                LineDataSet lineDataSet = new LineDataSet(lineEntries, "Trial Value"); // add entries to dataset
                lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                lineDataSet.setLineWidth(2.0f);

                XAxis lineChartXAxis = lineChart.getXAxis();
                lineChartXAxis.setValueFormatter(dateAxisFormatter);

                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);
                lineChart.invalidate(); // refresh

                BarDataSet histogramDataSet = new BarDataSet(barEntries, "Frequency");

                histogramDataSet.setBarBorderWidth(2.0f);

                String[] labels = bins.keySet().toArray(new String[bins.keySet().size()]);

                for (int i = 0; i<labels.length; i++) {
                    Log.d("label", labels[i]);
                }

//                ValueFormatter binAxisFormatter = new ValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value) {
//                        Log.d("Value", "" + value);
//                        int roundedIndex = Math.round(value);
//                        return bins.keySet().toArray()[roundedIndex].toString();
//                    }
//                };

                ValueFormatter binBinomialFormatter = new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        Log.d("Value", "" + value);

                            if (labels[(int) value].equals("1.0")) {
                                return "Pass";
                            } else {
                                return  "Fail";
                            }
                    }
                };

                //Source:ProgrammerSought; https://www.programmersought.com
                //Code: https://www.programmersought.com/article/43275089312/
//                XAxis barChartXAxis = histogram.getXAxis();
//                barChartXAxis.setValueFormatter(binAxisFormatter);
                XAxis barChartXAxis = histogram.getXAxis();
                barChartXAxis.setGranularity(1f);
                barChartXAxis.setGranularityEnabled(true);
                barChartXAxis.setLabelCount(bins.size());  // Set the number of labels on the x-axis
                barChartXAxis.setTextSize(15f); // The size of the label on the x axis
                barChartXAxis.setDrawGridLines(false); // Set this to true to draw grid lines for this axis.
                if (experiment.getTrialType().equals("Binomial")){
                    barChartXAxis.setValueFormatter(binBinomialFormatter);
                } else {
                    barChartXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                }

                YAxis barChartYAxis = histogram.getAxisRight();
//                barChartYAxis.setAxisMinimum(0f);

                BarData histogramData = new BarData(histogramDataSet);

                histogramData.setBarWidth(0.5f);
                histogram.setData(histogramData);
                histogram.setFitBars(true); // make the x-axis fit exactly all bars
                histogram.invalidate();

                stats.bubbleSortByValue(statsDataList); // sort list by value

                ArrayList<Double> sortedArray = new ArrayList<>();
                for (int i = 0; i < statsDataList.size(); i++) {
                    sortedArray.add((Double) statsDataList.get(i).get(0));
                }


//                double[] quartiles = stats.quartiles(sortedArray);

                double SD = stats.calculateSD(sortedArray);
                double mean = (double) stats.calcMean(statsDataList);
                double median = (double) stats.calcMedian(statsDataList);

                meanView.setText("Mean: "+ Double.parseDouble(String.valueOf(mean)));
                medianView.setText("Median: "+ String.valueOf(median));
                SDView.setText("Std: "+String.valueOf(SD));


            }
        } , statsDataList, db.collection("Experiments").document(experiment.getExpID()).collection("Trials"));

    }//onCreate

}


