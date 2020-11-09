package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

// 탈퇴 완료 화면 by 가희

public class generalWithdrawalCompleteActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_withdrawal_complete_activity);
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        Button thank = findViewById(R.id.wdOk);


     thank.setOnClickListener(new View.OnClickListener() { // 감사합니다 버튼 클릭시 메인으로 돌아가기 이벤트 진행
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(getApplicationContext(), generalLoginActivity.class);
             startActivity(intent);
             finish();
         }
     });


    }
        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home: {
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;
                }
            }
            return super.onOptionsItemSelected(item);
        }

        public void setToolbar(){ //툴바 활용
            Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정
            setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
            ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
            actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
            actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
        }
}