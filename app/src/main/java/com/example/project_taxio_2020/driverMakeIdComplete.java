package com.example.project_taxio_2020;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class driverMakeIdComplete extends AppCompatActivity {
    TextView nameC, emailC, birthC, phoneC;
    DatabaseReference mDatabase;
    String driver_num, namea;
    Button btnCom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_make_id_complete);
        setToolbar();
        nameC = findViewById(R.id.nameC);
        emailC = findViewById(R.id.emailC);
        birthC = findViewById(R.id.birthC);
        phoneC = findViewById(R.id.phoneC);
        btnCom = findViewById(R.id.btnCom);


        Intent i = getIntent();
        driver_num = (String)i.getSerializableExtra("driver_num");

        completeId();
        btnCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity();
            }
        });

    }

    public void completeId(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver"); //얘한테 줄거야
        ValueEventListener DriverListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Driver driver = snapshot.child(driver_num).getValue(Driver.class); //child로 경로 지정
                nameC.setText(nameC.getText().toString()+driver.getDriver_name());
                birthC.setText(birthC.getText().toString()+driver.getDriver_birth());
                phoneC.setText(phoneC.getText().toString()+driver.getDriver_call());
                emailC.setText(emailC.getText().toString()+driver.getDriver_email());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Moon-Test","error");
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(DriverListener); //콜백 한 번만 호출이 이뤄지는 경우
    }



    public void moveActivity() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    } //액티비티 이동

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }//Toolbar세팅
    final long FINISH_INTERVAK_TIME = 2000;
    long backPressedTime = 0;
    Toast toast;
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        toast  = Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);

        if (0 <= intervalTime && FINISH_INTERVAK_TIME >= intervalTime) {
            toast.cancel();
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            toast.show();
        }
    }
}
