package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// ALL 회원가입 > 나이 선택

public class generalAgeCheck extends AppCompatActivity {

    Button over14, under14;
    String memberSort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_age_check);
        setToolbar();

        over14 = findViewById(R.id.over14);
        under14 = findViewById(R.id.under14);

        Intent i = getIntent();
        memberSort = i.getStringExtra("sort");

        over14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalClause.class);
                i.putExtra("sort", memberSort);
                startActivity(i);
            }
        });

        under14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalClause.class);
                i.putExtra("sort", memberSort);
                i.putExtra("age", 14);
                startActivity(i);
            }
        });
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }
    final long FINISH_INTERVAK_TIME = 2000;
    long backPressedTime = 0;
    Toast toast;
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        toast  = Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);

        if (0 <= intervalTime && FINISH_INTERVAK_TIME >= intervalTime) {
            toast.cancel();
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            toast.show();
        }
    }
}
