package com.example.project_taxio_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class driverMainActivity extends AppCompatActivity {

    TextView title_text;
    private DrawerLayout drawerLayout;
    ScrollView scroll;
    ListView request, help;
    NavigationView nDrawer;
    FirebaseStorage storage;
    StorageReference storageRef;
    String driver_num, uid;
    View header;
    private generalMyScheAdapter adapter;

    RequestAdapter requestAdapter, helpAdapter;
    ArrayList<RequestItem> requestList = new ArrayList<RequestItem>();
    ArrayList<RequestItem> helpList = new ArrayList<RequestItem>();
    DatabaseReference dDatabase, mDatabase;
    int i = 0;
    ListView dialogList;
   DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Driver");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        setToolbar();

        //값을 받아오기
        Intent i = getIntent();
        driver_num = i.getStringExtra("driver_num");
        uid = i.getStringExtra("uid");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        header = nDrawer.getHeaderView(0);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();
        setHeaderImage();

        request = findViewById(R.id.request);
        help = findViewById(R.id.help);
        scroll = findViewById(R.id.scroll);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), driverMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        dDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        requestAdapter = new RequestAdapter(driverMainActivity.this, requestList);
        requestList.add(new RequestItem("구하은 (2020년 12월 25일 - 2020년 12월 26일)", "협재해변-닭머르해안길-동문시장"));
        request.setAdapter(requestAdapter);
        helpAdapter = new RequestAdapter(driverMainActivity.this, helpList);
        helpList.add(new RequestItem("박관우(2020년 12월 25일 - 2020년 12월 26일)", "우도마을-쇠머리오름-후해석벽"));
        helpList.add(new RequestItem("문가희 (2020년 12월 27일 - 2020년 12월 28일)", "오셜록-용두암-주상절리"));
        help.setAdapter(helpAdapter);

        request.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        help.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        request.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(driverMainActivity.this);
                builder.setTitle("요청 승인");
                builder.setMessage("예약 요청을 승인하시겠습니까?");
                builder.setPositiveButton("수락",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"수락이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                requestList.remove(position);
                                requestList.add(new RequestItem("아직 요청된 일정이 없습니다.", " "));
                                requestAdapter.notifyDataSetChanged();
                                request.setAdapter(requestAdapter);

                            }
                        });
                builder.setNegativeButton("거절",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"거절이 완료되었습니다.",Toast.LENGTH_SHORT).show();

                        }});
                builder.show();
            }
        });
    }







    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        ImageButton menu = findViewById(R.id.menu);
        ImageButton chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatList.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }

    public void naviItem() {
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.drawer_chkRev) {
                    Intent intent = new Intent(getApplicationContext(), driverCheckScheActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_chkEpi) {
                    Intent intent = new Intent(getApplicationContext(), driverCheckEpilogueActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_setting) {
                    Intent intent = new Intent(getApplicationContext(), DriverSetting.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                } else if(id==R.id.drawer_sche){
                    Intent intent = new Intent(getApplicationContext(), driverScheActivity.class);
                    intent.putExtra("driver_num", driver_num);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }
    public int convertDiffDay(String strVersusDate) {
        Calendar now = Calendar.getInstance();
        Calendar versusDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        try {
            now.setTime(dateFormat.parse(dateFormat.format(now.getTime())));
            versusDate.setTime(dateFormat.parse(strVersusDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        float diffDays = -1.0f;
        if (now.compareTo(versusDate) <= 0) {
            float diffSec = (versusDate.getTimeInMillis() - now.getTimeInMillis()) * 0.001f;
            diffDays = diffSec / (24 * 60 * 60);
        }
        Log.d("CJW_test", "diffDays : " + diffDays);
        return (int) diffDays;
    }

    public void setHeaderImage() {
        final TextView userName = header.findViewById(R.id.userName);
        final de.hdodenhof.circleimageview.CircleImageView profile_pic = header.findViewById(R.id.profile_pic);

        DatabaseReference gDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        gDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                    if (driverSnapshot.getKey().equals(driver_num)) {
                        userName.setText(driverSnapshot.child("driver_name").getValue().toString());
                        storage = FirebaseStorage.getInstance();
                        storageRef = storage.getReferenceFromUrl("gs://taxio-b186e.appspot.com/driver/" + driverSnapshot.child("driver_route").getValue().toString());
                        GlideApp.with(getApplicationContext()).load(storageRef).into(profile_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
