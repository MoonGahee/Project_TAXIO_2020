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
        init();

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
                                dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        HashMap result = new HashMap<>();
                                        int i = 1;
                                        for (DataSnapshot column : snapshot.child(driver_num).child("Driver_Schedule").getChildren()) {
                                            if (Integer.parseInt(column.getKey()) != i) { //여기가 이상한 것 같은데
                                                break;
                                            } else {
                                                i++;
                                            }
                                        }
                                        for (final DataSnapshot request : snapshot.child(driver_num).child("Request").getChildren()) {
                                            dDatabase.child(driver_num).child("Request").child("state").setValue("1");
                                            result.put("days", request.child("RequestDay").child("day").getValue(String.class));
                                            result.put("general_name", request.child("general_name").getValue(String.class));
                                            result.put("couse", request.child("RequestDay").child("course").getValue(String.class));
                                            result.put("time", request.child("RequestDay").child("time").getValue(String.class));
                                            result.put("start_time", request.child("RequestDay").child("start_time").getValue(String.class));
                                            databaseRef.child(driver_num).child("Request").child(Integer.toString(i)).updateChildren(result);
                                            i++;
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot column : snapshot.child(request.child("general_name").getValue(String.class)).child("Schecule").getChildren()) {
                                                        String day = column.child("days").child("schedule_date").getValue(String.class);
                                                        if (request.child("RequestDay").child("day").getValue(String.class).equals(day)) {
                                                            mDatabase.child(request.child("general_name").getValue(String.class)).child("Schedule").child("reservation_state").setValue("1");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                builder.setNegativeButton("거절",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"거절이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                dDatabase.child(driver_num).child("Request").child(String.valueOf(position)).child("state").setValue("2");
                                dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (final DataSnapshot request : snapshot.child(driver_num).child("Request").getChildren()) {
                                            if (request.getKey().equals(String.valueOf(position))) {
                                                final String name = request.child("general_name").getValue(String.class);
                                                final String date = request.child("RequestDay").child("day").getValue(String.class);
                                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot column : snapshot.child(name).child("Schecule").getChildren()) {
                                                            String day = column.child("days").child("schedule_date").getValue(String.class);
                                                            if (date.equals(day)) {
                                                                mDatabase.child(name).child("Schedule").child("reservation_state").setValue("2");
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                builder.show();
            }
        });
    }

    void init() {

        dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String viewData = "2";
                int smallestDay = 999;
                for (DataSnapshot column : snapshot.child(driver_num).child("Request").getChildren()) {
                    int versusDay = convertDiffDay(column.child("arrival").getValue(String.class));
                    if (versusDay > 0) {
                        if (versusDay < smallestDay) {
                            smallestDay = versusDay;
                            viewData = column.getKey();
                        }
                    }
                }

                for (DataSnapshot column : snapshot.child(driver_num).child("Request").child(viewData).getChildren()) {
                    if (snapshot.child(driver_num).child("Request").child(viewData).child("state").getValue(String.class).equals("0")) {
                        String recruit = snapshot.child(driver_num).child("Request").child(viewData).child("days").getValue(String.class);
                        String recruit_place = snapshot.child(driver_num).child("Request").child(viewData).child("general_name").getValue(String.class);
                        requestList.add(new RequestItem(recruit, recruit_place));
                    }
                }
                for (DataSnapshot column1 : snapshot.child("Request").getChildren()) {
                    if (column1.child("state").getValue().toString().equals("2")) {
                        String recruit = column1.child("days").getValue(String.class);
                        String recruit_place = column1.child("general_name").getValue(String.class);
                        helpList.add(new RequestItem(recruit, recruit_place));
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
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
