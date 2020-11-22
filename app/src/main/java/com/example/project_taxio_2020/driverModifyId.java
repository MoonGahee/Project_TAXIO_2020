package com.example.project_taxio_2020;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.sql.Ref;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class driverModifyId extends AppCompatActivity {
    EditText edtPassword, edtCheckPass, edtNum1, edtNum2, edtEmail, edtCarNum, edtTime;
    Spinner spinnerNum, spTrunk, spCarCount;
    Button  btnComplete;
    String driver_num;
    String TAG ="EXCEPTION";
    public static final String pattern = "^(?=.*[a-z])(?=.*[0-9]).{8,16}$";
    Matcher m;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_modify_id);

        setToolbar();
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver");

        Intent in = getIntent();
        driver_num = in.getStringExtra("driver_num");

        edtPassword = findViewById(R.id.edtPassword);
        edtCheckPass = findViewById(R.id.edtCheckPass);
        edtNum1 = findViewById(R.id.edtNum1);
        edtNum2 = findViewById(R.id.edtNum2);
        edtEmail = findViewById(R.id.edtEmail);
        edtTime = findViewById(R.id.edtTime);

        edtCarNum = findViewById(R.id.edtCarNum);
        spCarCount = findViewById(R.id.spCarCount);
        spTrunk = findViewById(R.id.spTrunk);


        ArrayAdapter phoneAdapter = ArrayAdapter.createFromResource(this, R.array.phone, android.R.layout.simple_spinner_item);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(phoneAdapter);

        final ArrayAdapter trunkAdapter = ArrayAdapter.createFromResource(this, R.array.trunk, android.R.layout.simple_spinner_item);
        trunkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrunk.setAdapter(trunkAdapter);

        final ArrayAdapter countAdapter = ArrayAdapter.createFromResource(this, R.array.carpeople, android.R.layout.simple_spinner_item);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCarCount.setAdapter(countAdapter);

        btnComplete = findViewById(R.id.btnComplete);


        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getdriver_password = edtPassword.getText().toString();
                final String getdriver_call = spinnerNum.getSelectedItem().toString() + "-" + edtNum1.getText().toString() + "-" + edtNum2.getText().toString();
                final String getdriver_carNum = edtCarNum.getText().toString();
                final String getdriver_carSeat = spCarCount.getSelectedItem().toString();
                final String getdriver_trunk = spTrunk.getSelectedItem().toString();
                final String getdriver_cost = edtTime.getText().toString();
                //부모 전화
                // 이미지 루트 데려오기
                final HashMap result = new HashMap<>();
                if (!(getdriver_password.equals(""))) {
                    result.put("driver_password", getdriver_password);
                }
                if (!(getdriver_call.equals("010--"))) {
                    result.put("driver_call", getdriver_call);
                }
                if (!(getdriver_carNum.equals(""))) {
                    result.put("driver_carNum", getdriver_carNum);
                }
                if (!(getdriver_cost.equals(""))) {
                    result.put("driver_cost", getdriver_cost);
                }
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                            if (driverSnapshot.child("driver_num").getValue().toString().equals(driver_num)) {
                                String seat = driverSnapshot.child("driver_carSeat").getValue().toString();
                                String trunk = driverSnapshot.child("driver_trunk").getValue().toString();
                                if(!(seat.equals(getdriver_carSeat))){
                                    result.put("driver_carNum", getdriver_carSeat);
                                }
                                if(!(seat.equals(getdriver_trunk))){
                                    result.put("driver_carNum", getdriver_trunk);
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mDatabase.child("Driver").child(driver_num).updateChildren(result);
                Toast.makeText(getApplicationContext(), "개인정보 수정 완료!.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), driverMainActivity.class);
                intent.putExtra("driver_num", driver_num);
                startActivity(intent);
                finish();
            }

        });

    }


    public Boolean checkPass(String password) {
        boolean check = false;
        Pattern p = Pattern.compile(pattern);
        m=p.matcher(password);
        if(m.find()) {
            check = true;
        }
        return check;
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
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }
}

