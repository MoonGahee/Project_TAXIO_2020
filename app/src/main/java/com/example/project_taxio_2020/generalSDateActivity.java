package com.example.project_taxio_2020;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applikeysolutions.cosmocalendar.dialog.CalendarDialog;
import com.applikeysolutions.cosmocalendar.dialog.OnDaysSelectionListener;
import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteria;
import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteriaType;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// 날짜 선택 by 하은

public class generalSDateActivity extends AppCompatActivity {//finish
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    Button ok;
    com.applikeysolutions.cosmocalendar.view.CalendarView cal;
    TextView title_text;
    int tripMonth, tripDay, tripYear, tripDays = 0;
    DatabaseReference mDatabase;
    String general_num, schedule_num;
    String[] tripDate;
    String date="";
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

    String nowm = monthFormat.format(currentTime);
    String nowd = dayFormat.format(currentTime);
    public String today = nowm+"월 "+nowd+"일";


    int icon, wear_icon;

    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    RecyclerView weather_test;
    generalDateWeatherAdapter generalWeatherAdapter;
    ArrayList<generalDateWeatherItem> list;

    DateForeCastManager mForeCast;
    String lon = "126.897240"; // 좌표 설정
    String lat = "37.568256";  // 좌표 설정
    generalSDateActivity mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInfomation;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_date_activity);
        setToolbar();
        setHomeBtn();

        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        //값 받아오기
        Intent i = getIntent();
        general_num = (String) i.getSerializableExtra("general_num");
        schedule_num = (String) i.getSerializableExtra("schedule_num");

        //인플레이팅
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        ok = findViewById(R.id.ok);
        title_text = findViewById(R.id.title_text);
        cal = findViewById(R.id.cal);
        Initialize();


        ok.setOnClickListener(new View.OnClickListener() {
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
                if (tripDays == 0) {
                    cal.clearSelections();
                    AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                    dlg.setTitle("알림");
                    dlg.setMessage("원하는 일정을 선택해주세요");
                    dlg.setNegativeButton("확인", null);
                    dlg.show();
                } else if (tripYear == nowYear) {
                    if (nowmonth > tripMonth) {
                        cal.clearSelections();
                        date = "";
                        tripDays = 0;
                        AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                        dlg.setTitle("알림");
                        dlg.setMessage("지난 날짜는 선택할 수 없습니다.");
                        dlg.setNegativeButton("확인", null);
                        dlg.show();
                    } else if (nowmonth == tripMonth) {
                        if (nowday > tripDay) {
                            cal.clearSelections();
                            date = "";
                            cal.clearSelections();
                            AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                            dlg.setTitle("알림");
                            dlg.setMessage("지난 날짜는 선택할 수 없습니다.");
                            dlg.setNegativeButton("확인", null);
                            dlg.show();
                        } else if (nowday == tripDay) {
                            cal.clearSelections();
                            date = "";
                            cal.clearSelections();
                            AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
                            dlg.setTitle("알림");
                            dlg.setMessage("오늘 날짜는 선택할 수 없습니다");
                            dlg.setNegativeButton("확인", null);
                            dlg.show();
                        } else
                            selectDate();
                    } else
                        selectDate();
                } else
                    selectDate();
            }
        });//확인 버튼 눌렀을 때 다이얼로그

    }

    public void Initialize() {
        weather_test = findViewById(R.id.weather_test);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weather_test.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        mWeatherInfomation = new ArrayList<>();
        mThis = this;
        mForeCast = new DateForeCastManager(lon, lat, mThis);
        mForeCast.run();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case THREAD_HANDLER_SUCCESS_INFO:
                    mForeCast.getmWeather();
                    mWeatherData = mForeCast.getmWeather();
                    if (mWeatherData.size() == 0)
                        list.add(new generalDateWeatherItem(null, 0, "데이터가 없습니다."));

                    DataToInformation(); // 자료 클래스로 저장,

                    String data = "";
                    DataChangedToHangeul();
                    data = PrintValue();

                    list.add(new generalDateWeatherItem(today, icon, data));

                    generalWeatherAdapter = new generalDateWeatherAdapter(generalSDateActivity.this, list);
                    weather_test.setAdapter(generalWeatherAdapter);
                    break;
                default:
                    break;
            }
        }
    };

    public String PrintValue() {
        String mData = "";
        for (int i = 0; i < mWeatherInfomation.size(); i++) {
            mData = mData + mWeatherInfomation.get(i).getWeather_Name() + "\n"
                    + "기온: " + mWeatherInfomation.get(i).getTemp_Max() + "℃/"
                    + mWeatherInfomation.get(i).getTemp_Min() + "℃" + "\n"
                    + "체감 온도: " + mWeatherInfomation.get(i).getFeel_like_value() + "℃" + "\n";

            if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) >= 25)
                wear_icon = R.drawable.over25;
            else if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) <= 20)
                wear_icon = R.drawable.under20;
            else if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) <= 15)
                wear_icon = R.drawable.under15;
            else if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) <= 0)
                wear_icon = R.drawable.under0;
            else
                wear_icon = 0;

            icon = mWeatherInfomation.get(i).getWeather_icon();
        }

        return mData;

    }

    public void DataChangedToHangeul() {
        for (int i = 0; i < mWeatherInfomation.size(); i++) {
            WeatherToHangeul mHangeul = new WeatherToHangeul(mWeatherInfomation.get(i));
            mWeatherInfomation.set(i, mHangeul.getHangeulWeather());
        }
    }

    public void DataToInformation() {
        for (int i = 0; i < mWeatherData.size(); i++) {
            mWeatherInfomation.add(new WeatherInfo(
                    String.valueOf(mWeatherData.get(i).get("weather_Name")), String.valueOf(mWeatherData.get(i).get("weather_Number")), String.valueOf(mWeatherData.get(i).get("weather_Much")),
                    String.valueOf(mWeatherData.get(i).get("weather_Type")), String.valueOf(mWeatherData.get(i).get("wind_Direction")), String.valueOf(mWeatherData.get(i).get("wind_SortNumber")),
                    String.valueOf(mWeatherData.get(i).get("wind_SortCode")), String.valueOf(mWeatherData.get(i).get("wind_Speed")), String.valueOf(mWeatherData.get(i).get("wind_Name")),
                    String.valueOf(mWeatherData.get(i).get("temp_Min")), String.valueOf(mWeatherData.get(i).get("temp_Max")), String.valueOf(mWeatherData.get(i).get("humidity")),
                    String.valueOf(mWeatherData.get(i).get("clouds_Value")), String.valueOf(mWeatherData.get(i).get("clouds_Sort")), String.valueOf(mWeatherData.get(i).get("Clouds_Per")), String.valueOf(mWeatherData.get(i).get("day")),
                    String.valueOf(mWeatherData.get(i).get("feel_like_value"))
            ));

        }
    }

    public void selectDate() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(generalSDateActivity.this);
        dlg.setTitle("일정 확인");
        dlg.setMessage(date + ", " + (tripDays - 1) + "박" + tripDays + "일이 선택한 일정이 맞습니까?");
        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveActivity();
                makeSchedule(Integer.toString(tripDays), tripDate[0], tripDate[1]);
            }
        });
        dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                date = "";
                cal.clearSelections();
            }
        });
        dlg.show();
    }

    public void makeSchedule(String tripDays, String startingDay, String endDay) {
        HashMap result = new HashMap<>();
        result.put("times", tripDays);
        result.put("departure_date", startingDay);
        result.put("arrival_date", endDay);
        result.put("days", tripDays); //임의의 테이블 생성
        mDatabase.child(general_num).child("Schedule").child(schedule_num).updateChildren(result); //이전 값이 날라가지 않도록 함 (region)
        moveActivity();
    }

    // 회원정보를 가지고 다음 액티비티로 이동
    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), generalSTaxiActivity.class);
        intent.putExtra("general_num", general_num);  //회원번호
        intent.putExtra("schedule_num", schedule_num);  //회원번호
        intent.putExtra("tripDays", tripDays); //여행 며칠간
        intent.putExtra("startingDay", tripDate[0]); //시작날
        intent.putExtra("endDay", tripDate[1]); //도착날
        startActivity(intent);
        finish();
    }

    public void setHomeBtn() {
        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(generalSDateActivity.this);
                builder.setTitle("예약 종료");
                builder.setMessage("지금 화면을 나가면 일정 정보는 삭제됩니다.\n그래도 괜찮으신가요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // DB에 데이터 삭제 시작
                        mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
                        // DB에 데이터 삭제 완료
                        Intent i = new Intent(getApplicationContext(), generalMainActivity.class); //삭제 후 홈으로 돌아가기
                        i.putExtra("general_num", general_num);
                        startActivity(i);
                        finish();
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
    }

    //네비게이션 > 화면 이동 일정 삭제
    public void naviItem() {
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                if (id == R.id.drawer_schTrip) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(generalSDateActivity.this);
                    builder.setTitle("예약 종료");
                    builder.setMessage("지금 화면을 나가면 일정 정보는 삭제됩니다.\n그래도 괜찮으신가요?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // DB에 데이터 삭제 시작
                            mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
                            // DB에 데이터 삭제 완료
                            Intent intent = new Intent(getApplicationContext(), generalMyscheActivity.class);
                            intent.putExtra("general_num", general_num);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else if (id == R.id.drawer_myInfo) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(generalSDateActivity.this);
                    builder.setTitle("예약 종료");
                    builder.setMessage("지금 화면을 나가면 일정 정보는 삭제됩니다.\n그래도 괜찮으신가요?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // DB에 데이터 삭제 시작
                            mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
                            // DB에 데이터 삭제 완료
                            Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                            intent.putExtra("general_num", general_num);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else if (id == R.id.drawer_setting) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(generalSDateActivity.this);
                    builder.setTitle("예약 종료");
                    builder.setMessage("지금 화면을 나가면 일정 정보는 삭제됩니다.\n그래도 괜찮으신가요?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // DB에 데이터 삭제 시작
                            mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
                            // DB에 데이터 삭제 완료
                            Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                            intent.putExtra("general_num", general_num);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });
    }

    //햄버거 버튼, 툴바 설정
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
}