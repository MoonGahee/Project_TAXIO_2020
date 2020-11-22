package com.example.project_taxio_2020;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.Objects;

// 탑승자 메인 화면 by 주혜

public class generalMainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    private Context context = this;
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
    FirebaseStorage storage;
    StorageReference storageRef;

    Integer[] images ={R.drawable.one, R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five};
    ImageView mine;
    String nowm = monthFormat.format(currentTime);
    String nowd = dayFormat.format(currentTime);
    public String date = nowm + "월 " + nowd + "일";

    int icon, wear_icon;

    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    RecyclerView weather_test;
    RecyclerView trip_data;
    generalWeatherAdapter generalWeatherAdapter;
    ArrayList<generalWeatherItem> list;
    mainTripAdapter mainTripAdapter;
    ArrayList<mainTripItem> mainTripLists = new ArrayList<>();

    ViewFlipper viewPic;
    ForeCastManager mForeCast;

    String lon = "126.897240"; // 좌표 설정
    String lat = "37.568256";  // 좌표 설정
    generalMainActivity mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInfomation;
    String general_num, name, email;
    View header;

    ArrayList<reservationItem> list_itemArrayList = new ArrayList<>();

    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //값을 받아오기
        Intent i = getIntent();
        general_num = i.getStringExtra("general_num");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_main_activity);
        setToolbar();
        GetData();
        Initialize();

        mine = findViewById(R.id.bell);
        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("moon", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast
                        String msg = "ex";
                        Log.d("moon", msg);
                        Toast.makeText(generalMainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/


        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        header = nDrawer.getHeaderView(0);
        naviItem();
        setHeaderImage();

        Button newTripBtn; //새로운 여행 만들기
        newTripBtn = findViewById(R.id.newTripBtn);


        newTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity();
            }
        });

        viewPic = findViewById(R.id.viewFlip);

        for(int image : images) {
            fllipperImages(image);
        }

    }
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewPic.addView(imageView);
        viewPic.setFlipInterval(2500);
        viewPic.setAutoStart(true);

        // animation
        viewPic.setInAnimation(this,android.R.anim.slide_in_left);
        viewPic.setOutAnimation(this,android.R.anim.slide_out_right);
    }

    public void Initialize() {
        weather_test = findViewById(R.id.weather_test);
        trip_data = findViewById(R.id.trip_data_Recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weather_test.setLayoutManager(layoutManager);


        LinearLayoutManager layoutManagers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trip_data.setLayoutManager(layoutManagers);

        mainTripAdapter = new mainTripAdapter(this, mainTripLists);
        trip_data.setAdapter(mainTripAdapter);

        list = new ArrayList<generalWeatherItem>();
        mWeatherInfomation = new ArrayList<>();
        mThis = this;
        mForeCast = new ForeCastManager(lon, lat, mThis);
        mForeCast.run();
    }

    public void GetData() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("General");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String viewData = "1";
                int smallestDay = 999;
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                    int versusDay = convertDiffDay(column.child("arrival_date").getValue(String.class));
                    if (versusDay > 0) {
                        if (versusDay < smallestDay) {
                            smallestDay = versusDay;
                            viewData = column.getKey();
                        }
                    }
                }
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").child(viewData).child("days").getChildren()) {
                    DataSnapshot dateSchedule = column.child("Date_Schedule");
                    DataSnapshot dateCourse = column.child("Date_Course");

                    Date_Schedule dateScheduleItem = new Date_Schedule();
                    dateScheduleItem.setSchedule_num(viewData);
                    dateScheduleItem.setGeneral_num(general_num);
                    dateScheduleItem.setSchedule_date(dateSchedule.child("schedule_date").getValue(String.class));
                    dateScheduleItem.setStart_time(dateSchedule.child("start_time").getValue(String.class));
                    dateScheduleItem.setTaxi_time(dateSchedule.child("taxi_time").getValue(String.class));
                    dateScheduleItem.setBoarding_status(dateSchedule.child("boarding_status").getValue(Boolean.class));


                    StringJoiner lists = new StringJoiner("-");
                    for (DataSnapshot couresPlace : dateCourse.getChildren()) {
                        lists.add(couresPlace.child("coures_place").getValue(String.class));
                    }
                    String list = lists.toString();

                    mainTripItem mtItem = new mainTripItem(dateScheduleItem.getPrintText(), list);
                    reservationItem rsItem = new reservationItem(dateScheduleItem.getPrintText(), list);

                    list_itemArrayList.add(rsItem);
                    mainTripLists.add(mtItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int convertDiffDay(String strVersusDate) {
        Calendar now = Calendar.getInstance();
        Calendar versusDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        try {
            now.setTime(dateFormat.parse(dateFormat.format(now.getTime())));
            versusDate.setTime(dateFormat.parse(strVersusDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        float diffDays = -1.0f;
        if (now.compareTo(versusDate) <= 0) {
            float diffSec = (versusDate.getTimeInMillis() - now.getTimeInMillis()) * 0.001f;
            diffDays = diffSec / (24 * 60 * 60);
        }
        Log.d("CJW_test", "diffDays : " + diffDays);
        return (int) diffDays;
    }

    public String PrintValue() {
        String mData = "";
        for (int i = 0; i < mWeatherInfomation.size(); i++) {
            mData = mData + mWeatherInfomation.get(i).getWeather_Name() + "\n"
                    + "기온: " + mWeatherInfomation.get(i).getTemp_Max() + "도/"
                    + mWeatherInfomation.get(i).getTemp_Min() + "도"+ "\n"
                    + "체감 온도: " + mWeatherInfomation.get(i).getFeel_like_value() + "도" ;

            if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) <= 0)
                wear_icon = R.drawable.under0;
            else if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) <= 15)
                wear_icon = R.drawable.under15;
            else if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) <= 20)
                wear_icon = R.drawable.under20;
            else if (Double.parseDouble(mWeatherInfomation.get(i).getFeel_like_value()) >= 25)
                wear_icon = R.drawable.over25;
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

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case THREAD_HANDLER_SUCCESS_INFO:
                    mForeCast.getmWeather();
                    mWeatherData = mForeCast.getmWeather();
                    if (mWeatherData.size() == 0)
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
        ;
    }


    //네비게이션
    public void naviItem(){

        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.drawer_schTrip) {
                    Intent intent = new Intent(getApplicationContext(), generalMyscheActivity.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                      
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalTripEpliogue.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                      
                } else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                      
                }
                return true;
            }
        });
    }

    public void setHeaderImage(){
        final TextView userName = header.findViewById(R.id.userName);
        final ImageView profile_pic = header.findViewById(R.id.profile_pic);

        DatabaseReference gDatabase = FirebaseDatabase.getInstance().getReference("General");
        gDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot generalSnapshot : snapshot.getChildren()) {
                    if(generalSnapshot.child("general_num").getKey().equals(general_num)) {
                        userName.setText(generalSnapshot.child("general_name").getValue().toString());
                        storage = FirebaseStorage.getInstance();
                        storageRef = storage.getReferenceFromUrl("gs://taxio-b186e.appspot.com/general/"+generalSnapshot.child("general_route").getValue().toString());
                        GlideApp.with(getApplicationContext()).load(storageRef).into(profile_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //햄버거 버튼 및 툴바
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
