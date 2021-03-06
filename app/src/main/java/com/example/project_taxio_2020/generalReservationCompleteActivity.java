package com.example.project_taxio_2020;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 예약 완료 화면 by 가희

public class generalReservationCompleteActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    TextView title_text;
    TextView time, name, taxi, date, course;
    String tripdate, general_num, schedule_num;
    DatabaseReference mDatabase,dDatabase;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";
    NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_reservation_complete_activity);
        setToolbar();
        setHomeBtn();

        Intent intent = getIntent();
        general_num = (String) intent.getSerializableExtra("general_num");
        schedule_num = (String) intent.getSerializableExtra("schedule_num");
        tripdate = intent.getStringExtra("tripDate");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        Button goMain; //새로운 여행 만들기
        goMain = findViewById(R.id.goMain);

        name = findViewById(R.id.name);
        course = findViewById(R.id.course);
        date = findViewById(R.id.date);
        taxi = findViewById(R.id.taxi);
        time = findViewById(R.id.time);
        date.setText(tripdate);
        dataActivity();

        goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoti();
                Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                intent.putExtra("general_num", general_num);
                startActivity(intent);
                finish();
            }
        });
    }

    public void dataActivity() {
        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        dDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        ValueEventListener generalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                General general = snapshot.child(general_num).getValue(General.class); //child로 경로 지정
                Schedule schedule = snapshot.child(general_num).child("Schedule").child(schedule_num).getValue(Schedule.class);
                name.setText(general.getGeneral_name());
                course.setText(schedule.getRegion()+ "에서 "+(Integer.parseInt(schedule.getTimes())-1)+"박 "+schedule.getTimes()+"일");
                date.setText(schedule.getPrintDate());
                Log.d("CJW_test",snapshot.child(general_num).child("Schedule").child(schedule_num).child("driver_name").getValue(String.class));
                taxi.setText(snapshot.child(general_num).child("Schedule").child(schedule_num).child("driver_name").getValue(String.class));
                //time.setText(); 총합시간 만들어주기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Moon-Test","error");
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우
    }


    public void showNoti() {
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel
                    (new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            // 하위 버전일 경우
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        // 알림창 제목
        builder.setContentTitle("예약 완료");
        // 알림창 메시지
        builder.setContentText("일정 예약이 완료되었습니다!");
        builder.setSmallIcon(R.drawable.bell);
        Notification notification = builder.build();
        // 알림창 실행
        manager.notify(1, notification);
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

    public void setHomeBtn() {
        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class); //삭제 후 홈으로 돌아가기
                i.putExtra("general_num", general_num);
                startActivity(i);
                finish();
            }
        });
    }
}
