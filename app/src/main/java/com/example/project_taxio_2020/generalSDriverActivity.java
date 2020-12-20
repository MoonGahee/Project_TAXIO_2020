package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    String general_num, schedule_num, date, name, trunk, sex, seat, cost, pic;
    RadioGroup rg1, rg2, rg3;
    RadioButton noGender, manDriver, womanDriver, allTrunk, yesTrunk, noTrunk, under4, under6, over6;
    Button searchBtn;
    TextView title_text, search_result, tripdate;
    Toolbar toolbar;
    RecyclerView recyclerView_driver;
    List<String> listDriverEmail = new ArrayList<>();
    List<String> listDriverName = new ArrayList<>();
    List<String> listDriverSex = new ArrayList<>();
    List<String> listDriverSeat = new ArrayList<>();
    List<String> listDriverTrunk = new ArrayList<>();
    List<String> listDriverPrice = new ArrayList<>();
    List<String> listDriverInfo = new ArrayList<>();
    List<String> listDriverImg = new ArrayList<>();

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

        init();
        driverSet();

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
                    //search_result.setText(listDriverName.size()+"분의 기사님이 기다리고 계십니다.");
                    search_result.setVisibility(View.VISIBLE);
                    recyclerView_driver.setVisibility(View.VISIBLE);
                    selectDriver();

                }
            }
        });
        //RecyclerView 사용

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
                    pic = driverSnapshot.child("driver_route").getValue(String.class);
                    listDriverEmail.add(driverSnapshot.child("driver_email").getValue(String.class)) ;
                    listDriverImg.add(pic);
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

        adapter.getnum(general_num, schedule_num);

        recyclerView_driver.setAdapter(adapter);
        recyclerView_driver.addOnItemTouchListener(onItemTouchListener);
    }

    public void getData(int position){ //임시 데이터값 추가

        generalDriverItem data = new generalDriverItem();

        data.setDriverName(listDriverName.get(position));

        data.setDriverInfo(listDriverInfo.get(position));
        data.setDirverPrice(listDriverPrice.get(position));
        data.setDriverPhoto(listDriverImg.get(position));

        adapter.addData(data); //RecyclerRecruitDriver.java의 addData로 값을 전달함
        adapter.notifyDataSetChanged(); //adapter값이 변경되었음

        Log.d("test", "4");
    }

    public void selectDriver(){
        switch(rg1.getCheckedRadioButtonId()){
            case R.id.noGender:
                for(int i = 0; i<listDriverName.size();i++){
                    selectTrunck(i);
                }
                break;
            case R.id.manDriver:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverSex.get(i).equals("남"))
                        selectTrunck(i);
                }
                break;
            case R.id.womanDriver:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverSex.get(i).equals("여"))
                        selectTrunck(i);
                }
                break;
        }
        Log.d("test", "1");
    }
    public void selectTrunck(int position){
        switch(rg2.getCheckedRadioButtonId()){
            case R.id.allTrunk:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverTrunk.get(position).equals("사용 불가능") || listDriverTrunk.get(position).equals("사용 가능")){
                        selectSeat(position);
                        break;
                    }
                }
                break;
            case R.id.noTrunk:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverTrunk.get(position).equals("사용 불가능")){
                        selectSeat(position);
                        break;
                    }
                }
                break;
            case R.id.yesTrunk:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverTrunk.get(position).equals("사용 가능")){
                        selectSeat(position);
                        break;
                    }
                }
                break;
        }
        Log.d("test", "2");
    }
    public void selectSeat(int position){
        switch(rg3.getCheckedRadioButtonId()){
            case R.id.under4:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverSeat.get(position).equals("4") || listDriverSeat.get(position).equals("6")){
                        getData(position);
                        break;
                    }
                }
                break;
            case R.id.under6:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverSeat.get(position).equals("4")){
                        getData(position);
                        break;
                    }
                }
                break;
            case R.id.over6:
                for(int i = 0; i<listDriverName.size();i++){
                    if (listDriverSeat.get(position).equals("6")){
                        getData(position);
                        break;
                    }
                }
                break;
        }
        Log.d("test", "3");
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

    RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            if (MotionEvent.ACTION_DOWN == e.getAction())
                recyclerView_driver.requestDisallowInterceptTouchEvent(true);
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };
}