package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class generalMakeId extends AppCompatActivity {
    EditText edtNameM, edtId, edtPassword, edtCheckPass, edtNum1, edtNum2, edtEmail;
    Spinner spGenderM, birthY, birthM, birthD, spinnerNum;
    Button checkId, btnEmail, btnImg, btnComplete;
    String id, password;
    ImageView photo;
    FirebaseAuth mAuth;
    String TAG ="EXCEPTION";
    public static final String pattern = "^(?=.*[a-z])(?=.*[0-9]).{8,16}$";
    Matcher m;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_make_id);

        setToolbar();
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        edtNameM = findViewById(R.id.edtNameM);
        edtId = findViewById(R.id.edtId);
        edtPassword = findViewById(R.id.edtPassword);
        edtCheckPass = findViewById(R.id.edtCheckPass);
        edtNum1 = findViewById(R.id.edtNum1);
        edtNum2 = findViewById(R.id.edtNum2);
        edtEmail = findViewById(R.id.edtEmail);

        spGenderM = findViewById(R.id.spGenderM);
        birthY = findViewById(R.id.birthY);
        birthM = findViewById(R.id.birthM);
        birthD = findViewById(R.id.birthD);
        spinnerNum = findViewById(R.id.spinnerNum);

        ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenderM.setAdapter(genderAdapter);

        final ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthY.setAdapter(yearAdapter);

        final ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthM.setAdapter(monthAdapter);

        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthD.setAdapter(dayAdapter);

        ArrayAdapter phoneAdapter = ArrayAdapter.createFromResource(this, R.array.phone, android.R.layout.simple_spinner_item);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(phoneAdapter);

        checkId = findViewById(R.id.btnid);
        btnEmail = findViewById(R.id.btnEmail);
        btnImg = findViewById(R.id.btnImg);
        btnComplete = findViewById(R.id.btnComplete);

        photo = findViewById(R.id.photo);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getGeneral_id = edtId.getText().toString();
                String getGeneral_password = edtPassword.getText().toString();
                String getGeneral_name = edtNameM.getText().toString();
                String getGeneral_sex  = spGenderM.getSelectedItem().toString();
                String getGeneral_birth = birthY.getSelectedItem().toString() + "-" + birthM.getSelectedItem().toString() + "-" + birthD.getSelectedItem().toString();
                String getGeneral_call = spinnerNum.getSelectedItem().toString() + "-" + edtNum1.getText().toString() + "-" + edtNum2.getText().toString();
                String getGeneral_email = edtEmail.getText().toString();
                //부모 전화
                // 이미지 루트 데려오기

                HashMap result = new HashMap<>();
                result.put("general_id", getGeneral_id);
                result.put("general_password", getGeneral_password);
                result.put("general_name", getGeneral_name);
                result.put("general_sex", getGeneral_sex);
                result.put("general_birth", getGeneral_birth);
                result.put("general_call", getGeneral_call);
                result.put("general_email", getGeneral_email);

                General general = new General(getGeneral_id,getGeneral_password,getGeneral_name,getGeneral_sex,getGeneral_birth,getGeneral_call,getGeneral_email,"","");

                //mDatabase.child("users").child(userId).setValue(user)
                mDatabase.child("General").child(getGeneral_id).setValue(general);
                /*.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DB","성공");
                    }
                });*/

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void join(){
        id=edtId.getText().toString();
        password=edtPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();

                            Toast.makeText(getApplicationContext(),"회원가입 완료!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "이메일을 다시 확인해주세요",
                                    Toast.LENGTH_SHORT).show();
                        }
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
