package com.example.project_taxio_2020;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView; //ItemViewHolder와 ViewHolder의 차이?
import java.util.ArrayList;

// 기사 선택 리사이클러뷰 개당 by 가희

public class generalDriverAdapter extends RecyclerView.Adapter<generalDriverAdapter.ItemViewHolder>{
    // 주석 체크만 한 경우 ViewHolder > itemViewHolder로 변경함
    private ArrayList<generalDriverItem> dData = new ArrayList<>();
    Context context;

    public class ItemViewHolder extends RecyclerView.ViewHolder{ //
        TextView driverName;
        TextView infoDriver;
        TextView infoPrice;
        ImageView driverImg;
        TextView recruit;
        TextView sendMsg;

        ItemViewHolder(View itemView){ //
            super(itemView);

            //객체 생성
            driverName = itemView.findViewById(R.id.driverName);
            infoDriver = itemView.findViewById(R.id.infoDriver);
            infoPrice = itemView.findViewById(R.id.infoPrice);
            driverImg = itemView.findViewById(R.id.driverImg);
            recruit = itemView.findViewById(R.id.recruit);
            sendMsg = itemView.findViewById(R.id.sendMsg);
        }

        //값을 하나하나 보여주는 함수
        void onBind(generalDriverItem dataD){
            driverName.setText(dataD.getDriverName());
            infoDriver.setText(dataD.getDriverInfo());
            infoPrice.setText(dataD.getDirverPrice());
        }

    }

    @NonNull
    @Override //view를 인플레이터함
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_driver_item, parent, false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override // position에 맞추어 각 항목을 구성하기 위해서 호출함
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) { //
        holder.onBind(dData.get(position));

        holder.recruit.setOnClickListener(new View.OnClickListener() {
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

        holder.sendMsg.setOnClickListener(new View.OnClickListener() {
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
        return dData.size();
    }

    void addData(generalDriverItem data){ // 1. RecruitDriver에서 호출 > 2. DriverData값을 가져와서 > 3. 이 곳에 DriverData를 추가
        dData.add(data);
    }
}