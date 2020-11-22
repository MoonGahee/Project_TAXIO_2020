package com.example.project_taxio_2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

// by  관우

public class generalEpilogueAdapter extends BaseAdapter {

    Context context;
    ArrayList<generalEpilogueItem> list_itemArrayList;

    ImageView image_imageView;
    TextView driver_textView;
    RatingBar rating_ratingBar;
    FirebaseStorage storage;
    StorageReference storageRef;

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

        convertView = LayoutInflater.from(context).inflate(R.layout.general_epilogue_item, null);
        image_imageView = (ImageView)convertView.findViewById(R.id.image);
        driver_textView = (TextView)convertView.findViewById(R.id.driver);
        rating_ratingBar = (RatingBar)convertView.findViewById(R.id.rating);

        storage = FirebaseStorage.getInstance();
        String route = "gs://taxio-b186e.appspot.com/driver/"+list_itemArrayList.get(position).getImage();
        storageRef = storage.getReferenceFromUrl(route);
        GlideApp.with(context).load(storageRef).into(image_imageView);
        driver_textView.setText(list_itemArrayList.get(position).getDriver());
        rating_ratingBar.setRating(list_itemArrayList.get(position).getRating());

        return convertView;
    }

    public generalEpilogueAdapter (Context context, ArrayList<generalEpilogueItem> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }
}
