package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class generalLoginActivity extends AppCompatActivity {
    EditText edtId, edtPw;
    Button btnLogin;
    FirebaseAuth mAuth;
    String email, pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setToolbar();

        mAuth = FirebaseAuth.getInstance();
        edtId = findViewById(R.id.edtId);
        edtPw = findViewById(R.id.edtPw);
        btnLogin = findViewById(R.id.btnLogin);

        edtId.setText("heni1325@gmail.com");
        edtPw.setText("12341234");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtId.getText().toString();
                pw = edtPw.getText().toString();
                login(email, pw);
            }
        });



    }


    // 이메일을 비교해서 값을 다음 화면에 넘겨주는 것을 해야한다.
    public void login(String email, String pw){
        mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            sendEmailVerification();
                            Intent i = new Intent(getApplicationContext(), generalMainActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }
                });
    }
    public void sendEmailVerification(){
        if(mAuth.getCurrentUser().isEmailVerified()){
            Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {                         //해당 이메일에 확인메일을 보냄
                        Log.d("Koo TEST", "Email sent.");
                        Toast.makeText(getApplicationContext(),
                                "Verification email sent to " + mAuth.getCurrentUser().getEmail(),
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), generalLoginActivity.class);
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
    };


   //로그인 화면 툴바 삭제
   public void setToolbar(){
       Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
       setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
       actionBar.hide(); //액션바의 타이틀 삭제
    }
}
