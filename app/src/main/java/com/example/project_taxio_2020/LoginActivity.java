package com.example.project_taxio_2020;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Login by 하은

public class LoginActivity extends AppCompatActivity {
    EditText edtId, edtPw;
    Button btnLogin;
    TextView btnFId, btnFPw, btnJoin;
    FirebaseAuth mAuth;
    String email, pw;
    RadioGroup rdg;
    RadioButton rdoD, rdoG;
    DatabaseReference gDatabase, dDatabase;
    final String strDriver = "Driver";
    final String strGeneral = "General";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        edtId = findViewById(R.id.edtId);
        edtPw = findViewById(R.id.edtPw);
        btnLogin = findViewById(R.id.btnLogin);
        btnFId = findViewById(R.id.btnFId);
        btnFPw = findViewById(R.id.btnFPw);
        btnJoin = findViewById(R.id.btnJoin);
        rdg = findViewById(R.id.rdg);
        rdoD = findViewById(R.id.rdoD);
        rdoG = findViewById(R.id.rdoG);


        gDatabase = FirebaseDatabase.getInstance().getReference("General");
        dDatabase = FirebaseDatabase.getInstance().getReference("Driver");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtId.getText().toString();
                pw = edtPw.getText().toString();

                if (email == null || pw == null) {
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    switch (rdg.getCheckedRadioButtonId()) {
                        case R.id.rdoG:
                            login(email, pw);
                            break;
                        case R.id.rdoD:
                            login(email, pw);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "회원 종류를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberSort.class);
                startActivity(intent);
                finish();

            }
        });

        btnFId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), findId.class);
                startActivity(i);
                finish();
            }
        });

        btnFPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), findPw.class);
                startActivity(i);
                finish();
            }
        });
    }


    // 이메일을 비교해서 값을 다음 화면에 넘겨주는 것을 해야한다.
    public void login(final String email, final String pw) {
        mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                            //데이터를 읽어서 해당 general_num 전송기능 진행
                            if (rdg.getCheckedRadioButtonId() == R.id.rdoG) {
                                gMoveActivity(email);
                            } else if (rdg.getCheckedRadioButtonId() == R.id.rdoD) {
                                dMoveActivity(email);
                            }
                        }
                    }
                });
    }

    public void login(String email) {
        gMoveActivity(email);
    }

    public void sendEmailVerification() {
        if (!mAuth.getCurrentUser().isEmailVerified()) {
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {                         //해당 이메일에 확인메일을 보냄
                        Toast.makeText(getApplicationContext(),
                                mAuth.getCurrentUser().getEmail()+"로 인증메일을 발송했습니다.",
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), driverMainActivity.class);
                        startActivity(i);
                        finish();
                    } else {                                             //메일 보내기 실패
                        Log.e("Koo TEST", "sendEmailVerification", task.getException());
                        Toast.makeText(getApplicationContext(),
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    //이동하기
    public void gMoveActivity(final String email) {
        //데이터베이스에서 이메일과 동일한 아이를 찾아서 거기 general_num 값을 인텐트로 계속 전달전달
        gDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot general : snapshot.getChildren()) {
                    if (general.child("general_email").getValue().toString().equals(email)) {
                        Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                        intent.putExtra("general_num", general.getKey());
                        intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(intent);
                        savePreference(strGeneral);
                        finish();
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void dMoveActivity(final String email) {
        //데이터베이스에서 이메일과 동일한 아이를 찾아서 거기 driver_num 값을 인텐트로 계속 전달전달
        dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driver : snapshot.getChildren()) {
                    if (driver.child("driver_email").getValue().toString().equals(email)) {
                        Intent intent = new Intent(getApplicationContext(), driverMainActivity.class);
                        intent.putExtra("driver_num", driver.getKey());
                        intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(intent);
                        savePreference(strDriver);
                        finish();
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void savePreference(String strFlag) {
        SharedPreferences driverOrGeneral = getSharedPreferences("preferenceFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = driverOrGeneral.edit();

        switch (strFlag) {
            case strDriver:
                editor.putString("driverOrGeneral", strFlag);
                break;
            case strGeneral:
                editor.putString("driverOrGeneral", strFlag);
                break;
            default:
                break;
        }
        editor.commit();
    }
    final long FINISH_INTERVAK_TIME = 2000;
    long backPressedTime = 0;
    Toast toast;
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        toast  = Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);

        if (0 <= intervalTime && FINISH_INTERVAK_TIME >= intervalTime) {
            toast.cancel();
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            toast.show();
        }
    }
}