package com.example.project_taxio_2020;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class findPw extends AppCompatActivity {
    RadioGroup rdg;
    RadioButton rdoD, rdoG;
    EditText edtName, edtBirth, edtPhone, edtEmail;
    Button btnFId;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rdg = findViewById(R.id.rdg);
        rdoD = findViewById(R.id.rdoD);
        rdoG = findViewById(R.id.rdoG);
        edtName = findViewById(R.id.edtName);
        edtBirth = findViewById(R.id.edtBirth);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        btnFId = findViewById(R.id.btnFId);

    };
}
