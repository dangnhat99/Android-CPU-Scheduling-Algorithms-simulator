package com.example.csa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csa.Models.input_process_model;
import com.example.csa.R;
import com.example.csa.adapters.show_processes_adapter;
import com.example.csa.sqldatabase.database_functions;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class Enter_Data extends AppCompatActivity {
    TextInputLayout at_input,bt_input,name_input;
    EditText at_et,bt_et,name_et;
    Button btn;
   //TextInputEditText at_et,bt_et,name_et;
    ArrayList<input_process_model> input_list;
    RecyclerView recyclerView;
    show_processes_adapter adapter;
    GridLayoutManager layoutManager;
    com.example.csa.sqldatabase.database_functions database_functions;
    int device_window_width;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_data_layout);
        init();
    }

    public void init()
    {
        at_input=findViewById(R.id.enter_data_at_input);
        bt_input=findViewById(R.id.enter_data_bt_input);
        name_input=findViewById(R.id.enter_data_name_input);
        at_et=  at_input.getEditText();
        bt_et=  bt_input.getEditText();
        name_et=  name_input.getEditText();
        input_list=new ArrayList<>();
        database_functions=new database_functions(getApplicationContext());
        recyclerView=findViewById(R.id.enter_data_recyclerview);
        adapter=new show_processes_adapter(getApplicationContext(),input_list);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        device_window_width = displayMetrics.widthPixels;
        layoutManager=new GridLayoutManager(getApplicationContext(), 1);
        btn=findViewById(R.id.button);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_list.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void add_clicked(View view) {
        if (!bt_et.getText().toString().equals("")&&!at_et.getText().toString().equals("")&&!name_et.getText().toString().equals(""))
        {
            input_process_model temp=new input_process_model(name_et.getText().toString()
                    ,Integer.parseInt(bt_et.getText().toString())
                    ,Integer.parseInt(at_et.getText().toString())
                    ,-1);
            input_list.add(temp);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            Log.e("width",device_window_width+ "  &  "+((show_processes_adapter) recyclerView.getAdapter()).getitemwidth() +"  &  "+adapter.getitemwidth());

            if (((show_processes_adapter)recyclerView.getAdapter()).getitemwidth()!=0) {
                layoutManager = new GridLayoutManager(getApplicationContext(), device_window_width / ((show_processes_adapter) recyclerView.getAdapter()).getitemwidth());
                recyclerView.setLayoutManager(layoutManager);
            }
        }else
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
    }

    public void clicked(View view) {
        if (input_list.size()>0){
            startActivityForResult(new Intent(getApplicationContext(), Select_Algorithm.class),111);
        }else
            Toast.makeText(this, "No process entered", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==111&& resultCode==222)
        {
            database_functions.addprocesses(input_list);
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("algorithm",data.getStringExtra("algorithm_name"));
            startActivity(intent);
        } else if (resultCode == 333) {
            database_functions.addprocesses(input_list);
            startActivity(new Intent(this, ComparisionActivity.class));
        }
    }
}
