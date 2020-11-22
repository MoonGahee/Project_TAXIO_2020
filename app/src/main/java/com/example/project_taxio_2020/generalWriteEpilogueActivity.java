package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringJoiner;

// Mypage 후기 작성 by 관우

public class generalWriteEpilogueActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;

    Button cancel_btn, registration_btn;
    Toolbar toolbar;
    HashMap result;
    TextView trip_date, trip_region, driver_name, title_text;
    String score;
    RatingBar rating;
    String name;
    int i;
    String general_num, driver_num;
    String generalName;
    String driverName, schedule, region;
    private FirebaseStorage storage;
    StorageReference storageRef;
    DatabaseReference eDatabase, mDatabase, gDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_write_epilogue_activity);
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        eDatabase = FirebaseDatabase.getInstance().getReference("Epilogue"); //얘한테 줄거야
        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //General DB참조
        //값 받아오기
        Intent i = getIntent();
        general_num = i.getStringExtra("generalNum");
        driverName = i.getStringExtra("driverName");
        schedule = i.getStringExtra("Schedule");
        region = i.getStringExtra("Region");

        cancel_btn = findViewById(R.id.cancel_btn);
        registration_btn = findViewById(R.id.registration_btn);
        trip_date = findViewById(R.id.trip_date);
        trip_region = findViewById(R.id.trip_region);
        driver_name = findViewById(R.id.driver_name);
        rating = findViewById(R.id.rating);

        setTextBox(schedule, region, driverName);


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalWriteEpilogueActivity.this);
                builder.setTitle("작성 취소");
                builder.setMessage("후기 작성을 정말 취소하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                        intent.putExtra("general_num", general_num);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "마저 다 작성해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalWriteEpilogueActivity.this);
                builder.setTitle("후기 작성");
                builder.setMessage("후기 작성을 마무리 하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setData();
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "마저 다 작성해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    public void setTextBox(String tripDate, String region, String driverName) {
        trip_date.setText(tripDate);
        trip_region.setText(region);
        driver_name.setText(driverName);
    }

    public void setData() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                generalName = snapshot.child(general_num).child("general_name").getValue(String.class);
                Date now = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
                String strNow = sdf.format(now);

                Log.d("CJW_test","기사이름 : "+driverName +", 승객이름 : "+generalName + ", 작성날짜 : "+strNow + ", 별점 : "+rating.getRating());
                //TODO; 디비에 입력하기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);

        startActivity(intent);
        finish();
    }//액티비티 이동

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