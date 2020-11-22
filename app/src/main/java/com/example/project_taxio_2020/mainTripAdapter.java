package com.example.project_taxio_2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mainTripAdapter extends RecyclerView.Adapter<mainTripAdapter.ViewHolder> {
    Context context;
    private ArrayList<mainTripItem> list;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dates, lists;

        public ViewHolder(View view) {
            super(view);

            dates = view.findViewById(R.id.listDate);
            lists = view.findViewById(R.id.listTrip);
        }
    }

    public mainTripAdapter(Context context, ArrayList<mainTripItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.general_main_trip, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mainTripAdapter.ViewHolder holder, int position) {
        if(list.get(position).getDates().equals("")){
            holder.dates.setText("아직 일정이 없습니다!");
        }
        else{
        holder.dates.setText(list.get(position).getDates());
        holder.lists.setText(list.get(position).getLists());}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
