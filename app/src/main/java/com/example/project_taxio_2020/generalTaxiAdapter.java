package com.example.project_taxio_2020;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class generalTaxiAdapter extends RecyclerView.Adapter<generalTaxiAdapter.ItemViewHolder> {

    private ArrayList<generalTaxiItem> Tdata = new ArrayList<>();
    Context context;

    public class ItemViewHolder extends RecyclerView.ViewHolder{ //
        TextView taxiDate;
        Spinner time;
        Button timeSelect;

        ItemViewHolder(View itemView){ //
            super(itemView);

            //객체 생성
            taxiDate = itemView.findViewById(R.id.taxi_day);
            time = itemView.findViewById(R.id.rent_spin);
            timeSelect = itemView.findViewById(R.id.start_btn);
        }

        //값을 하나하나 보여주는 함수
        void onBind(generalTaxiItem data){
            taxiDate.setText(data.getTripDate());
        }

    }

    @NonNull
    @Override //view를 인플레이터함
    public generalTaxiAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_taxi_item, parent, false);
        context = parent.getContext();
        return new generalTaxiAdapter.ItemViewHolder(view);
    }

    @Override // position에 맞추어 각 항목을 구성하기 위해서 호출함
    public void onBindViewHolder(@NonNull generalTaxiAdapter.ItemViewHolder holder, int position) { //
        holder.onBind(Tdata.get(position));

        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("기사 요청");
                builder.setMessage("아이린 기사님에게 요청하시겠습니까?\n금액은 60,000원입니다.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context ,generalReservationCompleteActivity.class);
                        context.startActivity(i);
                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.show();
            }
        });

        holder.timeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("문자 전송");
                builder.setMessage("아이린 기사님에게 문자를 \n전송하시겠습니까?");
                builder.setPositiveButton("예", null);
                builder.setNegativeButton("아니오", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() { // recyclerview의 총 개수 반환
        return Tdata.size();
    }

    void addData(generalTaxiItem data){ // 1. RecruitDriver에서 호출 > 2. DriverData값을 가져와서 > 3. 이 곳에 DriverData를 추가
        Tdata.add(data);
    }
}
