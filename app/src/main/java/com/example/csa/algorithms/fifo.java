package com.example.csa.algorithms;

import android.annotation.SuppressLint;
import android.os.Build;

import com.example.csa.Models.ProcessModel;
import com.example.csa.Models.input_process_model;
import com.example.csa.Models.output_process_model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class fifo {

    float average_waiting_time= (float) 0.0;
    float average_response_time= (float) 0.0;

    static float[] avg_output= new float[2];

    public static ArrayList<ProcessModel> resultArray;

    public static ArrayList<output_process_model> getOutput() {
        return output;
    }

    // ArrayList<input_process_model> input=new ArrayList<>();
    static ArrayList<output_process_model> output=new ArrayList<>();

    public fifo(ArrayList<input_process_model> input)
    {
        resultArray = new ArrayList<>();
          int num=input.size();
          output=set_data(input);

          float prevEnd=0;
        //output=compare(output);
        Collections.sort(output,new Sortbyroll());

        for(int i=0 ; i<num ;i++)
            {
               output.get(i).setCompleted(max(prevEnd,output.get(i).getArrival_time()) + output.get(i).getCbt());
               output.get(i).setTurn_around_time((int)output.get(i).getCompleted() -output.get(i).getArrival_time());
               output.get(i).setWaiting_time(output.get(i).getTurn_around_time()-output.get(i).getCbt());
                prevEnd=output.get(i).getCompleted();

                average_waiting_time += output.get(i).getWaiting_time();
                average_response_time += output.get(i).getTurn_around_time();

                if (i==0) {
                    resultArray.add(new ProcessModel(output.get(i).getArrival_time(), output.get(i).getName(),output.get(i).getArrival_time() + output.get(i).getCbt()));
                } else {
                   if ( resultArray.get(resultArray.size()-1).getLastTime() < output.get(i).getArrival_time() ) {
                       resultArray.add(new ProcessModel(output.get(i).getArrival_time(), output.get(i).getName(), output.get(i).getArrival_time() + output.get(i).getCbt()));
                   } else {
                       resultArray.add(new ProcessModel(resultArray.get(resultArray.size()-1).getLastTime(), output.get(i).getName(), resultArray.get(resultArray.size()-1).getLastTime() + output.get(i).getCbt()));
                   }
                }

            }
        avg_output[0] = (float)average_waiting_time/num;
        avg_output[1] = (float)average_response_time/num;
        out(output);
    }

    public ArrayList<output_process_model> out(ArrayList<output_process_model> output)
    {
        return output;
    }

      public static float[] get_avg()
    {
        return avg_output;
    }
    public static ArrayList<ProcessModel> getResultArray() {
        return resultArray;
    }


    ArrayList<output_process_model> compare(ArrayList<output_process_model> input)
    {
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.size()-1; j++) {
                if(input.get(j).getArrival_time() > input.get(j+1).getArrival_time())
                {
                    Collections.swap(input,j,j+1);
                }

            }
        }
        return input;

    }

    float max(float a,float b)
    {
        if(a>b)
            return a;
        else
            return b;
    }

    ArrayList<output_process_model> set_data(ArrayList<input_process_model> input)
    {
        ArrayList<output_process_model> output=new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            output_process_model temp=new output_process_model(
                    input.get(i).getName(),
                    input.get(i).getCbt(),
                    input.get(i).getArrival_time()
            );
            output.add(i,temp);
        }
        return output;
    }

    class Sortbyroll implements Comparator<output_process_model>

    {

        // Used for sorting in ascending order of

        // roll number

        public int compare(output_process_model a, output_process_model b)

        {

            return a.getArrival_time() - b.getArrival_time();

        }

    }
}
