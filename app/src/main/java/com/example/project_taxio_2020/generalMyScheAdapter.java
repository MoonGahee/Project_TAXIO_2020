package com.example.project_taxio_2020;

import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class generalMyScheAdapter extends RecyclerView.Adapter<generalMyScheAdapter.ItemViewHolder> {
    //
    private ArrayList<Date_Schedule> tData = new ArrayList<>();

    class ItemViewHolder extends RecyclerView.ViewHolder { //subView setting
        TextView tripSchedule;
        TextView tripPosition;

        ItemViewHolder(View itemView) {
            super(itemView);

            tripSchedule = itemView.findViewById(R.id.tripSchedule);
            tripPosition = itemView.findViewById(R.id.tripPosition);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), generalMyScheDetailActivity.class);
                    //데이터 데려와야하나..?
                    v.getContext().startActivity(intent);
                }
            });
        }

        void onBind(Date_Schedule dataT) { //data setting
            tripSchedule.setText(dataT.getSchedule_date());
            tripPosition.setText(dataT.getGeneral_num());
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_mysche_item, parent, false);
        //뭔 의미인지 사실 잘 모르는데 인플레이트함..함.. 죄송..
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(tData.get(position));
    }

    @Override
    public int getItemCount() {
        return tData.size();
    }

    void addItem(Date_Schedule data) {
        tData.add(data);
    }

    void clearItem() {
        tData.clear();
    }

}
