package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MakeIdComplete extends AppCompatActivity {
    TextView nameC, emailC, birthC, phoneC;
    DatabaseReference mDatabase;
    String general_num, memberSort, driver_num;
    Button btnCom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_id_complete);
        setToolbar();
        nameC = findViewById(R.id.nameC);
        emailC = findViewById(R.id.emailC);
        birthC = findViewById(R.id.birthC);
        phoneC = findViewById(R.id.phoneC);
        btnCom = findViewById(R.id.btnCom);

        Intent i = getIntent();
        general_num = (String)i.getSerializableExtra("general_num");
        driver_num = i.getStringExtra("driver_num");
        memberSort = i.getStringExtra("sort");

        if(memberSort.equals("general"))
            gCompleteId();
        else if(memberSort.equals("driver"))
            dCompleteId();

        btnCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity();
            }
        });


    }

    public void gCompleteId(){
        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //얘한테 줄거야
        ValueEventListener generalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                General general = snapshot.child(general_num).getValue(General.class); //child로 경로 지정
                nameC.setText(nameC.getText().toString()+general.getGeneral_name());
                birthC.setText(birthC.getText().toString()+general.getGeneral_birth());
                phoneC.setText(phoneC.getText().toString()+general.getGeneral_call());
                emailC.setText(emailC.getText().toString()+general.getGeneral_email());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Moon-Test","error");
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우
    }
    public void dCompleteId(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Driver"); //얘한테 줄거야
        ValueEventListener generalListener = new ValueEventListener() {
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
        mDatabase.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우
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

}
