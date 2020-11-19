package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class introActivity extends AppCompatActivity {
    ImageView introTaxio;

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
                Intent intent = new Intent(getApplicationContext(),generalLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
