package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class driverCheckScheActivity extends AppCompatActivity {
    RecyclerView trip_data;
    TextView title_text;
    Toolbar toolbar;
    ListView recruitList;
    reservationAdapter reservationAdapter;
    ArrayList<reservationItem> list_itemArrayList;
    String driver_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_chk_sch);
        setToolbar();

        //값을 받아오기
        Intent i = getIntent();
        driver_num = i.getStringExtra("driver_num");

        trip_data = findViewById(R.id.trip_data_Recycler);
        recruitList = findViewById(R.id.recruitList);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), driverMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        list_itemArrayList = new ArrayList<reservationItem>();

        list_itemArrayList.add(new reservationItem("5월 25일 16시(4시간)", "상창농장 - 용담해변(총 2명)"));
        list_itemArrayList.add(new reservationItem("5월 30일 16시(4시간)", "상창농장 - 용담해변(총 2명)"));
        list_itemArrayList.add(new reservationItem("5월 25일 16시(4시간)", "상창농장 - 용담해변(총 2명)"));

        reservationAdapter = new reservationAdapter(driverCheckScheActivity.this, list_itemArrayList);
        recruitList.setAdapter(reservationAdapter);

        recruitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), driverAcceptRequestActivity.class);
                intent.putExtra("driver_num", driver_num);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }

    public boolean onOptionsItemSelected(MenuItem item) {//toolbar의 back키 눌렀을 시
        switch (item.getItemId()){
            case android.R.id.home:{//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
