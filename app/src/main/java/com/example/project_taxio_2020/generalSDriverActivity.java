package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// 기사 선택 리사이클러뷰 by 가희

public class generalSDriverActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    private generalDriverAdapter adapter;
    String general_num, schedule_num, date, name, trunk, sex, seat, cost;
    RadioGroup rg1, rg2, rg3;
    RadioButton noGender, manDriver, womanDriver, allTrunk, yesTrunk, noTrunk, under4, under6, over6;
    Button searchBtn;
    TextView title_text, search_result, tripdate;
    Toolbar toolbar;
    RecyclerView recyclerView_driver;
    List<String> listDriverName = new ArrayList<>();
    List<String> listDriverSex = new ArrayList<>();
    List<String> listDriverSeat = new ArrayList<>();
    List<String> listDriverTrunk = new ArrayList<>();
    List<String> listDriverPrice = new ArrayList<>();
    List<String> listDriverInfo = new ArrayList<>();

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
                if(String.valueOf(rg1.getCheckedRadioButtonId()).equals("")||String.valueOf(rg2.getCheckedRadioButtonId()).equals("")||String.valueOf(rg3.getCheckedRadioButtonId()).equals("")){
                    Toast.makeText(getApplicationContext(),"원하는 조건을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    search_result.setVisibility(View.VISIBLE);
                    recyclerView_driver.setVisibility(View.VISIBLE);
                    driverSet();
                    selectDriver();
                }
            }
        });
        //RecyclerView 사용
        init();

    }
    public void driverSet(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("Driver");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                    name = driverSnapshot.child("driver_name").getValue(String.class);
                    trunk = driverSnapshot.child("driver_trunk").getValue(String.class);
                    seat = driverSnapshot.child("driver_carSeat").getValue(String.class);
                    sex = driverSnapshot.child("driver_sex").getValue(String.class);
                    cost = driverSnapshot.child("driver_cost").getValue(String.class);
                    listDriverName.add(name);
                    listDriverSex.add(sex);
                    listDriverSeat.add(seat);
                    listDriverTrunk.add(trunk);
                    listDriverPrice.add("시간당: " + cost + "원");
                    listDriverInfo.add("성별: " + sex + "\n트렁크: " + trunk + "\n" + seat + "인승");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void init(){ //LayoutManager와 Adapter를 활용하여 (?) RecyclerView에 연결을 해주는 기능
        RecyclerView recyclerView_driver = findViewById(R.id.recyclerView_driver);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_driver.setLayoutManager(linearLayoutManager);

        adapter = new generalDriverAdapter();
        recyclerView_driver.setAdapter(adapter);
    }

    public void getData(){ //임시 데이터값 추가
        List<Integer> listDriverPhoto = Arrays.asList(R.drawable.taxi);

        for(int i = 0; i < listDriverName.size(); i++){  //DriverData Class 객체에 set
            generalDriverItem data = new generalDriverItem();
            switch(rg1.getCheckedRadioButtonId()){
                case R.id.noGender:
                    data.setDriverName(listDriverName.get(i));
                    selectTrunck();
                    break;
                case R.id.manDriver:
                    data.setDriverName(listDriverName.get(i));
                    for(int j = 0; j<listDriverName.size();j++){
                        listDriverSex.get(j).equals("남");
                        selectTrunck();
                    }
                    break;
                case R.id.womanDriver:
                    data.setDriverName(listDriverName.get(i));
                    for(int j = 0; j<listDriverName.size();j++){
                        listDriverSex.equals("여");
                        selectTrunck();
                    }
                    break;
            }

            data.setDriverInfo(listDriverInfo.get(i));
            data.setDirverPrice(listDriverPrice.get(i));
            //data.setDriverPhoto(listDriverPhoto.get(i));

            adapter.addData(data); //RecyclerRecruitDriver.java의 addData로 값을 전달함
            adapter.getnum(general_num, schedule_num, date);
        }

        adapter.notifyDataSetChanged(); //adapter값이 변경되었음

    }

    public void selectDriver(){
        switch(rg1.getCheckedRadioButtonId()){
            case R.id.noGender:
                selectTrunck();
                break;
            case R.id.manDriver:
                for(int i = 0; i<listDriverName.size();i++){
                    listDriverSex.get(i).equals("남");
                    selectTrunck();
                }
                break;
            case R.id.womanDriver:
                for(int i = 0; i<listDriverName.size();i++){
                    listDriverSex.equals("여");
                    selectTrunck();
                }
                break;
        }

    }
    public void selectTrunck(){
        switch(rg2.getCheckedRadioButtonId()){
            case R.id.allTrunk:
                selectSeat();
                break;
            case R.id.noTrunk:
                for(int i = 0; i<listDriverName.size();i++){
                    listDriverTrunk.get(i).equals("사용 불가능");
                    selectSeat();
                }
                break;
            case R.id.yesTrunk:
                for(int i = 0; i<listDriverName.size();i++){
                    listDriverTrunk.get(i).equals("사용 가능");
                    selectSeat();
                }
                break;
        }
    }
    public void selectSeat(){
        switch(rg1.getCheckedRadioButtonId()){
            case R.id.under4:
                getData();
                break;
            case R.id.under6:
                for(int i = 0; i<listDriverName.size();i++){
                    listDriverSeat.get(i).equals("4");
                    getData();
                }
                break;
            case R.id.over6:
                for(int i = 0; i<listDriverName.size();i++){
                    getData();
                }
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
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
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
