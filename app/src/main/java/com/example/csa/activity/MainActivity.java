package com.example.csa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.csa.Models.ProcessModel;
import com.example.csa.Models.input_process_model;
import com.example.csa.Models.output_process_model;
import com.example.csa.R;
import com.example.csa.adapters.ProcessAdapter;
import com.example.csa.adapters.show_processes_adapter;
import com.example.csa.algorithms.RR;
import com.example.csa.algorithms.fifo;
import com.example.csa.algorithms.hrrn;
import com.example.csa.algorithms.parent_abs;
import com.example.csa.algorithms.sjf;
import com.example.csa.algorithms.srt;
import com.example.csa.sqldatabase.database_functions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private BarChart chart;
    private TextView avg_wt, avg_at;
    private RecyclerView recyclerView;
    private show_processes_adapter adapter;
    ArrayList<input_process_model> process_list;
    database_functions database_functions;
    ArrayList<output_process_model> output;

    RecyclerView rvProcess;
    ProcessAdapter processAdapter;
    ArrayList<ProcessModel> processList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //init
        avg_wt = findViewById(R.id.avg_wt_txt);
        avg_at = findViewById(R.id.avg_at_txt);
        chart = findViewById(R.id.chart1);
        rvProcess = findViewById(R.id.rvProcess);
        processAdapter = new ProcessAdapter(processList, this);
        rvProcess.setAdapter(processAdapter);
        recyclerView = findViewById(R.id.main_recycler);
        database_functions = new database_functions(getApplicationContext());
        process_list = database_functions.getprocesses();
        adapter = new show_processes_adapter(getApplicationContext(), process_list);
        recyclerView.setAdapter(adapter);
        //        ArrayList<input_process_model> input = new ArrayList<>();
        output = new ArrayList<>();

        // fifo
//        input = test_data(input);
        // output = new fifo(input).out(output);

//sjf
//         new sjf(process_list);
        // output = sjf.getOutput();

//hrrn
//         new hrrn(process_list);
//         output= hrrn.getOutput();

        start_algorithm();

