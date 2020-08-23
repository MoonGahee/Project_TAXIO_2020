package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteria;
import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteriaType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

// 날짜 선택 by 하은

public class generalSDateActivity extends AppCompatActivity {
    Button ok;
    com.applikeysolutions.cosmocalendar.view.CalendarView cal;
    String date;
    TextView title_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_date_activity);
        setToolbar();

        ok = findViewById(R.id.ok);
        title_text = findViewById(R.id.title_text);
        cal = findViewById(R.id.cal);
        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class);
                startActivity(i);
                finish();
            }
        });


        //cal.setMinDate(myCal.getTimeInMillis());//최소선택날은 오늘
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                dlg.setTitle("일정 확인");
                dlg.setMessage(date+"이 선택한 일정이 맞습니까?");
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), generalSTaxiActivity.class);
                        i.putExtra("date", date);
                        startActivity(i);
                        finish();
                    }
                });
                dlg.setNegativeButton("아니오", null);
                dlg.show();

            }
        });//확인 버튼 눌렀을 때 다이얼로그

        Date current = Calendar.getInstance().getTime();
        SimpleDateFormat day = new SimpleDateFormat("dd", Locale.KOREA);
        int today = Integer.parseInt(day.format(current));
        cal.setDisabledDaysCriteria(new DisabledDaysCriteria(1, today, DisabledDaysCriteriaType.DAYS_OF_MONTH));
        //오늘 날짜까지 선택 불가

    }



    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
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
