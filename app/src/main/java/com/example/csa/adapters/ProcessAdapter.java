package com.example.csa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csa.Models.ProcessModel;
import com.example.csa.R;

import java.util.ArrayList;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ProcessViewHolder> {
    ArrayList<ProcessModel> list;
    Context context;

    public ProcessAdapter(ArrayList<ProcessModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProcessViewHolder(LayoutInflater.from(context).inflate(R.layout.process_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProcessViewHolder extends RecyclerView.ViewHolder {
        TextView txtFirstTime, txtProcessName, txtLastTime;

        public ProcessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFirstTime = itemView.findViewById(R.id.txtFirstTime);
            txtLastTime = itemView.findViewById(R.id.txtLastTime);
            txtProcessName = itemView.findViewById(R.id.txtProcessName);
        }

        void bindView() {
            ProcessModel item = list.get(getAdapterPosition());

            txtFirstTime.setText(String.valueOf(item.getFirstTime()));
            txtLastTime.setText(String.valueOf(item.getLastTime()));
            txtProcessName.setText(item.getProcessName());
        }
    }
}
