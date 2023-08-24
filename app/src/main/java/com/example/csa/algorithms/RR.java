package com.example.csa.algorithms;

import android.util.Log;

import com.example.csa.Models.ProcessModel;
import com.example.csa.Models.input_process_model;
import com.example.csa.Models.output_process_model;

import java.util.ArrayList;
import java.util.Collections;

public class RR extends parent_abs {

    public float average_waiting_time = (float) 0.0;
    public float average_response_time = (float) 0.0;
    public static ArrayList<ProcessModel> resultArray;
    public static float[] avg_output = new float[2];
    static ArrayList<output_process_model> output = new ArrayList<>();

    public RR(ArrayList<input_process_model> input) {
        resultArray = new ArrayList<>();

        int size = input.size();
        int t = 0;
        int q = 2;
        output = set_data(input);
        Collections.sort(output, new parent_abs.SortbyAT());

        int i, total = 0, x, counter = 0, time_quantum = 4;
        int wait_time = 0, turnaround_time = 0;
        //arrival_time[10], burst_time[10],
        int[] cbtList = new int[size];
        for (int j = 0; j < size; j++) {
            cbtList[j] = output.get(j).getCbt();
        }
        x = size;
        for (total = 0, i = 0; x != 0; ) {
            if (cbtList[i] <= time_quantum && cbtList[i] > 0) {
                int lastTotal = total;

                total = total + cbtList[i];
                Log.d("TOTAL", "RR: " + output.get(i).getName() + " " + total);
                String resultString = lastTotal + "\t" + output.get(i).getName() + "\t" + total;
                resultArray.add(new ProcessModel(lastTotal, output.get(i).getName(), total));

                cbtList[i] = 0;
                counter = 1;
            } else if (cbtList[i] > 0) {
                cbtList[i] = cbtList[i] - time_quantum;
                int lastTotal = total;
                total = total + time_quantum;
                String resultString = lastTotal + "\t" + output.get(i).getName() + "\t" + total;
                resultArray.add(new ProcessModel(lastTotal, output.get(i).getName(), total));
                Log.d("TOTAL", "RR: " + output.get(i).getName() + " " + total);
            }
            if (cbtList[i] == 0 && counter == 1) {
                x--;
                output.get(i).setWaiting_time(total - output.get(i).getArrival_time() - output.get(i).getCbt());
                output.get(i).setTurn_around_time(total - output.get(i).getArrival_time());
                output.get(i).setCompleted(total);
                wait_time = wait_time + total - output.get(i).getArrival_time() - output.get(i).getCbt();
                turnaround_time = turnaround_time + total - output.get(i).getArrival_time();
                counter = 0;
            }
            if (i == size - 1) {
                i = 0;
            } else if (output.get(i + 1).getArrival_time() <= total) {
                i++;
            } else {
                i = 0;
            }
        }

        average_waiting_time = ((float) (wait_time * 1.0 / size));
        average_response_time = ((float) (turnaround_time * 1.0 / size));

        avg_output[0] = (float) average_waiting_time;
        avg_output[1] = (float) average_response_time;
        out(output);
    }

    public static float[] get_avg() {
        return avg_output;
    }

    public static ArrayList<output_process_model> getOutput() {
        return output;
    }

    public static ArrayList<ProcessModel> getResultArray() {
        return resultArray;
    }
}
