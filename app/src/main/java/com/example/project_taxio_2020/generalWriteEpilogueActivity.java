package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.StringJoiner;

// Mypage 후기 작성 by 관우

public class generalWriteEpilogueActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;

    Button cancel_btn, registration_btn;
    Toolbar toolbar;
    HashMap result;
    TextView trip_date, trip_region, driver_name, trip_couse, title_text;
    String score;
    RatingBar rating;
    String name;
    int i;
    String general_num, driver_num;
    private FirebaseStorage storage;
    StorageReference storageRef;
    DatabaseReference eDatabase, mDatabase, gDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_write_epilogue_activity);
        setToolbar();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        eDatabase = FirebaseDatabase.getInstance().getReference("Epilogue"); //얘한테 줄거야
        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //General DB참조
        gDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        //값 받아오기
        Intent i = getIntent();
        general_num = i.getStringExtra("general_num");

        cancel_btn = findViewById(R.id.cancel_btn);
        registration_btn = findViewById(R.id.registration_btn);
        trip_couse = findViewById(R.id.trip_course);
        trip_date = findViewById(R.id.trip_date);
        trip_region = findViewById(R.id.trip_region);
        driver_name = findViewById(R.id.driver_name);
        rating = findViewById(R.id.rating);


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
    public void GetData() {
        score = String.valueOf(rating.getRating());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String viewData = "1";
                int smallestDay = 999;
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                    trip_date.setText(column.child("departure_date").getValue().toString()+" ~ "+column.child("arrival_date").getValue().toString());
                }
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").child(viewData).child("days").getChildren()) {
                    DataSnapshot dateSchedule = column.child("Date_Schedule");
                    DataSnapshot dateCourse = column.child("Date_Course");

                    Date_Schedule dateScheduleItem = new Date_Schedule();
                    dateScheduleItem.setSchedule_num(viewData);
                    dateScheduleItem.setGeneral_num(general_num);
                    dateScheduleItem.setSchedule_date(dateSchedule.child("schedule_date").getValue(String.class));


                    StringJoiner lists = new StringJoiner("-");
                    for (DataSnapshot couresPlace : dateCourse.getChildren()) {
                        lists.add(couresPlace.child("coures_place").getValue(String.class));
                    }
                    String list = lists.toString();
                }

                for(DataSnapshot generalSnapshot : snapshot.getChildren()){
                    name = generalSnapshot.child("Schedule").child("driver_name").getValue().toString();
                    driver_name.setText(name);
                    trip_region.setText(generalSnapshot.child("Schedule").child("region").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        gDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.child("Driver_name").child("Driver_Schedule").child("general_name").getChildren()) {
                    driver_num = column.child("Driver_num").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        makeEpi(driver_num, general_num, general_num, score);
    }
    public void makeEpi(String getdriver_num, String getGeneral_num, String getDriver_route, String getScore) {
        result = new HashMap<>();
        result.put("driver_num", getdriver_num);
        result.put("general_num", getGeneral_num);
        result.put("driver_route", getDriver_route);
        result.put("score", getScore);

        getNumber();
    }
    public void getNumber() {
        ValueEventListener generalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot column : snapshot.getChildren()) {
                    if (Integer.parseInt(column.getKey()) != i) { //여기가 이상한 것 같은데
                        break;
                    } else {
                        i++;
                    }
                }
                //resultNum = new HashMap<>();
                result.put("epilogue_id", Integer.toString(i));
                setDatabase();//데이터베이스 값 입력
                moveActivity();//액티비티 이동
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우
    }//회원 번호 부여

    public void setDatabase() {
        mDatabase.child(result.get("epilogue_id").toString()).setValue(result);
    }//데이터베이스 값 입력

    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(),generalMainActivity.class);

        startActivity(intent);
        finish();
    }//액티비티 이동

    //네비게이션
    public void naviItem(){
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if(id == R.id.drawer_schTrip){
                    Intent intent = new Intent(getApplicationContext(), generalMyscheActivity.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                }else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
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