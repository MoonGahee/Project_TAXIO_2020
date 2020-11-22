package com.example.project_taxio_2020;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

public class driverMakeId extends AppCompatActivity {
    private final int GALLERY_CODE = 10;
    EditText edtNameM, edtPassword, edtCheckPass, edtNum1, edtTime, edtNum2, edtEmail, edtCarNum;
    Spinner spGenderM, birthY, birthM, birthD, spinnerNum, spEmail,spTrunk, spCarCount, spRegion;
    Button btnComplete;
    TextView btnEmail, btnImg;
    ImageView photo;
    int check=0, eCheck=0;
    private FirebaseAuth mAuth; //인증
    private FirebaseStorage storage;
    StorageReference storageRef;
    String TAG = "EXCEPTION", imagePath,  memberSort, imageName;
    public static final String pattern = "^(?=.*[a-z])(?=.*[0-9]).{8,16}$";
    Matcher m;
    boolean isCorrectPassword = false;
    DatabaseReference mDatabase, gDatabase;
    HashMap result, resultNum;

    String passwordNotice = "비밀번호 패턴을 맞춰주세요.";
    String chkPasswordNotice = "비밀번호가 일치하지 않습니다.";
    String chkNullNotice = "입력값을 다 입력해주세요.";
    String chkAutoNotice = "Authentication failed.";
    String signInComplete = "회원가입 완료!";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_make_id);

        setToolbar();//Toolbar세팅
        setFindView();//뷰 객체화 findViewbyId 일괄처리
        setAdapter();//Adapter 세팅 일괄처리
        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //General DB참조
        gDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        Intent i = getIntent();
        memberSort = i.getStringExtra("sort");

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);}

        //비밀번호 입력이 끝난 뒤 패턴에 맞는지 비교하기//다시해야됨
        if(!(edtPassword.getText().toString().equals(""))&&!(edtPassword.isFocused())){
            if (!checkPass(edtPassword.getText().toString())) {
                isCorrectPassword = false;
                Toast.makeText(getApplicationContext(), passwordNotice, Toast.LENGTH_SHORT).show();
            }
        }
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
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=0; eCheck=0;

                final String email = edtEmail.getText().toString() + "@" + spEmail.getSelectedItem().toString();
                gDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                            if(email.equals(driverSnapshot.child("driver_email").getValue().toString())){
                                edtEmail.setText("");
                                Toast.makeText(getApplicationContext(), "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                                eCheck++;
                            }
                            else{
                                check++;
                                eCheck++;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot generalSnapshot : snapshot.getChildren()) {
                            if(email.equals(generalSnapshot.child("general_email").getValue().toString())){
                                edtEmail.setText("");
                                Toast.makeText(getApplicationContext(), "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                                eCheck++;
                            }
                            else{
                                check++;
                                eCheck++;
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(eCheck == check){
                    Toast.makeText(getApplicationContext(), "사용가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
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
                StorageReference ref = storageRef.child("driver/"+file.getLastPathSegment());
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
                final String getdriver_email = edtEmail.getText().toString() + "@" + spEmail.getSelectedItem().toString();
                final String getdriver_password = edtPassword.getText().toString();
                final String getdriver_name = edtNameM.getText().toString();
                final String getdriver_sex = spGenderM.getSelectedItem().toString();
                final String getdriver_birth = birthY.getSelectedItem().toString() + "-" + birthM.getSelectedItem().toString() + "-" + birthD.getSelectedItem().toString();
                final String getdriver_call = spinnerNum.getSelectedItem().toString() + "-" + edtNum1.getText().toString() + "-" + edtNum2.getText().toString();
                final String getdriver_route = imagePath;
                final String getdriver_region = spRegion.getSelectedItem().toString();
                final String getdriver_carNum = edtCarNum.getText().toString();
                final String getdriver_carSeat = spCarCount.getSelectedItem().toString();
                final String getdriver_trunk = spTrunk.getSelectedItem().toString();
                final String getdriver_cost = edtTime.getText().toString();
                if (chkNull(getdriver_email, getdriver_password, getdriver_name, getdriver_call)) {
                    //인증
                    mAuth.createUserWithEmailAndPassword(getdriver_email, getdriver_password)
                            .addOnCompleteListener(driverMakeId.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //이메일 인증에 성공할 경우 id를 만들어 데이터베이스상에 입력
                                        if(check==eCheck){
                                            makeId(getdriver_email, getdriver_password, getdriver_name, getdriver_sex, getdriver_birth, getdriver_region,  getdriver_call, getdriver_route, getdriver_carNum, getdriver_carSeat, getdriver_trunk, getdriver_cost);}
                                        else{
                                            Toast.makeText(getApplicationContext(),"이메일 중복체크를 해주세요", Toast.LENGTH_SHORT).show();}

                                    } else {
                                        Toast.makeText(getApplicationContext(), chkAutoNotice, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), chkNullNotice, Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(), signInComplete, Toast.LENGTH_SHORT).show();
            }
        });

        //단계 터치리스너 막아버리기
        SeekBar seek_signin = (SeekBar)findViewById(R.id.progress);
        seek_signin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
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
            imageName = imagePath.substring(imagePath.lastIndexOf("/")+1);
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
    public void makeId(String getdriver_email, String getdriver_password, String getdriver_name,  String getdriver_sex, String getdriver_birth, String getdriver_region,String getdriver_call, String getdriver_route, String getdriver_carNum, String getdriver_carSeat, String getdriver_trunk, String getdriver_cost) {
        result = new HashMap<>();
        result.put("driver_email", getdriver_email);
        result.put("driver_password", getdriver_password);
        result.put("driver_name", getdriver_name);
        result.put("driver_sex", getdriver_sex);
        result.put("driver_birth", getdriver_birth);
        result.put("driver_region", getdriver_region);
        result.put("driver_call", getdriver_call);
        result.put("driver_route", getdriver_route);
        result.put("driver_carNum", getdriver_carNum);
        result.put("driver_carSeat", getdriver_carSeat);
        result.put("driver_trunk", getdriver_trunk);
        result.put("driver_cost", getdriver_cost);

        getNumber();//회원 번호 부여
    }

    public boolean chkNull(String getdriver_email, String getdriver_password, String getdriver_name, String getdriver_call) {
        if (getdriver_email.split("@")[0].isEmpty() || getdriver_password.isEmpty() || getdriver_name.isEmpty() || getdriver_call.split("-")[1].isEmpty() || getdriver_call.split("-")[2].isEmpty()) {
            return false;
        } else {
            return true;
        }
    }//입력값이 null인지 비교

    public void getNumber() {
        ValueEventListener driverListener = new ValueEventListener() {
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
                resultNum = new HashMap<>();
                resultNum.put("driver_num", Integer.toString(i));
                setDatabase();//데이터베이스 값 입력
                moveActivity();//액티비티 이동
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(driverListener); //콜백 한 번만 호출이 이뤄지는 경우
    }//회원 번호 부여

    public void setDatabase() {
        mDatabase.child(resultNum.get("driver_num").toString()).setValue(result);
    }//데이터베이스 값 입력

    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), MakeIdComplete.class);
        intent.putExtra("driver_num", resultNum.get("driver_num").toString());
        intent.putExtra("sort", memberSort);
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
        edtTime = findViewById(R.id.edtTime);

        spGenderM = findViewById(R.id.spGenderM);
        birthY = findViewById(R.id.birthY);
        birthM = findViewById(R.id.birthM);
        birthD = findViewById(R.id.birthD);
        spinnerNum = findViewById(R.id.spinnerNum);
        btnEmail = findViewById(R.id.btnEmail);

        btnEmail = findViewById(R.id.btnEmail);
        btnImg = findViewById(R.id.btnImg);
        btnComplete = findViewById(R.id.btnComplete);

        edtCarNum = findViewById(R.id.edtCarNum);
        spCarCount = findViewById(R.id.spCarCount);
        spTrunk = findViewById(R.id.spTrunk);
        spRegion = findViewById(R.id.spRegion);

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

        final ArrayAdapter countAdapter = ArrayAdapter.createFromResource(this, R.array.carpeople, android.R.layout.simple_spinner_item);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCarCount.setAdapter(countAdapter);

        final ArrayAdapter trunkAdapter = ArrayAdapter.createFromResource(this, R.array.trunk, android.R.layout.simple_spinner_item);
        trunkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrunk.setAdapter(trunkAdapter);

        final ArrayAdapter regionAdapter = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegion.setAdapter(regionAdapter);

    }//Adapter 세팅 일괄처리
}
