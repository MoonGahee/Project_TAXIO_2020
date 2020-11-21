package com.example.project_taxio_2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class introActivity extends AppCompatActivity {
    ImageView introTaxio;
    final String strDriver = "Driver";
    final String strGeneral = "General";
    DatabaseReference gDatabase, dDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        
        //inflating
        introTaxio = (ImageView)findViewById(R.id.introTaxio);
        //띄우기
        Glide.with(this).load(R.raw.introreal).into(introTaxio);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!autoLogin()) { //자동로그인이 아닐경우 로그인 창으로 이동
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    public boolean autoLogin() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String strDriverOrGeneral = getPreference();
        //최근 로그인한 유저가 있고 해당 이메일이 인증이 된 경우 자동로그인
        if (user != null && user.isEmailVerified() && !(strDriverOrGeneral.length() == 0)) {
            login(user.getEmail(), strDriverOrGeneral);
            return true;
        } else {
            return false;
        }
    }

    public void login(String email, String strFlag) {
        switch (strFlag) {
            case strDriver:
                dMoveActivity(email);
                break;
            case strGeneral:
                gMoveActivity(email);
                break;
        }
    }

    public String getPreference() {
        SharedPreferences driverOrGeneral = getSharedPreferences("preferenceFile", MODE_PRIVATE);
        String tempDriverOrGeneral = driverOrGeneral.getString("driverOrGeneral", "");
        return tempDriverOrGeneral;
    }

    //이동하기
    public void gMoveActivity(final String email) {
        gDatabase = FirebaseDatabase.getInstance().getReference("General");

        //데이터베이스에서 이메일과 동일한 아이를 찾아서 거기 general_num 값을 인텐트로 계속 전달전달
        gDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot general : snapshot.getChildren()) {
                    if (general.child("general_email").getValue().toString().equals(email)) {
                        Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                        intent.putExtra("general_num", general.getKey());
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void dMoveActivity(final String email) {
        dDatabase = FirebaseDatabase.getInstance().getReference("Driver");

        //데이터베이스에서 이메일과 동일한 아이를 찾아서 거기 driver_num 값을 인텐트로 계속 전달전달
        dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driver : snapshot.getChildren()) {
                    if (driver.child("driver_email").getValue().toString().equals(email)) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("driver_num", driver.getKey());
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
