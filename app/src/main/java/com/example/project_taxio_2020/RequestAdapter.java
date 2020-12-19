package com.example.project_taxio_2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestAdapter extends BaseAdapter {
    Context context;
    TextView recruit_Textview, recruitplace_Textview;
    ArrayList<RequestItem> list;

    public RequestAdapter(Context context, ArrayList<RequestItem> list) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.reservation_item, null);

        recruit_Textview = (TextView)convertView.findViewById(R.id.recruit);
        recruitplace_Textview = (TextView)convertView.findViewById(R.id.recruitplace);

        recruit_Textview.setText(list.get(position).getDays());
        recruitplace_Textview.setText(list.get(position).getGeneral_name());

        return convertView;
    }

}
