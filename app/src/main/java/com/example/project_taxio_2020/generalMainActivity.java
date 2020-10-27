package com.example.project_taxio_2020;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

// 탑승자 메인 화면 by 주혜

public class generalMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context = this;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_main_activity);
        setToolbar();
        Initialize();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        NavigationView nDrawer = (NavigationView)findViewById(R.id.nDrawer);

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

        Button newTripBtn; //새로운 여행 만들기
        newTripBtn = findViewById(R.id.newTripBtn);

        newTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), generalSRegionActivity.class);
                startActivity(intent);
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
            mData = mData + "\n" + mWeatherInfomation.get(i).getWeather_Name() +"\n"
                    +  "풍속: " + mWeatherInfomation.get(i).getWind_Speed() + " mps" + "\n"
                    +  "최고 기온: " + mWeatherInfomation.get(i).getTemp_Max() + "℃"
                    +  " /최저 기온: " + mWeatherInfomation.get(i).getTemp_Min() + "℃" +"\n"
                    +  "체감 온도: " + mWeatherInfomation.get(i).getFeel_like_value() + "℃" +"\n"
                    +  "습도: " + mWeatherInfomation.get(i).getHumidity() + "%";
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

                    list.add(new generalWeatherItem("5월 23일", R.drawable.sun, data, R.drawable.over25));
                    list.add(new generalWeatherItem("5월 24일", R.drawable.sun, data, R.drawable.under15));

                    generalWeatherAdapter = new generalWeatherAdapter(generalMainActivity.this, list);
                    weather_test.setAdapter(generalWeatherAdapter);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                drawerLayout.openDrawer(GravityCompat.START);
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
