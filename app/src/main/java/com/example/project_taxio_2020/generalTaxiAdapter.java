package com.example.project_taxio_2020;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class generalTaxiAdapter extends RecyclerView.Adapter<generalTaxiAdapter.ItemViewHolder> {

    private ArrayList<generalTaxiItem> Tdata = new ArrayList<>();
    Context context;

    public class ItemViewHolder extends RecyclerView.ViewHolder{ //
        TextView taxiDate;
        Button timeSelect;
        Spinner timeT;
        ItemViewHolder(View itemView){ //
            super(itemView);

            //객체 생성
            taxiDate = itemView.findViewById(R.id.taxi_day);
            timeT = itemView.findViewById(R.id.rent_spin);
            timeSelect = itemView.findViewById(R.id.start_btn);
        }

        //값을 하나하나 보여주는 함수
        void onBind(generalTaxiItem data){taxiDate.setText(data.getTripDate());
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
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        holder.onBind(Tdata.get(position));

        holder.timeT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String timeLong = String.valueOf(holder.timeT.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.timeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder d = new AlertDialog.Builder(context);
                d.setTitle("시작시간 선택");
                d.setMessage("택시투어를 시작할 시간을 선택하세요");
                d.show();
            }
        });
    }

    @Override
    public int getItemCount() { // recyclerview의 총 개수 반환
        return Tdata.size();
    }

    void addData(generalTaxiItem data){
        Tdata.add(data);
    }
}
