package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class driverMyScheActivity extends AppCompatActivity {
    TextView title_text;
    Toolbar toolbar;
    com.applikeysolutions.cosmocalendar.view.CalendarView cal;
    int tripMonth, tripDay, tripYear, tripDays = 0;
    String date = "";
    String[] tripDate;
    TextView tripDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_mysch);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        tripDetail = findViewById(R.id.tripDetail);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), driverMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        cal = findViewById(R.id.cal);

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat yearFormat = new SimpleDateFormat("yy", Locale.getDefault());
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

                String nowy = yearFormat.format(currentTime);
                String nowm = monthFormat.format(currentTime);
                String nowd = dayFormat.format(currentTime);
                final int nowYear = Integer.parseInt(nowy);
                final int nowmonth = Integer.parseInt(nowm);
                final int nowday = Integer.parseInt(nowd);

                final List<Calendar> days = cal.getSelectedDates();
                for (int i = 0; i < days.size(); i++) {
                    Calendar calendar = days.get(i);
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final int month = calendar.get(Calendar.MONTH);
                    final int year = calendar.get(Calendar.YEAR);
                    String day_full = year + "년 " + (month + 1) + "월 " + day + "일";
                    if (i == 0) {
                        date += (day_full + "~");
                        tripMonth = month + 1;
                        tripDay = day;
                    } else if (i == days.size() - 1)
                        date += day_full;
                    tripDays = days.size();
                }
                tripDate = date.split("~");

                tripDetail.setText(date);
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
