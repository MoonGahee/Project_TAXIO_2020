package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

// 후기 by 관우 >> ?

public class generalCheckEpilogueActivity extends AppCompatActivity {
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    TextView title_text;
    Button edit_epilogue;
    RecyclerView listView;
    generalEpilogueAdapter epilogue_listAdapter;
    ArrayList<generalEpilogueItem> list_itemArrayList;
    String general_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_check_epilogue_activity);
        setToolbar();

        //값 받아오기
        Intent i = getIntent();
        general_num = i.getStringExtra("general_num");

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        naviItem();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        edit_epilogue = findViewById(R.id.edit_epilogue);
        listView = findViewById(R.id.recyclerView_drivereEpl);

        list_itemArrayList = new ArrayList<generalEpilogueItem>();

        listView.setAdapter(epilogue_listAdapter);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(generalCheckEpilogueActivity.this, generalMainActivity.class);
                i.putExtra("general_num", general_num);
                startActivity(i);
                finish();
            }
        });

        edit_epilogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(generalCheckEpilogueActivity.this, generalTripEpliogue.class);
                i.putExtra("general_num", general_num);
                startActivity(i);
                finish();
            }
        });
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
                }else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
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
