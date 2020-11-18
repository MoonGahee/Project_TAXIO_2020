package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class driverAcceptRequestActivity extends AppCompatActivity {
    Button btnAccept, btnGiveup;
    TextView title_text;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_accept_request);
        setToolbar();

        btnAccept = findViewById(R.id.btnAccept);
        btnGiveup = findViewById(R.id.btnGiveup);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(driverAcceptRequestActivity.this);
                builder.setTitle("예약 승인");
                builder.setMessage("예약을 승인하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "예약이 승인되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), driverMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "예약이 거절되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), driverMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.show();
            }
        });

        btnGiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(driverAcceptRequestActivity.this);
                builder.setTitle("예약 거절");
                builder.setMessage("예약을 거절하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "예약이 거절되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), driverMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "예약이 승인인되었습니다.", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(getApplicationContext(), driverMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.show();
            }
        });

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), driverMainActivity.class);
                startActivity(i);
                finish();
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

    public boolean onOptionsItemSelected(MenuItem item) {//toolbar의 back키 눌렀을 시
        switch (item.getItemId()){
            case android.R.id.home:{//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
