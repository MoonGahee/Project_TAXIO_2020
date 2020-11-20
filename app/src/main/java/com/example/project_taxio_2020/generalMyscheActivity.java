package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.Date;
import java.util.ArrayList;


public class generalMyscheActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    DatabaseReference mDatabase;
    String general_num;
    MaterialCalendarView cal1;
    private generalMyScheAdapter adapter;
    ArrayList<Schedule> scheduleDataList = new ArrayList<Schedule>();
    String startDay, finishDay;
    //Schedule day;

    protected void onCreate(@Nullable Bundle savedInstanceState) {//관광택시 이용시간에 따라 시작가능 시간 설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_mysche_activity);
        setToolbar();
        drawerLayout = findViewById(R.id.drawerLayout);
        nDrawer = findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        naviItem();
        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        //값 받아오기
        Intent i = getIntent();
        general_num = (String) i.getSerializableExtra("general_num");
        cal1 = findViewById(R.id.cal1);
        ValueEventListener scheduleListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Moon","generalNum : "+general_num);
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                    Schedule data = new Schedule();
                    data.setSchedule_num(column.getKey());
                    data.setDeparture_date(column.child("departure_date").getValue(String.class));
                    data.setArrival_date(column.child("arrival_date").getValue(String.class));
                    data.setTimes(column.child("times").getValue(String.class));
                    data.setRegion(column.child("region").getValue(String.class));
                    startDay = data.getDeparture_date();
                    finishDay = data.getArrival_date();




                    Log.d("Moon", "scheduleNum : "+data.getSchedule_num() +" departureDate : "+ data.getDeparture_date() + " arrivalDate : "+ data.getArrival_date());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(scheduleListener);
        // 시작일, 종료일 가져오기

        //Date d = Date.valueOf(startDay);
//        Date d = Date.valueOf(startDay);
  //      cal1.setSelectedDate(d);



        //cal1.setSelectedDate(d);

        init();
        //getData();
    }


    public void init() {
        RecyclerView tripRecycler = findViewById(R.id.tripRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tripRecycler.setLayoutManager(linearLayoutManager);
        adapter = new generalMyScheAdapter();
        tripRecycler.setAdapter(adapter);
        tripRecycler.setHasFixedSize(true);
    }

    /*public void getData() {
        ValueEventListener dateListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date_Schedule date_schedule = snapshot.child(general_num).child("Schedule").child("schedule_num").child("days").getValue(Date_Schedule.class);
                Schedule day = snapshot.child(general_num).child("Schdule").getValue(Schedule.class);
                int i = 1;
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                    if (Integer.parseInt(column.getKey()) != day.getTimes().length()) { //여기가 이상한 것 같은데
                        date_schedule.getSchedule_date();
                        Log.d("moon",date_schedule.getSchedule_date());
                    } else {
                        i++;
                    }
                }
                adapter.addItem(date_schedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Moon-Test", "error");
            }
        };
        mDatabase.addListenerForSingleValueEvent(dateListener);
        adapter.notifyDataSetChanged();
    }

    ;*/
    //콜백 한 번만 호출이 이뤄지는 경우


    //네비게이션
   /* public void naviItem() {
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.drawer_schTrip) {
                    Intent intent = new Intent(getApplicationContext(), generalSDriverActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_modify) {
                    Intent intent = new Intent(getApplicationContext(), generalModifyId.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_out) {
                    Intent intent = new Intent(getApplicationContext(), generalWriteWithdrawalActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }
*/

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

