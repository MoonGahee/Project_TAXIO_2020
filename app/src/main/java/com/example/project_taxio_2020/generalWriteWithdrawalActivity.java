package com.example.project_taxio_2020;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class generalWriteWithdrawalActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    Button wd_ok;
    Button wd_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_write_withdrawal_activity);
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView nDrawer = (NavigationView) findViewById(R.id.nDrawer);

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("General");

        wd_ok = findViewById(R.id.wd_ok);
        wd_cancel = findViewById(R.id.wd_cancel);

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
                    Intent intent = new Intent(getApplicationContext(), generalReservationCompleteActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_out) {
                    Intent intent = new Intent(getApplicationContext(), generalWriteWithdrawalActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        wd_ok.setOnClickListener(new View.OnClickListener() { //탈퇴버튼 선택시 Dialog
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalWriteWithdrawalActivity.this);
                builder.setTitle("탈퇴하기");
                builder.setMessage("정말 탈퇴하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // DB에 데이터 삭제 시작
                        mDatabase.child("moon2").removeValue(); //moon2대신에 id를 데려오면 되지용!
                        // DB에 데이터 삭제 완료
                        Intent intent = new Intent(getApplicationContext(), generalWithdrawalCompleteActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

       wd_cancel.setOnClickListener(new View.OnClickListener() { //철회버튼 선택시 Dialog
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalWriteWithdrawalActivity.this); //getApplicationContext()이면 오류인 이유?
                builder.setTitle("탈퇴철회");
                builder.setMessage("탈퇴를 철회하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
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

    public void setToolbar() { //툴바 활용
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }
}

