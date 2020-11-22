package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// 이것도 잘 모르겠습니다.

public class generalClause extends AppCompatActivity {

    CheckBox all_check, clause1, clause2, clause3;
    Button previous, next;
    ScrollView all_scroll, show_clause1, show_clause2, show_clause3;
    int age;
    String memberSort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_clause);
        setToolbar();

        Intent i = getIntent();
        age = i.getIntExtra("age", 0);
        memberSort = i.getStringExtra("sort");


        all_check = findViewById(R.id.all_check);

        clause1 = findViewById(R.id.clause1);
        clause2 = findViewById(R.id.clause2);
        clause3 = findViewById(R.id.clause3);

        all_scroll = findViewById(R.id.all_scroll);
        show_clause1 = findViewById(R.id.show_clause1);
        show_clause2 = findViewById(R.id.show_clause2);
        show_clause3 = findViewById(R.id.show_clause3);

        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        all_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (all_check.isChecked()) {
                    clause1.setChecked(true);
                    clause2.setChecked(true);
                    clause3.setChecked(true);
                }
                else {
                    clause1.setChecked(false);
                    clause2.setChecked(false);
                    clause3.setChecked(false);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalClause.this);
                builder.setTitle("확인");
                builder.setMessage("이전 화면으로 돌아가시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), generalAgeCheck.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clause1.isChecked() && clause2.isChecked()) {
                    if(memberSort.equals("general")){
                        if(age == 14){
                            Intent intent = new Intent(getApplicationContext(), generalMakeIdChild.class);
                            intent.putExtra("sort", memberSort);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), generalMakeId.class);
                            intent.putExtra("sort", memberSort);
                            startActivity(intent);
                        }
                    }
                    else if(memberSort.equals("driver")){
                        Intent intent = new Intent(getApplicationContext(), driverMakeId.class);
                        intent.putExtra("sort", memberSort);
                        startActivity(intent);
                    }

                }
                else
                    Toast.makeText(getApplicationContext(), "필수 항목에 동의해주세요!", Toast.LENGTH_SHORT).show();
            }
        });

        show_clause1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                all_scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        show_clause2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                all_scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        show_clause3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                all_scroll.requestDisallowInterceptTouchEvent(true);
                return false;
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
