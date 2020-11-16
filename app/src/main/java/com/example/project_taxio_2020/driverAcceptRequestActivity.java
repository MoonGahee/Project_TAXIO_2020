package com.example.project_taxio_2020;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class driverAcceptRequestActivity extends AppCompatActivity {
    Button btnAccept, btnGiveup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_accept_request);

        btnAccept = findViewById(R.id.btnAccept);
        btnGiveup = findViewById(R.id.btnGiveup);
    }
}
