package com.example.project_taxio_2020;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class driverResumeActivity extends AppCompatActivity {
    RecyclerView recyclerView_driver;
    Button search_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_resume);

        recyclerView_driver = findViewById(R.id.recyclerView_driver);
        search_btn = findViewById(R.id.search_btn);
    }
}
