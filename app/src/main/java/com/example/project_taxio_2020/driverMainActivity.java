package com.example.project_taxio_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringJoiner;

public class driverMainActivity extends AppCompatActivity {

    TextView title_text;
    private DrawerLayout drawerLayout;
    ListView request, help;
    NavigationView nDrawer;
    FirebaseStorage storage;
    StorageReference storageRef;
    String driver_num;
    View header;
    private generalMyScheAdapter adapter;

    reservationAdapter requestAdapter, helpAdapter;
    ArrayList<reservationItem> requestList = new ArrayList<reservationItem>();
    ArrayList<reservationItem> helpList = new ArrayList<reservationItem>();
    DatabaseReference dDatabase, mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

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
        init();
        request = findViewById(R.id.request);
        help = findViewById(R.id.help);

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

        dDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        requestAdapter = new reservationAdapter(driverMainActivity.this, requestList);
        request.setAdapter(requestAdapter);
        helpAdapter = new reservationAdapter(driverMainActivity.this, helpList);
        help.setAdapter(helpAdapter);

        request.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getApplicationContext());
                dlg.setTitle("요청 관리");
                dlg.setMessage("요청 상태를 변경하시겠습니까?");
                dlg.setPositiveButton("수락", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object m = parent.getAdapter().getItem(position);
                        String textm = String.valueOf(m);
                        int idx = textm.indexOf("-");
                        final String date = textm.substring(0, idx);
                        final String name = textm.substring(idx+1, idx+4);
                        dDatabase.child(driver_num).child("state").setValue("1");
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot column : snapshot.child(name).child("Schecule").getChildren()){
                                    String day = column.child("days").child("schedule_date").getValue(String.class);
                                    if(date.equals(day)){
                                        mDatabase.child(name).child("Schedule").child("reservation_state").setValue("1");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
                dlg.setNegativeButton("거절", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object m = parent.getAdapter().getItem(position);
                        String textm = String.valueOf(m);
                        int idx = textm.indexOf("-");
                        final String date = textm.substring(0, idx);
                        final String name = textm.substring(idx+1, idx+4);
                        dDatabase.child(driver_num).child("state").setValue("2");
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot column : snapshot.child(name).child("Schecule").getChildren()){
                                    String day = column.child("days").child("schedule_date").getValue(String.class);
                                    if(date.equals(day)){
                                        mDatabase.child(name).child("Schedule").child("reservation_state").setValue("2");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });


    }
    void init() {
        dDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.child(driver_num).child("Request").getChildren()) {
                    String recruit = column.child("day").getValue(String.class) +"-" +column.child("general_name").getValue(String.class)+" 승객님"+"     ("+ column.child("time").getValue(String.class)+")";
                    String recruit_place = column.child("course").getValue(String.class);
                    requestList.add(new reservationItem(recruit, recruit_place));
                }
                for (DataSnapshot column:snapshot.child("Request").getChildren()){
                    if(column.child("state").getValue().toString().equals("2")){
                        String recruit = column.child("day").getValue(String.class) +"-" +column.child("general_name").getValue(String.class)+" 승객님"+"     ("+ column.child("time").getValue(String.class)+")";
                        String recruit_place = column.child("course").getValue(String.class);
                        requestList.add(new reservationItem(recruit, recruit_place));
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

                if (id == R.id.drawer_chkRev) {
                    Intent intent = new Intent(getApplicationContext(), driverCheckScheActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_chkEpi) {
                    Intent intent = new Intent(getApplicationContext(), driverCheckEpilogueActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), DriverSetting.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                } else if(id==R.id.drawer_sche){
                    Intent intent = new Intent(getApplicationContext(), driverScheActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    public void setHeaderImage() {
        final TextView userName = header.findViewById(R.id.userName);
        final de.hdodenhof.circleimageview.CircleImageView profile_pic = header.findViewById(R.id.profile_pic);

        DatabaseReference gDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        gDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                    if (driverSnapshot.getKey().equals(driver_num)) {
                        userName.setText(driverSnapshot.child("driver_name").getValue().toString());
                        storage = FirebaseStorage.getInstance();
                        storageRef = storage.getReferenceFromUrl("gs://taxio-b186e.appspot.com/driver/" + driverSnapshot.child("driver_route").getValue().toString());
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
