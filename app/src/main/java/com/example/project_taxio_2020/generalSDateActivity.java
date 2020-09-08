package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applikeysolutions.cosmocalendar.dialog.CalendarDialog;
import com.applikeysolutions.cosmocalendar.dialog.OnDaysSelectionListener;
import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteria;
import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteriaType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// 날짜 선택 by 하은

public class generalSDateActivity extends AppCompatActivity {
    Button ok;
    com.applikeysolutions.cosmocalendar.view.CalendarView cal;
    TextView title_text;
    int tripMonth, tripDay, tripDays=0;
    String date="";
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

        //cal.setDisabledDaysCriteria(new DisabledDaysCriteria(1, today, DisabledDaysCriteriaType.DAYS_OF_MONTH));
        //오늘 날짜까지 선택 불가


        //선택한 날짜



           ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

                String nowm = monthFormat.format(currentTime);
                String nowd = dayFormat.format(currentTime);
                final int nowmonth = Integer.parseInt(nowm);
                final int nowday = Integer.parseInt(nowd);


                final List<Calendar> days = cal.getSelectedDates();
                for (int i = 0; i < days.size(); i++) {
                    Calendar calendar = days.get(i);
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final int month = calendar.get(Calendar.MONTH);
                    final int year = calendar.get(Calendar.YEAR);
                    String day_full = year + "년 " + (month + 1) + "월 " + day + "일";
                    if (i==0) {
                                date += (day_full + "~");
                                tripMonth = month+1;
                                tripDay = day;
                            }
                    else if (i == days.size() - 1)
                        date += day_full;
                    tripDays=days.size();
                }
                if (tripDays == 0) {
                    cal.clearSelections();
                    AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                    dlg.setTitle("알림");
                    dlg.setMessage(nowday+nowmonth+"원하는 일정을 선택해주세요");
                    dlg.setNegativeButton("확인", null);
                    dlg.show();
                } else {
                    if (nowmonth > tripMonth) {
                        cal.clearSelections();
                        date=""; tripDays=0;
                        AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                        dlg.setTitle("알림");
                        dlg.setMessage("지난 날짜는 선택할 수 없습니다.");
                        dlg.setNegativeButton("확인", null);
                        dlg.show();
                    } else if (nowmonth == tripMonth){
                        if (nowday > tripDay) {
                            cal.clearSelections();
                            date="";
                            cal.clearSelections();
                            AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                            dlg.setTitle("알림");
                            dlg.setMessage("지난 날짜는 선택할 수 없습니다.");
                            dlg.setNegativeButton("확인", null);
                            dlg.show();
                        }
                        else if(nowday==tripDay){
                            cal.clearSelections();
                            date="";
                            cal.clearSelections();
                            AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                            dlg.setTitle("알림");
                            dlg.setMessage("오늘 날짜는 선택할 수 없습니다");
                            dlg.setNegativeButton("확인", null);
                            dlg.show();
                        }
                        else{
                            AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                            dlg.setTitle("일정 확인");
                            dlg.setMessage(date + ", "+ (tripDays-1)+"박"+tripDays+"일이 선택한 일정이 맞습니까?");
                            dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getApplicationContext(), generalSTaxiActivity.class);
                                    i.putExtra("startMonth", tripMonth);
                                    i.putExtra("startDay", tripDay);
                                    startActivity(i);
                                    finish();
                                }
                            });
                            dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    date="";
                                    cal.clearSelections();
                                }
                            });
                            dlg.show();
                        }
                    }
                    else{
                        AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                        dlg.setTitle("일정 확인");
                        dlg.setMessage(date + ", "+ (tripDays-1)+"박"+tripDays+"일이 선택한 일정이 맞습니까?");
                        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), generalSTaxiActivity.class);
                                i.putExtra("days", tripDays);
                                i.putExtra("startMonth", tripMonth);
                                i.putExtra("startDay", tripDay);
                                startActivity(i);
                                finish();
                            }
                        });
                        dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                date="";
                                cal.clearSelections();
                            }
                        });
                        dlg.show();
                    }

                }
            }
        });//확인 버튼 눌렀을 때 다이얼로그




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
