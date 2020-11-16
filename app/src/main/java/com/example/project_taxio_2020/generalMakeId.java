package com.example.project_taxio_2020;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.loader.content.CursorLoader;

import com.example.project_taxio_2020.databinding.GeneralSelectRegionActivityBinding;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class generalMakeId extends AppCompatActivity {
    private final int GALLERY_CODE = 10;
    EditText edtNameM, edtPassword, edtCheckPass, edtNum1, edtNum2, edtEmail;
    Spinner spGenderM, birthY, birthM, birthD, spinnerNum, spEmail;
    Button btnComplete;
    TextView btnEmail, btnImg;
    ImageView photo;
    private FirebaseAuth mAuth; //인증
    private FirebaseStorage storage;
    StorageReference storageRef;
    String TAG = "EXCEPTION", imagePath;
    public static final String pattern = "^(?=.*[a-z])(?=.*[0-9]).{8,16}$";
    Matcher m;
    boolean isCorrectPassword = false;
    DatabaseReference mDatabase;
    HashMap result;

    String passwordNotice = "비밀번호 패턴을 맞춰주세요.";
    String chkPasswordNotice = "비밀번호가 일치하지 않습니다.";
    String chkNullNotice = "입력값을 다 입력해주세요.";
    String chkAutoNotice = "Authentication failed.";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_make_id);

        setToolbar();//Toolbar세팅
        setFindView();//뷰 객체화 findViewbyId 일괄처리
        setAdapter();//Adapter 세팅 일괄처리
        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //General DB참조
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);}

        //비밀번호 입력이 끝난 뒤 패턴에 맞는지 비교하기
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!checkPass(edtPassword.getText().toString())) {
                    isCorrectPassword = false;
                    Toast.makeText(getApplicationContext(), passwordNotice, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //비밀번호 확인 입력이 끝난 뒤 비밀번호와 맞는지 비교하기
        edtCheckPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (edtPassword.getText().toString().equals(edtCheckPass.getText().toString())) {
                        isCorrectPassword = true;
                    } else {
                        isCorrectPassword = false;
                        Toast.makeText(getApplicationContext(),chkPasswordNotice , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture();

            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 값을 저장함
                Uri file = Uri.fromFile(new File(imagePath));
                StorageReference ref = storageRef.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask =  ref.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ;
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ;
                    }
                });
                final String getGeneral_email = edtEmail.getText().toString() + "@" + spEmail.getSelectedItem().toString();
                final String getGeneral_password = edtPassword.getText().toString();
                final String getGeneral_name = edtNameM.getText().toString();
                final String getGeneral_sex = spGenderM.getSelectedItem().toString();
                final String getGeneral_birth = birthY.getSelectedItem().toString() + "-" + birthM.getSelectedItem().toString() + "-" + birthD.getSelectedItem().toString();
                final String getGeneral_call = spinnerNum.getSelectedItem().toString() + "-" + edtNum1.getText().toString() + "-" + edtNum2.getText().toString();
                //부모 전화
                //이미지 루트 데려오기
                if (chkNull(getGeneral_email, getGeneral_password, getGeneral_name, getGeneral_call)) {
                    //인증
                    mAuth.createUserWithEmailAndPassword(getGeneral_email, getGeneral_password)
                            .addOnCompleteListener(generalMakeId.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //이메일 인증에 성공할 경우 id를 만들어 데이터베이스상에 입력
                                        makeId(getGeneral_email, getGeneral_password, getGeneral_name, getGeneral_sex, getGeneral_birth, getGeneral_call);
                                    } else {
                                        Toast.makeText(getApplicationContext(), chkAutoNotice, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), chkNullNotice, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getPicture(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE){
            imagePath = getPath(data.getData());
            File f = new File(imagePath);
            photo.setImageURI(data.getData());
        }
    }

    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cu = cursorLoader.loadInBackground();
        int index = cu.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cu.moveToFirst();

        return cu.getString(index);
    }







    //로그인 값을 저장
    public void makeId(String getGeneral_email, String getGeneral_password, String getGeneral_name, String getGeneral_sex, String getGeneral_birth, String getGeneral_call) {
        result = new HashMap<>();
        result.put("general_email", getGeneral_email);
        result.put("general_password", getGeneral_password);
        result.put("general_name", getGeneral_name);
        result.put("general_sex", getGeneral_sex);
        result.put("general_birth", getGeneral_birth);
        result.put("general_call", getGeneral_call);
        getNumber();//회원 번호 부여
    }

    public boolean chkNull(String getGeneral_email, String getGeneral_password, String getGeneral_name, String getGeneral_call) {
        if (getGeneral_email.split("@")[0].isEmpty() || getGeneral_password.isEmpty() || getGeneral_name.isEmpty() || getGeneral_call.split("-")[1].isEmpty() || getGeneral_call.split("-")[2].isEmpty()) {
            return false;
        } else {
            return true;
        }
    }//입력값이 null인지 비교

    public void getNumber() {
        ValueEventListener generalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot column : snapshot.getChildren()) {
                    if (Integer.parseInt(column.getKey()) != i) { //여기가 이상한 것 같은데
                        break;
                    } else {
                        i++;
                    }
                }
                result.put("general_num", Integer.toString(i));
                setDatabase();//데이터베이스 값 입력
                moveActivity();//액티비티 이동
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우
    }//회원 번호 부여

    public void setDatabase() {
        mDatabase.child(result.get("general_num").toString()).setValue(result);
    }//데이터베이스 값 입력

    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), generalSRegionActivity.class);
        intent.putExtra("general_num", result.get("general_num").toString());
        startActivity(intent);
        finish();
    }//액티비티 이동

    public Boolean checkPass(String password) {
        boolean check = false;
        Pattern p = Pattern.compile(pattern);
        m = p.matcher(password);
        if (m.find()) {
            check = true;
        }
        return check;
    }//비밀번호가 패턴에 맞는지 비교

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//toolbar의 back키 눌렀을 시

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }//Toolbar세팅

    public void setFindView() {
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

        btnEmail = findViewById(R.id.btnEmail);
        btnImg = findViewById(R.id.btnImg);
        btnComplete = findViewById(R.id.btnComplete);

        photo = findViewById(R.id.photo);
    }//뷰 객체화 findViewbyId 일괄처리

    public void setAdapter() {
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
    }//Adapter 세팅 일괄처리
}
