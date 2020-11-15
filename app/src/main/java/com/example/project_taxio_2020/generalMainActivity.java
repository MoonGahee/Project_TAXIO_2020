package com.example.project_taxio_2020;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// 탑승자 메인 화면 by 주혜

public class generalMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    private Context context = this;
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

    String nowm = monthFormat.format(currentTime);
    String nowd = dayFormat.format(currentTime);
    public String date = nowm+"월 "+nowd+"일";
    String people;

    int icon, wear_icon;

    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    RecyclerView weather_test;
    generalWeatherAdapter generalWeatherAdapter;
    ArrayList<generalWeatherItem> list;

    ForeCastManager mForeCast;

    String lon = "126.897240"; // 좌표 설정
    String lat = "37.568256";  // 좌표 설정
    generalMainActivity mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInfomation;
    String general_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //값을 받아오기
        Intent i = getIntent();
        general_num = (String)i.getSerializableExtra("general_num");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_main_activity);
        setToolbar();
        Initialize();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        Button newTripBtn; //새로운 여행 만들기
        newTripBtn = findViewById(R.id.newTripBtn);

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.general_number_people, null);
        TextView intro = (TextView) view.findViewById(R.id.people_intro);
        final EditText number = (EditText) view.findViewById(R.id.people_write);

        newTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalMainActivity.this);
                builder.setTitle("인원 수 선택");
                builder.setView(view);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(number.getText().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "값을 입력해주세요!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            people = number.getText().toString();
                            moveActivity();
                            //인원수를 어디에 저장할 것인가
                            //Intent intent = new Intent(getApplicationContext(), generalSRegionActivity.class);
                            //intent.putExtra("tripPeople", people);
                            //startActivity(intent);
                        }
                    }
                });

                builder.setNegativeButton("취소", null);

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ((ViewGroup)view.getParent()).removeView(view);
                    }
                });

                builder.show();
            }
        });
    }

    public void Initialize() {
        weather_test = findViewById(R.id.weather_test);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weather_test.setLayoutManager(layoutManager);
        list = new ArrayList<generalWeatherItem>();
        mWeatherInfomation = new ArrayList<>();
        mThis = this;
        mForeCast = new ForeCastManager(lon, lat, mThis);
        mForeCast.run();
    }

    public String PrintValue() {
        String mData = "";
        for (int i = 0; i < mWeatherInfomation.size(); i++) {
            mData = mData + mWeatherInfomation.get(i).getWeather_Name() +"\n"
                    +  "기온: " + mWeatherInfomation.get(i).getTemp_Max() + "℃/"
                    + mWeatherInfomation.get(i).getTemp_Min() + "℃" +"\n"
                    +  "체감 온도: " + mWeatherInfomation.get(i).getFeel_like_value() + "℃" +"\n";

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

    public void DataChangedToHangeul()
    {
        for(int i = 0 ; i <mWeatherInfomation.size(); i ++)
        {
            WeatherToHangeul mHangeul = new WeatherToHangeul(mWeatherInfomation.get(i));
            mWeatherInfomation.set(i,mHangeul.getHangeulWeather());
        }
    }

    public void DataToInformation()
    {
        for(int i = 0; i < mWeatherData.size(); i++)
        {
            mWeatherInfomation.add(new WeatherInfo(
                    String.valueOf(mWeatherData.get(i).get("weather_Name")),  String.valueOf(mWeatherData.get(i).get("weather_Number")), String.valueOf(mWeatherData.get(i).get("weather_Much")),
                    String.valueOf(mWeatherData.get(i).get("weather_Type")),  String.valueOf(mWeatherData.get(i).get("wind_Direction")),  String.valueOf(mWeatherData.get(i).get("wind_SortNumber")),
                    String.valueOf(mWeatherData.get(i).get("wind_SortCode")),  String.valueOf(mWeatherData.get(i).get("wind_Speed")),  String.valueOf(mWeatherData.get(i).get("wind_Name")),
                    String.valueOf(mWeatherData.get(i).get("temp_Min")),  String.valueOf(mWeatherData.get(i).get("temp_Max")),  String.valueOf(mWeatherData.get(i).get("humidity")),
                    String.valueOf(mWeatherData.get(i).get("clouds_Value")),  String.valueOf(mWeatherData.get(i).get("clouds_Sort")), String.valueOf(mWeatherData.get(i).get("Clouds_Per")),String.valueOf(mWeatherData.get(i).get("day")),
                    String.valueOf(mWeatherData.get(i).get("feel_like_value"))
            ));

        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case THREAD_HANDLER_SUCCESS_INFO :
                    mForeCast.getmWeather();
                    mWeatherData = mForeCast.getmWeather();
                    if(mWeatherData.size() ==0)
                        list.add(new generalWeatherItem(null, 0, "데이터가 없습니다.", 0));

                    DataToInformation(); // 자료 클래스로 저장,

                    String data = "";
                    DataChangedToHangeul();
                    data = PrintValue();

                    list.add(new generalWeatherItem(date, icon, data, wear_icon));

                    generalWeatherAdapter = new generalWeatherAdapter(generalMainActivity.this, list);
                    weather_test.setAdapter(generalWeatherAdapter);
                    break;
                default:
                    break;
            }
        }
    };


    // 다음 화면으로 회원 번호 전달
    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), generalSRegionActivity.class);
        intent.putExtra("general_num", general_num);
        startActivity(intent);
        finish();
    }


    //네비게이션
    public void naviItem(){
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if(id == R.id.drawer_schTrip){
                    Intent intent = new Intent(getApplicationContext(), generalSDriverActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_modify) {
                    Intent intent = new Intent(getApplicationContext(), generalModifyId.class);
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
    }

    //햄버거 버튼 및 툴바
    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
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
