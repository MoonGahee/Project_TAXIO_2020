package com.example.project_taxio_2020;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//택시 선택 by 하은

public class generalSTaxiActivity extends AppCompatActivity {
    private generalTaxiAdapter adapter;
    Button start_btn, ok;
    Spinner rent_spin;
    TextView taxi_day, title_text;
    RecyclerView recyclerView_taxi;
    Integer tripDays, tripMonth, tripDay;
    String dates[];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_taxi_activity);
        setToolbar();
        start_btn = findViewById(R.id.start_btn);
        ok = findViewById(R.id.ok);
        rent_spin = findViewById(R.id.rent_spin);
        taxi_day = findViewById(R.id.taxi_day);
        recyclerView_taxi = findViewById(R.id.recyclerView_taxi);
        Intent i = new Intent();
        tripDays= i.getIntExtra("days", 0);
        tripMonth=i.getIntExtra("startMonth", 0);
        tripDay=i.getIntExtra("startDay", 0);
        dates=new String[tripDays];
        for(int j = 0; j <tripDays ; j++){
            dates[j]=(tripMonth + "월 " + (tripDay + j) + "일");
        }


        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        RecyclerView recyclerView_taxi = findViewById(R.id.recyclerView_taxi);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_taxi.setLayoutManager(linearLayoutManager);

        adapter = new generalTaxiAdapter();
        recyclerView_taxi.setAdapter(adapter);

        for(int j = 0; j <tripDays ; j++){
            generalTaxiItem data = new generalTaxiItem();
            data.setTripDate(dates[j]);
            adapter.addData(data);
        }
        adapter.notifyDataSetChanged();
    }




    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화

    }
}
