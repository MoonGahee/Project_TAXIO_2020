package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

// 예약 완료 화면 by 가희

public class generalReservationCompleteActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_reservation_complete_activity);
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView nDrawer = (NavigationView) findViewById(R.id.nDrawer);

        Button goMain; //새로운 여행 만들기
        goMain = findViewById(R.id.goMain);

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
                    Intent intent = new Intent(getApplicationContext(), generalWriteEpilogueActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });


        findViewById(R.id.goMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }
}
