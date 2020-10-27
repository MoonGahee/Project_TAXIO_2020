package com.example.project_taxio_2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class generalWeatherAdapter extends RecyclerView.Adapter<generalWeatherAdapter.ViewHolder> {
    Context context;
    private ArrayList<generalWeatherItem> list;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView weather_icon, weather_wear;
        public TextView weather_name, weather_day;

        public ViewHolder(View view) {
            super(view);

            weather_day = view.findViewById(R.id.weather_day);
            weather_icon = view.findViewById(R.id.weather_icon);
            weather_name = view.findViewById(R.id.weather_name);
            weather_wear = view.findViewById(R.id.weather_wear);
        }
    }

    public generalWeatherAdapter(Context context, ArrayList<generalWeatherItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.weather_name.setText(list.get(position).getWeather_name());
        holder.weather_day.setText(list.get(position).getWeather_day());
        holder.weather_icon.setImageResource(list.get(position).getWeather_image());
        holder.weather_wear.setImageResource(list.get(position).getWeather_wear());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
