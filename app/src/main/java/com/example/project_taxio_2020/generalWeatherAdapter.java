package com.example.project_taxio_2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class generalWeatherAdapter extends BaseAdapter {
    Context context;
    ArrayList<generalWeatherItem> list;

    ImageView weather_icon;
    TextView weather_name, weather_day;

    public generalWeatherAdapter(Context context, ArrayList<generalWeatherItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.weather, null);
        weather_day = (TextView)convertView.findViewById(R.id.weather_day);
        weather_icon = (ImageView)convertView.findViewById(R.id.weather_icon);
        weather_name = (TextView)convertView.findViewById(R.id.weather_name);

        weather_day.setText(list.get(position).getWeather_day());
        weather_icon.setImageResource(list.get(position).getWeather_image());
        weather_name.setText(list.get(position).getWeather_name());

        return convertView;
    }
}
