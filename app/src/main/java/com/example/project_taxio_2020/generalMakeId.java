package com.example.project_taxio_2020;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.project_taxio_2020.databinding.GeneralSelectRegionActivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class generalMakeId extends AppCompatActivity {
    EditText edtNameM, edtPassword, edtCheckPass, edtNum1, edtNum2, edtEmail;
    Spinner spGenderM, birthY, birthM, birthD, spinnerNum, spEmail;
    Button  btnComplete;
    TextView btnEmail, btnImg;
    String id, password;
    ImageView photo;
    private FirebaseAuth mAuth; //인증
    String TAG ="EXCEPTION";
    public static final String pattern = "^(?=.*[a-z])(?=.*[0-9]).{8,16}$";
    Matcher m;
    DatabaseReference mDatabase;
    String getGeneral_num;
    HashMap result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_make_id);

        setToolbar();

        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //General DB참조
        mAuth = FirebaseAuth.getInstance();
        // SharedPreferance 사용 코드 작성해야 함. num값 유지

        edtNameM = findViewById(R.id.edtNameM);
        edtPassword = findViewById(R.id.edtPassword);
        edtCheckPass = findViewById(R.id.edtCheckPass);
        edtNum1 = findViewById(R.id.edtNum1);
        edtNum2 = findViewById(R.id.edtNum2);
        edtEmail = findViewById(R.id.edtEmail);
        spEmail = findViewById(R.id.spEmail);

        spGenderM = findViewById(R.id.spGenderM);
        birthY = findViewById(R.id.birthY);
        birthM = findViewById(R.id.birthM);
        birthD = findViewById(R.id.birthD);
        spinnerNum = findViewById(R.id.spinnerNum);
        btnEmail = findViewById(R.id.btnEmail);

        final ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenderM.setAdapter(genderAdapter);

        ArrayAdapter emailAdapter = ArrayAdapter.createFromResource(this, R.array.email, android.R.layout.simple_spinner_item);
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEmail.setAdapter(emailAdapter);

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

        btnEmail = findViewById(R.id.btnEmail);
        btnImg = findViewById(R.id.btnImg);
        btnComplete = findViewById(R.id.btnComplete);

        photo = findViewById(R.id.photo);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //로그인 값을 저장함
                String getGeneral_email = edtEmail.getText().toString();
                String getGeneral_password = edtPassword.getText().toString();
                String getGeneral_name = edtNameM.getText().toString();
                String getGeneral_sex  = spGenderM.getSelectedItem().toString();
                String getGeneral_birth = birthY.getSelectedItem().toString() + "-" + birthM.getSelectedItem().toString() + "-" + birthD.getSelectedItem().toString();
                String getGeneral_call = spinnerNum.getSelectedItem().toString() + "-" + edtNum1.getText().toString() + "-" + edtNum2.getText().toString();
                //부모 전화 
                // 이미지 루트 데려오기

                //인증
                mAuth.createUserWithEmailAndPassword(getGeneral_email, getGeneral_password)
                        .addOnCompleteListener(generalMakeId.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                //DB에 저장하는 함수 왜 둘이 순서가 바뀌면 아니되오?
                makeId(getGeneral_email, getGeneral_password, getGeneral_name, getGeneral_sex, getGeneral_birth, getGeneral_call);
            }
        });
    }



    //로그인 값을 저장
    public void makeId(String getGeneral_email, String getGeneral_password, String getGeneral_name, String getGeneral_sex, String getGeneral_birth, String getGeneral_call){
        result = new HashMap<>();
        result.put("general_email", getGeneral_email);
        result.put("general_password", getGeneral_password);
        result.put("general_name", getGeneral_name);
        result.put("general_sex", getGeneral_sex);
        result.put("general_birth", getGeneral_birth);
        result.put("general_call", getGeneral_call);
        getNumber();
    }

    //회원 번호 부여
    public void getNumber(){
        ValueEventListener generalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for(DataSnapshot column: snapshot.getChildren())
                {
                    Log.d("KOO TEST", column.getKey());
                        while (true) {
                            if (Integer.parseInt(column.getKey()) != i) { //여기가 이상한 것 같은데
                                String getGeneral_num = Integer.toString(i);
                                result.put("general_num", getGeneral_num);
                                mDatabase.child(getGeneral_num).setValue(result);
                                break;
                            }
                    else{
                            i++;
                        }
                    }
                }
                Intent intent = new Intent(getApplicationContext(), generalSRegionActivity.class);
                //intent.putExtra("general_num", getGeneral_num);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우
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
