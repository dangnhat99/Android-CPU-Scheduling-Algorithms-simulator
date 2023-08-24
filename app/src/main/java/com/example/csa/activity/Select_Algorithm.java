package com.example.csa.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csa.R;
import com.example.csa.adapters.select_algorithm_adapter;

public class Select_Algorithm extends AppCompatActivity {
    RecyclerView recyclerView;
    Button next_btn;
    Context context;
    GridLayoutManager layoutManager;
    select_algorithm_adapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_algorithm_layout);

        init();
    }

    public void init()
    {
        recyclerView=findViewById(R.id.select_algorithm_recyclerview);
        next_btn=findViewById(R.id.select_algorithm_btn);
        layoutManager= new GridLayoutManager(context, 3);
        adapter=new select_algorithm_adapter(getApplicationContext());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


    public void clicked(View view) {
        if (adapter.which_is_selected()!=null)
        {
            if (!adapter.which_is_selected().equals("COMPARISON")) {
                Intent intent=new Intent();
                intent.putExtra("algorithm_name",adapter.which_is_selected());
                setResult(222,intent);
                finish();
            } else {

            }

        }else
            Toast.makeText(context, "Algorithm not selected!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        setResult(333,intent);
        finish();
    }
}
