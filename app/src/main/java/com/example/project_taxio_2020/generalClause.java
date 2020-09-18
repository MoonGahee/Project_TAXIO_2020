package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class generalClause extends AppCompatActivity {

    CheckBox all_check, clause1, clause2, clause3;
    Button next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_clause);
        setToolbar();

        all_check = findViewById(R.id.all_check);

        clause1 = findViewById(R.id.clause1);
        clause2 = findViewById(R.id.clause2);
        clause3 = findViewById(R.id.clause3);

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clause1.isChecked() && clause2.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), generalMakeId.class);
                    startActivity(intent);
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {//toolbar의 back키 눌렀을 시
        switch (item.getItemId()){
            case android.R.id.home:{//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }
}
