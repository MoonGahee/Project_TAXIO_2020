package com.example.project_taxio_2020;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

// 장소 선택 by 주혜
public class generalSRegionActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    Toolbar toolbar;
    ImageView map;
    ImageButton btnJeju, btnSeoul, btnBusan, btnGyungju, btnGangwon;
    String general_num;
    DatabaseReference mDatabase;
    HashMap resultRegion;
    HashMap result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //값 받아오기
        Intent i = getIntent();
        general_num = i.getStringExtra("general_num");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_region_activity);
        setToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        //DataBase

        mDatabase = FirebaseDatabase.getInstance().getReference("General"); //path자체를 그 회원의id로 넣어도 될 것

        map = findViewById(R.id.imageKorea);
        btnJeju = findViewById(R.id.btnJeju);
        btnBusan = findViewById(R.id.btnBusan);
        btnSeoul = findViewById(R.id.btnSeoul);
        btnGyungju = findViewById(R.id.btnGyungju);
        btnGangwon = findViewById(R.id.btnGangwon);

        btnGangwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("강원");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnGyungju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("경주");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnSeoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("서울");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnBusan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("부산");
                builder.setMessage("현재 개발중인 지역입니다.");
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnJeju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalSRegionActivity.this);
                builder.setTitle("지역 확인");
                builder.setMessage("선택하신 지역이 제주도가 맞습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //DataBase 시작
                        String region = "제주";
                        result = new HashMap<>();
                        result.put("region", region);
                        //DataBase 종료
                        getNumber();

                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
    }


    public void getNumber() {
        ValueEventListener generalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").getChildren()) {
                    if (Integer.parseInt(column.getKey()) != i) { //여기가 이상한 것 같은데
                        break;
                    } else {
                        i++;
                    }
                }
                resultRegion = new HashMap<>();
                resultRegion.put("schedule_num", Integer.toString(i));
                Log.d("moon", Integer.toString(i));
                mDatabase.child(general_num).child("Schedule").child(resultRegion.get("schedule_num").toString()).setValue(result);
                //mDatabase.child(resultRegion.get("schedule_num").toString());
                moveActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //없는 경우
            }
        };
        mDatabase.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우
    }//회원 번호 부여


    // 회원정보를 가지고 다음 액티비티로 이동
    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), generalSDateActivity.class);
        intent.putExtra("general_num", general_num);
        intent.putExtra("schedule_num", resultRegion.get("schedule_num").toString());
        startActivity(intent);
        finish();
    }

    public void naviItem() {
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.drawer_schTrip) {
                    Intent intent = new Intent(getApplicationContext(), generalMyscheActivity.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), generalSetting.class);
                    intent.putExtra("general_num", general_num);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        ImageButton menu = findViewById(R.id.menu);
        TextView title_text = findViewById(R.id.title_text);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로 가기 버튼 활성화

        title_text.setClickable(true); //홈으로 가기 버튼 활성화
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class); //삭제 후 홈으로 돌아가기
                i.putExtra("general_num", general_num);
                startActivity(i);
                finish();
            }
        });
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