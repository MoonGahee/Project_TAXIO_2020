package com.example.project_taxio_2020;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// 장소 선택 by 주혜
public class generalSRegionActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    Toolbar toolbar;
    TextView title_text;
    ImageView map;
    ImageButton btnJeju,btnSeoul, btnBusan, btnGyungju, btnGangwon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Intent i = getIntent();
        final String general_num = (String)i.getSerializableExtra("general_num");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_region_activity);
        setToolbar();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        //DataBase
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //path자체를 그 회원의id로 넣어도 될 것

        map = findViewById(R.id.imageKorea);
        btnJeju = findViewById(R.id.btnJeju);
        btnBusan = findViewById(R.id.btnBusan);
        btnSeoul = findViewById(R.id.btnSeoul);
        btnGyungju = findViewById(R.id.btnGyungju);
        btnGangwon = findViewById(R.id.btnGangwon);

        btnGangwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("지역 확인");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnGyungju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("지역 확인");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnSeoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("지역 확인");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnBusan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("지역 확인");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnJeju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("지역 확인");
                builder.setMessage("선택하신 지역이 제주도가 맞습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //DataBase 시작
                        String region = "제주";
                        HashMap result = new HashMap<>();
                        result.put("region", region);
                        mDatabase.child(general_num).child("Schedule").setValue(result); // moon2 대신 현재 나의 id
                        //DataBase 종료

                        Intent intent = new Intent(getApplicationContext(), generalSDateActivity.class);
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
        });

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
    }

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