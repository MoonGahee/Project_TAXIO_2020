package com.example.project_taxio_2020;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RecyclerView listView;
    int selectedPosition = -1;
    String general_num;
    TextView average;
    reservationAdapter reservationAdapter;
    ArrayList<Schedule> list_schedule = new ArrayList<>();
    DatabaseReference Edatabase, Ddatabase;
    generalEpilogueAdapter epilogue_listAdapter = new generalEpilogueAdapter();
    generalEpilogueItem Edata;
    Float num = 0.0f;
    Float i = 0f;

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
        Edatabase = FirebaseDatabase.getInstance().getReference("Epilogue");
        Ddatabase = FirebaseDatabase.getInstance().getReference("Driver");
        init();

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

                Intent i = new Intent(generalTripEpliogue.this, generalWriteEpilogueActivity.class);
                i.putExtra("generalNum", general_num);
                i.putExtra("driverName", "박관우");
                i.putExtra("Schedule", "2020년 12월 25일 - 2020년 12월 26일");
                i.putExtra("Region", "제주");
                startActivity(i);
                finish();
            }

        });
    }

    void getDriver() {
        Ddatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.getChildren()) {
                    if(column.getKey().equals(general_num))
                        getEpilogue(column.child("driver_name").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getEpilogue(final String name) {
        Edatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.getChildren()) {
                    Epilogue item = new Epilogue();
                    item.setDriver_num(column.child("driver_name").getValue(String.class));
                    item.setGeneral_num(column.child("driver_name").getValue(String.class));
                    item.setReview(column.child("content").getValue(String.class));
                    item.setScore(Float.parseFloat(column.child("rating").getValue(String.class)));
                    item.setImage(column.child("image").getValue(String.class));

                    if(item.getDriver_num().equals(name)) {
                        Edata = new generalEpilogueItem(item.getImage(), item.getGeneral_num(), item.getScore(), item.getReview());
                    }

                    epilogue_listAdapter.addData(Edata);
                    epilogue_listAdapter.notifyDataSetChanged();

                    average(item.getScore());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void average (Float number) {
        num += number;
        i++;
        Log.d("pkw", Float.toString(num) + " " + Float.toString(i));
        average.setText(Float.toString(num / i) + "점");
    }
    void init() {
        listView = findViewById(R.id.epilogues);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        listView.setAdapter(epilogue_listAdapter);

        getDriver();

        average = findViewById(R.id.average_num);
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

                    if (item.getTravel_state().equals("0")) {
                        list_schedule.add(item);
                        reservationItem printItem = new reservationItem( item.getDeparture_date() + "~" + item.getArrival_date(), item.getTaxi_driver());

                        reservationAdapter.notifyDataSetChanged();
                    }

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

