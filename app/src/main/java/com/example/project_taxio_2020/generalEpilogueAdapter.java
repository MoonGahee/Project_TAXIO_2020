package com.example.project_taxio_2020;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

// by  관우

public class generalEpilogueAdapter extends RecyclerView.Adapter<generalEpilogueAdapter.ItemViewHolder> {
    private ArrayList<generalEpilogueItem> EData = new ArrayList<>();
    Context context;
    FirebaseStorage storage;
    StorageReference storageRef;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView driverName, review;
        RatingBar rating;


        ItemViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            driverName = itemView.findViewById(R.id.driver);
            rating = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.reviews);
            //인플레이팅
        }

        //값을 하나하나 출력해주는 함수
        void onBind(generalEpilogueItem dataE){
            Log.d("pkw", "1");
            storage = FirebaseStorage.getInstance();
            String route = "gs://taxio-b186e.appspot.com/general/"+dataE.getImage();
            storageRef = storage.getReferenceFromUrl(route);
            GlideApp.with(context).load(storageRef).into(image);
            driverName.setText(dataE.getDriver()+" 승객님");
            rating.setRating(dataE.getRating());
            review.setText(dataE.getReview());
            Log.d("pkw", "2");
        }

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_epilogue_item, parent, false);
        context = parent.getContext();
        return new generalEpilogueAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(EData.get(position));
    }

    @Override
    public int getItemCount() {
        return EData.size();
    }

    void addData(generalEpilogueItem data){ // 1. RecruitDriver에서 호출 > 2. DriverData값을 가져와서 > 3. 이 곳에 DriverData를 추가
        EData.add(data);
    }
}
