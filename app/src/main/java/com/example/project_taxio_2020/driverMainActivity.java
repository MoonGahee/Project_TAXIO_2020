package com.example.project_taxio_2020;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;

public class driverMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    DatabaseReference dDatabase;
    String driver_num;
    MaterialCalendarView cal1;
    private generalMyScheAdapter adapter;

    FirebaseStorage storage;
    StorageReference storageRef;
    Button btnD;
    mainTripAdapter mainTripAdapter;
    ArrayList<mainTripItem> lists = new ArrayList<>();
    View header;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_main_activity);
        setToolbar();

        //값을 받아오기
        Intent i = getIntent();
        driver_num = i.getStringExtra("driver_num");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        header = nDrawer.getHeaderView(0);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();
        setHeaderImage();

        dDatabase = FirebaseDatabase.getInstance().getReference("Driver");


        cal1 = findViewById(R.id.cal1);
        btnD = findViewById(R.id.btnD);

        init();
        ValueEventListener scheduleListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                for (DataSnapshot column : snapshot.child(driver_num).child("Drvier_Schedule").getChildren()) {
                    final Drvier_Schedule data = new Drvier_Schedule();
                    data.setDays(column.child("day").getValue(String.class));
                    data.setCourse(column.child("course").getValue(String.class));
                    data.setGeneral_name(column.child("general_name").getValue(String.class));
                    data.setTime(column.child("time").getValue(String.class));
                    data.setStart_time(column.child("start_time").getValue(String.class));
                    cal1.addDecorator(new DayViewDecorator() {
                        Calendar customDay = Calendar.getInstance();
                        Calendar couseDay = Calendar.getInstance();

                        @Override
                        public boolean shouldDecorate(CalendarDay day) {
                            try {
                                customDay.setTime(yearFormat.parse(yearFormat.format(day.getCalendar().getTime())));
                                couseDay.setTime(yearFormat.parse(data.getDays()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return true;
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
        dDatabase.addListenerForSingleValueEvent(scheduleListener);

        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/@33.3710638,126.5672003,11.25z?hl=ko"));
                startActivity(intent);
            }
        });

        cal1.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                final Calendar selectCal = date.getCalendar();
                adapter.clearItem();
                adapter.notifyDataSetChanged();

                dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SimpleDateFormat yearFormat = new SimpleDateFormat("MM월 dd일");
                        try {
                            selectCal.setTime(yearFormat.parse(yearFormat.format(selectCal.getTime())));
                            for (DataSnapshot column : snapshot.child(driver_num).child("Driver_Schedule").getChildren()) {
                                    String scheduleDate = column.child("day").getValue(String.class);
                                    Calendar startCal = Calendar.getInstance();
                                    startCal.setTime(yearFormat.parse(scheduleDate));
                                    if (selectCal.compareTo(startCal) == 0) {
                                        Date_Schedule drvier_schedule = new Date_Schedule();
                                        drvier_schedule.setGeneral_num(column.child("general_name").getValue(String.class)+" 승객님 "+column.child("time").getValue(String.class));
                                        drvier_schedule.setSchedule_date(column.child("course").getValue(String.class));
                                        adapter.addItem(drvier_schedule);
                                        adapter.notifyDataSetChanged();
                                        return;
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

    }

    public void init() {
        RecyclerView tripRecycler = findViewById(R.id.tripRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tripRecycler.setLayoutManager(linearLayoutManager);
        adapter = new generalMyScheAdapter();
        tripRecycler.setAdapter(adapter);
        tripRecycler.setHasFixedSize(true);
        final Date currentTime = Calendar.getInstance().getTime();
        cal1.setSelectedDate(currentTime);
        dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.child(driver_num).child("Driver_Schedule").getChildren()) {
                    if(column.child("day").getValue().equals(currentTime)) {
                        String scheduleDate = column.child("day").getValue(String.class);
                        Date_Schedule drvier_schedule = new Date_Schedule();
                        drvier_schedule.setGeneral_num(column.child("general_name").getValue(String.class) + " 승객님 " + column.child("time").getValue(String.class));
                        drvier_schedule.setSchedule_date(column.child("course").getValue(String.class));
                        adapter.addItem(drvier_schedule);
                        adapter.notifyDataSetChanged();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    public void naviItem() {
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.drawer_chkRes) {
                    Intent intent = new Intent(getApplicationContext(), driverMyScheActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_chkRev) {
                    Intent intent = new Intent(getApplicationContext(), driverCheckScheActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.drawer_chkEpi){
                    Intent intent = new Intent(getApplicationContext(), driverCheckEpilogueActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                }else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), DriverSetting.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    public void setHeaderImage(){
        final TextView userName = header.findViewById(R.id.userName);
        final de.hdodenhof.circleimageview.CircleImageView profile_pic = header.findViewById(R.id.profile_pic);

        DatabaseReference gDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        gDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                    if(driverSnapshot.getKey().equals(driver_num)) {
                        userName.setText(driverSnapshot.child("driver_name").getValue().toString());
                        storage = FirebaseStorage.getInstance();
                        storageRef = storage.getReferenceFromUrl("gs://taxio-b186e.appspot.com/driver/"+driverSnapshot.child("driver_route").getValue().toString());
                        GlideApp.with(getApplicationContext()).load(storageRef).into(profile_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
