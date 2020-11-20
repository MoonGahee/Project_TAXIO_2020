package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
// 기사 선택 리사이클러뷰 by 가희

public class generalSDriverActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    private generalDriverAdapter adapter;
    String general_num, schedule_num, date;
    RadioGroup rg1, rg2, rg3;
    RadioButton noGender, manDriver, womanDriver, allTrunk, yesTrunk, noTrunk, under4, under6, over6;
    Button searchBtn;
    TextView title_text, search_result, tripdate;
    Toolbar toolbar;
    RecyclerView recyclerView_driver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_driver_activity);

        setToolbar();

        Intent intent = getIntent();
        general_num = (String) intent.getSerializableExtra("general_num");
        schedule_num = (String) intent.getSerializableExtra("schedule_num");
        date = intent.getStringExtra("tripDate");


        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        tripdate = findViewById(R.id.tripdate);
        tripdate.setText(date);

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

        //객체 선언
        noGender = (RadioButton)findViewById(R.id.noGender);
        manDriver = findViewById(R.id.manDriver);
        womanDriver = findViewById(R.id.womanDriver);
        allTrunk = findViewById(R.id.allTrunk);
        yesTrunk = findViewById(R.id.yesTrunk);
        noTrunk = findViewById(R.id.noTrunk);
        under4 = findViewById(R.id.under4);
        under6 = findViewById(R.id.under6);
        over6 = findViewById(R.id.over6);
        searchBtn = findViewById(R.id.search_btn);
        search_result = findViewById(R.id.search_result);
        recyclerView_driver = findViewById(R.id.recyclerView_driver);

        rg1 = findViewById(R.id.rg1);
        rg2 = findViewById(R.id.rg2);
        rg3 = findViewById(R.id.rg3);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //조회 누르면 밑에 리사이클러뷰가 뜸
                search_result.setVisibility(View.VISIBLE);
                recyclerView_driver.setVisibility(View.VISIBLE);
            }
        });
        //RecyclerView 사용
        init();
        getData();

    }

    public void init(){ //LayoutManager와 Adapter를 활용하여 (?) RecyclerView에 연결을 해주는 기능
        RecyclerView recyclerView_driver = findViewById(R.id.recyclerView_driver);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_driver.setLayoutManager(linearLayoutManager);

        adapter = new generalDriverAdapter();
        recyclerView_driver.setAdapter(adapter);
    }

    public void getData(){ //임시 데이터값 추가
        List<String> listDriverName = Arrays.asList("아이린", "이지은", "박보검","이민호", "아이유", "예린");

        List<String> listDriverInfo = Arrays.asList("성별 : 여 \n트렁크 : 유 \n4인승", "성별 : 남 \n트렁크 : 무 \n6인승", "성별 : 남 \n트렁크 : 유 \n4인승","성별 : 여 \n트렁크 : 유 \n4인승", "성별 : 여 \n트렁크 : 유 \n4인승", "성별 : 여 \n트렁크 : 유 \n4인승");

        List<String> listDriverPrice = Arrays.asList("가격 : 60,000원", "가격 : 60,000원", "가격 : 40,000원","가격 : 50,000원", "가격 : 60,000원", "가격 : 60,000원");

        List<Integer> listDriverPhoto = Arrays.asList(R.drawable.taxi, R.drawable.taxi, R.drawable.taxi,R.drawable.taxi, R.drawable.taxi, R.drawable.taxi);

        for(int i = 0; i < listDriverName.size(); i++){  //DriverData Class 객체에 set
            generalDriverItem data = new generalDriverItem();
            data.setDriverName(listDriverName.get(i));
            data.setDriverInfo(listDriverInfo.get(i));
            data.setDirverPrice(listDriverPrice.get(i));
            data.setDriverPhoto(listDriverPhoto.get(i));

            adapter.addData(data); //RecyclerRecruitDriver.java의 addData로 값을 전달함
        }

        adapter.notifyDataSetChanged(); //adapter값이 변경되었음
    }

    public void selectDriver(){
        switch(rg1.getCheckedRadioButtonId()){
            case R.id.noGender:
                selectTrunck();
                break;
            case R.id.manDriver:
                selectTrunck();
                break;
            case R.id.womanDriver:
                selectTrunck();
                break;
        }

    }
    public void selectTrunck(){
        switch(rg2.getCheckedRadioButtonId()){
            case R.id.allTrunk:
                selectSeat();
                break;
            case R.id.noTrunk:
                selectSeat();
                break;
            case R.id.yesTrunk:
                selectSeat();
                break;
        }
    }
    public void selectSeat(){
        switch(rg1.getCheckedRadioButtonId()){
            case R.id.noGender:
                break;
            case R.id.manDriver:
                break;
            case R.id.womanDriver:
                break;
        }
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
                    Intent intent = new Intent(getApplicationContext(), generalMyscheActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

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
