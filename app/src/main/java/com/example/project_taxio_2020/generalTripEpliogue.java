package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringJoiner;

public class generalTripEpliogue extends AppCompatActivity {
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    TextView title_text;
    Button edit_epilogue;
    ListView listView;
    int selectedPosition = -1;
    generalEpilogueAdapter epilogue_listAdapter;
    String general_num;
    reservationAdapter reservationAdapter;
    ArrayList<Schedule> list_schedule = new ArrayList<>();
    ArrayList<reservationItem> list_itemArrayList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_epilogue_trip);
        setToolbar();

        //값 받아오기
        Intent i = getIntent();
        general_num = i.getStringExtra("general_num");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        naviItem();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        edit_epilogue = findViewById(R.id.edit_epilogue);
        listView = findViewById(R.id.epilogues);

        reservationAdapter = new reservationAdapter(generalTripEpliogue.this, list_itemArrayList);
        getData();
        listView.setAdapter(reservationAdapter);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(generalTripEpliogue.this, generalMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        edit_epilogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition >= 0) {
                    Intent i = new Intent(generalTripEpliogue.this, generalWriteEpilogueActivity.class);
                    i.putExtra("generalNum", general_num);
                    i.putExtra("driverName",  list_itemArrayList.get(selectedPosition).getRecruitplace());
                    i.putExtra("Schedule", list_itemArrayList.get(selectedPosition).getRecruit());
                    i.putExtra("Region", list_schedule.get(selectedPosition).getRegion());
                    startActivity(i);
                    finish();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
            }
        });
    }

    public void getData() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("General");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                    Schedule item = new Schedule();
                    item.setSchedule_num(column.getKey());
                    item.setDeparture_date(column.child("departure_date").getValue(String.class));
                    item.setArrival_date(column.child("arrival_date").getValue(String.class));
                    item.setRegion(column.child("region").getValue(String.class));
                    item.setTimes(column.child("times").getValue(String.class));
                    item.setTaxi_driver(column.child("driver_name").getValue(String.class));

                    list_schedule.add(item);
                    reservationItem printItem = new reservationItem( item.getDeparture_date() + "~" + item.getArrival_date(), item.getTaxi_driver());
                    list_itemArrayList.add(printItem);
                    reservationAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //네비게이션
    public void naviItem() {
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.drawer_schTrip) {
                    Intent intent = new Intent(getApplicationContext(), generalMyscheActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {//toolbar의 back키 눌렀을 시
        switch (item.getItemId()) {
            case android.R.id.home: {//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        ImageButton menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }
}