// fifo,sjf result

        ArrayList<BarEntry> cbt = new ArrayList<>();
        ArrayList<BarEntry> wt = new ArrayList<>();
        ArrayList<BarEntry> at = new ArrayList<>();

        for (int j = 0; j < output.size(); j++) {
            cbt.add(new BarEntry(j, (float) output.get(j).getCbt()));
            wt.add(new BarEntry(j, (float) output.get(j).getWaiting_time()));
            at.add(new BarEntry(j, (float) output.get(j).getTurn_around_time()));
        }
        BarDataSet set1, set2, set3;
        set1 = new BarDataSet(cbt, "cbt");
        //set1.setColor(Color.rgb(104, 241, 175));
        set1.setColor(Color.GREEN);
        set2 = new BarDataSet(wt, "Waiting_time");
        // set2.setColor(Color.rgb(164, 228, 251));
        set2.setColor(Color.RED);
        set3 = new BarDataSet(at, "Turn_around_time");
        //set3.setColor(Color.rgb(242, 247, 158));
        set3.setColor(Color.BLUE);

        BarData data = new BarData(set1, set2, set3);

        data.setValueFormatter(new LargeValueFormatter());

        chart.setData(data);

        float groupSpace = 0.50f;
        float barSpace = 0.02f;
        float barWidth = 0.46f;

        int startYear = 0;
        int endYear = process_list.size() * 2;
        // Disable zoom - SET Group - Refresh
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinValue(startYear);
        chart.getXAxis().setAxisMaximum(endYear);
        chart.groupBars(startYear, groupSpace, barSpace);
        chart.setFitBars(true);
        chart.invalidate();

        Log.i("1", "onCreate: 1");

        for (int i = 0; i < output.size(); i++) {

            Log.i("TAG", "onCreate: " + output.get(i).getName() + "  " + String.valueOf(output.get(i).getCbt()) +
                    "  " + String.valueOf(output.get(i).getArrival_time()) + "  " +
                    String.valueOf(output.get(i).getTurn_around_time()) + "  " +
                    String.valueOf(output.get(i).getWaiting_time()) + "  ");
        }
        Log.i("avg", "avg  " + String.valueOf(fifo.get_avg()[0]) + "  " + String.valueOf(fifo.get_avg()[1]));
        Log.i("2", "onCreate: 2");

    }

    // Just data Test!
    ArrayList<input_process_model> test_data(ArrayList<input_process_model> input) {
        // 2 1 0 3

        input_process_model p1 = new input_process_model("p3", 9, 0);
        input_process_model p2 = new input_process_model("p2", 4, 0);
        input_process_model p3 = new input_process_model("p1", 8, 0);
        input_process_model p4 = new input_process_model("p4", 5, 0);
        input.add(0, p1);
        input.add(1, p2);
        input.add(2, p3);
        input.add(3, p4);
        return input;
    }


    public void start_algorithm() {
//            {"FIFO","HRRN"","RR","SJF","SRT"}

        switch (Objects.requireNonNull(getIntent().getStringExtra("algorithm"))) {
            case "FIFO":
                Collections.sort(process_list, new Comparator<input_process_model>() {
                    @Override
                    public int compare(input_process_model t1, input_process_model t2) {
                        if (t1.getArrival_time() < t2.getArrival_time())
                            return -1;
                        else
                            return 1;
                    }
                });
                new fifo(process_list);
                output = fifo.getOutput();
                set_bar_data(output);
                avg_wt.setText(String.valueOf(limit_decimal(fifo.get_avg()[0])));
                avg_at.setText(String.valueOf(limit_decimal(fifo.get_avg()[1])));

                processList.clear();
                processList.addAll(fifo.getResultArray());
                adapter.notifyDataSetChanged();
                break;
            case "HRRN":
                new hrrn(process_list);
                output = hrrn.getOutput();
                set_bar_data(output);
                avg_wt.setText(String.valueOf(limit_decimal(hrrn.get_avg()[0])));
                avg_at.setText(String.valueOf(limit_decimal(hrrn.get_avg()[1])));
                break;
            case "RR":
                new RR(process_list);
                output = RR.getOutput();
                set_bar_data(output);
                avg_wt.setText(String.valueOf(limit_decimal(RR.get_avg()[0])));
                avg_at.setText(String.valueOf(limit_decimal(RR.get_avg()[1])));

//                for (String resultString:
//                     RR.getResultArray()) {
//                    txtProcess.append(resultString + "\n");
//                }

                processList.clear();
                processList.addAll(RR.getResultArray());
                adapter.notifyDataSetChanged();


                break;
            case "SJF":
                new sjf(process_list);
                output = sjf.getOutput();
                set_bar_data(output);
                Log.i("TAG", "start_algorithm: " + sjf.get_avg()[0]);
                Log.i("TAG", "start_algorithm: " + sjf.get_avg()[1]);
                avg_wt.setText(String.valueOf(sjf.get_avg()[0]));
                avg_at.setText(String.valueOf(sjf.get_avg()[1]));

                processList.clear();
                processList.addAll(sjf.getResultArray());
                adapter.notifyDataSetChanged();
                break;
            case "SRT":
                new srt(process_list);
                output = srt.getOutput();
                set_bar_data(output);
                avg_wt.setText(String.valueOf(limit_decimal(srt.get_avg()[0])));
                avg_at.setText(String.valueOf(limit_decimal(srt.get_avg()[1])));
                break;
        }
    }

    public void set_bar_data(ArrayList<output_process_model> output) {


        ArrayList<BarEntry> cbt = new ArrayList<>();
        ArrayList<BarEntry> wt = new ArrayList<>();
        ArrayList<BarEntry> at = new ArrayList<>();

        for (int j = 0; j < output.size(); j++) {
            cbt.add(new BarEntry(j, (float) output.get(j).getCbt()));
            wt.add(new BarEntry(j, (float) output.get(j).getWaiting_time()));
            at.add(new BarEntry(j, (float) output.get(j).getTurn_around_time()));
        }
        BarDataSet set1, set2, set3;
        set1 = new BarDataSet(cbt, "cbt");
        //set1.setColor(Color.rgb(104, 241, 175));
        set1.setColor(Color.GREEN);
        set2 = new BarDataSet(wt, "Waiting_time");
        // set2.setColor(Color.rgb(164, 228, 251));
        set2.setColor(Color.RED);
        set3 = new BarDataSet(at, "Turn_around_time");
        //set3.setColor(Color.rgb(242, 247, 158));
        set3.setColor(Color.BLUE);

        BarData data = new BarData(set1, set2, set3);

        data.setValueFormatter(new LargeValueFormatter());

        chart.setData(data);

        float groupSpace = 0.50f;
        float barSpace = 0.02f;
        float barWidth = 0.46f;

        int startYear = 0;
        int endYear = process_list.size() * 3;
        // Disable zoom - SET Group - Refresh
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinValue(startYear);
        chart.getXAxis().setAxisMaximum(endYear);
        chart.groupBars(startYear, groupSpace, barSpace);
        chart.setFitBars(false);
        chart.setHorizontalScrollBarEnabled(true);
        chart.invalidate();

    }


    public double limit_decimal(float number) {
        return Math.round(number * 100.0) / 100.0;
    }

}
