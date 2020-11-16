package com.example.project_taxio_2020;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class driverCheckScheActivity extends AppCompatActivity {
    RecyclerView trip_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_accept_request);

        trip_data = findViewById(R.id.trip_data_Recycler);
    }
}
