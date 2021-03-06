package com.example.project_taxio_2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class generalTimelineAdapter extends BaseAdapter {

    Context context;
    ArrayList<generalTimelineItem> list_itemArrayList;
    TextView place_textView, number_textView, requied_time_textView;
    ImageView line_imageView, taxi_imageView;

    public generalTimelineAdapter(Context context, ArrayList<generalTimelineItem> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.general_timeline_item, null);
        place_textView =(TextView)convertView.findViewById(R.id.place);
        number_textView = (TextView)convertView.findViewById(R.id.number);
        requied_time_textView = (TextView)convertView.findViewById(R.id.required_time);
        line_imageView = (ImageView)convertView.findViewById(R.id.line);
        taxi_imageView = (ImageView)convertView.findViewById(R.id.taxi);

        place_textView.setText(list_itemArrayList.get(position).getPlace());
        number_textView.setText(list_itemArrayList.get(position).getNumber());
        requied_time_textView.setText(list_itemArrayList.get(position).getRequied_time());
        line_imageView.setImageResource(list_itemArrayList.get(position).getLine());
        taxi_imageView.setImageResource(list_itemArrayList.get(position).getTaxi());

        return convertView;
    }
}
