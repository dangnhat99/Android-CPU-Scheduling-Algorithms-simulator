package com.example.csa.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csa.Models.ProcessModel;
import com.example.csa.Models.input_process_model;
import com.example.csa.R;
import com.example.csa.algorithms.RR;
import com.example.csa.algorithms.fifo;
import com.example.csa.algorithms.sjf;
import com.example.csa.algorithms.srt;
import com.example.csa.sqldatabase.database_functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComparisionActivity extends AppCompatActivity {
    ArrayList<input_process_model> process_list;
    com.example.csa.sqldatabase.database_functions database_functions;
    TextView txtMax,txtMin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);
        database_functions = new database_functions(getApplicationContext());
        process_list = database_functions.getprocesses();
        txtMax = findViewById(R.id.txtMax);
        txtMin = findViewById(R.id.txtMin);

        ArrayList<input_process_model> process_list2 = new ArrayList<>(process_list);

        ArrayList<Float> wtList = new ArrayList<>();

        Collections.sort(process_list2, new Comparator<input_process_model>() {
            @Override
            public int compare(input_process_model t1, input_process_model t2) {
                if (t1.getArrival_time() < t2.getArrival_time())
                    return -1;
                else
                    return 1;
            }
        });
        new fifo(process_list2);
        ((TextView)findViewById(R.id.avg_wt_txt1)).setText(String.valueOf(limit_decimal(fifo.get_avg()[0])));
        wtList.add((float) limit_decimal(fifo.get_avg()[0]));
        Log.d("AAAA", "onCreate: " + wtList.get(0));
        ((TextView)findViewById(R.id.avg_at_txt1)).setText(String.valueOf(limit_decimal(fifo.get_avg()[1])));

        new RR(process_list);
        ((TextView)findViewById(R.id.avg_wt_txt2)).setText(String.valueOf(limit_decimal(RR.get_avg()[0])));
        wtList.add((float) limit_decimal(RR.get_avg()[0]));
        Log.d("AAAA", "onCreate: " + wtList.get(1));
        ((TextView)findViewById(R.id.avg_at_txt2)).setText(String.valueOf(limit_decimal(RR.get_avg()[1])));

        new sjf(process_list);
        ((TextView)findViewById(R.id.avg_wt_txt3)).setText(String.valueOf(sjf.get_avg()[0]));
        wtList.add(sjf.get_avg()[0]);
        Log.d("AAAA", "onCreate: " + wtList.get(2));
        ((TextView)findViewById(R.id.avg_at_txt3)).setText(String.valueOf(sjf.get_avg()[1]));

        new srt(process_list);
        ((TextView)findViewById(R.id.avg_wt_txt4)).setText(String.valueOf(limit_decimal(srt.get_avg()[0])));
        wtList.add((float) limit_decimal(srt.get_avg()[0]));
        Log.d("AAAA", "onCreate: " + wtList.get(3));
        ((TextView)findViewById(R.id.avg_at_txt4)).setText(String.valueOf(limit_decimal(srt.get_avg()[1])));

        int max =0, min =0;
        Log.d("BBBB", "max: " + wtList.get(max).toString());
        for (int i=1; i< wtList.size(); i++) {
            Log.d("BBBB", "onCreate: " + wtList.get(i).toString());
            if (wtList.get(max) < wtList.get(i)) max = i;
            if (wtList.get(min) > wtList.get(i)) min = i;
        }

        txtMax.setText(setTextfunc(min));
        txtMin.setText(setTextfunc(max));

        for (int i=1; i< wtList.size(); i++) {
            if (i != min && wtList.get(min).equals(wtList.get(i))) {
                txtMax.append(" ," + setTextfunc(i));
            }

            if (i != max && wtList.get(max).equals(wtList.get(i))) {
                txtMin.append(" ," + setTextfunc(i));
            }
        }
    }

    public double limit_decimal(float number) {
        return Math.round(number * 100.0) / 100.0;
    }

    String setTextfunc(int i) {
        switch (i) {
            case 0:
                return "FIFO";
            case 1:
                return "RR";
            case 2:
                return "SJF";
            case 3:
                return "SRT";
            default: return ("");
        }
    }
}
