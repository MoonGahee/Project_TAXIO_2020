package com.example.project_taxio_2020;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
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
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringJoiner;


public class generalMyscheActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    DatabaseReference mDatabase;
    String general_num;
    MaterialCalendarView cal1;
    private generalMyScheAdapter adapter;
    ArrayList<mainTripItem> scheduleDataList = new ArrayList<mainTripItem>();
    //Schedule day;

    protected void onCreate(@Nullable Bundle savedInstanceState) {//관광택시 이용시간에 따라 시작가능 시간 설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_mysche_activity);
        setToolbar();
        drawerLayout = findViewById(R.id.drawerLayout);
        nDrawer = findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();
        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        //값 받아오기
        Intent i = getIntent();
        general_num = (String) i.getSerializableExtra("general_num");
        cal1 = findViewById(R.id.cal1);
        ValueEventListener scheduleListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                    final Schedule data = new Schedule();
                    data.setSchedule_num(column.getKey());
                    data.setDeparture_date(column.child("departure_date").getValue(String.class));
                    data.setArrival_date(column.child("arrival_date").getValue(String.class));
                    data.setTimes(column.child("times").getValue(String.class));
                    data.setRegion(column.child("region").getValue(String.class));

                    cal1.addDecorator(new DayViewDecorator() {
                        Calendar customDay = Calendar.getInstance();
                        Calendar arrivalDay = Calendar.getInstance();
                        Calendar departureDay = Calendar.getInstance();

                        @Override
                        public boolean shouldDecorate(CalendarDay day) {
                            try {
                                customDay.setTime(yearFormat.parse(yearFormat.format(day.getCalendar().getTime())));
                                departureDay.setTime(yearFormat.parse(data.getDeparture_date()));
                                arrivalDay.setTime(yearFormat.parse(data.getArrival_date()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return customDay.compareTo(departureDay) >= 0 && customDay.compareTo(arrivalDay) <= 0;
                        }

                        @Override
                        public void decorate(DayViewFacade view) {
                            view.addSpan(new ForegroundColorSpan(Color.GREEN){});
                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(scheduleListener);
        // 시작일, 종료일 가져오기

        cal1.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                final Calendar selectCal = date.getCalendar();
                adapter.clearItem();
                adapter.notifyDataSetChanged();

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SimpleDateFormat yearFormat = new SimpleDateFormat("MM월 dd일");

                        try {
                            selectCal.setTime(yearFormat.parse(yearFormat.format(selectCal.getTime())));

                            for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                                for (DataSnapshot column2 : column.child("days").getChildren()) {
                                    DataSnapshot dateSchedule = column2.child("Date_Schedule");
                                    DataSnapshot dateCourse = column2.child("Date_Course");

                                    String scheduleDate = dateSchedule.child("schedule_date").getValue(String.class);
                                    Calendar startCal = Calendar.getInstance();

                                    startCal.setTime(yearFormat.parse(scheduleDate));

                                    if (selectCal.compareTo(startCal) == 0) {

                                        Date_Schedule dateScheduleItem = new Date_Schedule();
                                        dateScheduleItem.setSchedule_num(column.getKey());
                                        dateScheduleItem.setGeneral_num(general_num);
                                        dateScheduleItem.setSchedule_date(scheduleDate);
                                        dateScheduleItem.setStart_time(dateSchedule.child("start_time").getValue(String.class));
                                        dateScheduleItem.setTaxi_time(dateSchedule.child("taxi_time").getValue(String.class));
                                        dateScheduleItem.setBoarding_status(dateSchedule.child("boarding_status").getValue(Boolean.class));

                                        StringJoiner lists = new StringJoiner("-");
                                        for (DataSnapshot couresPlace : dateCourse.getChildren()) {
                                            lists.add(couresPlace.child("coures_place").getValue(String.class));
                                        }
                                        String list = lists.toString();

                                        Date_Schedule item = new Date_Schedule();
                                        item.setGeneral_num(dateScheduleItem.getPrintText());
                                        item.setSchedule_date(list);
                                        adapter.addItem(item);
                                        adapter.notifyDataSetChanged();

                                        return;
                                    }
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        init();
    }


    public void init() {
        RecyclerView tripRecycler = findViewById(R.id.tripRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tripRecycler.setLayoutManager(linearLayoutManager);
        adapter = new generalMyScheAdapter();
        tripRecycler.setAdapter(adapter);
        tripRecycler.setHasFixedSize(true);
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
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    //툴바
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

