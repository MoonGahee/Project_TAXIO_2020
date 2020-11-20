package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //inflating
        introTaxio = (ImageView) findViewById(R.id.introTaxio);
        //띄우기
        Glide.with(this).load(R.raw.introreal).into(introTaxio);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!autoLogin()) { //자동로그인이 아닐경우 로그인 창으로 이동
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        //최근 로그인한 유저가 있고 해당 이메일이 인증이 된 경우 자동로그인
        if (user != null && user.isEmailVerified()) {
            login(user.getEmail());
            return true;
        } else {
            return false;
        }
    }

    public void login(String email) {
        moveActivity(email);
    }

    public void moveActivity(final String email) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("General");

        //데이터베이스에서 이메일과 동일한 아이를 찾아서 거기 general_num 값을 인텐트로 계속 전달전달
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot general : snapshot.getChildren()) {
                    if (general.child("general_email").getValue().toString().equals(email)) {
                        Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                        intent.putExtra("general_num", general.getKey());
                        startActivity(intent);
                        return;
                    }
                }
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
