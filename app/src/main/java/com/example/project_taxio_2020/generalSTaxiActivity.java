
package com.example.project_taxio_2020;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class generalSTaxiActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;

    String tripDate[], rentTime, startTime, startDay, endDay, tripDay;
    Integer tripDays;
    String date;
    Button ok;
    ListView ListView_taxi;
    RadioGroup use;
    RadioButton yes, no;
    TextView taxiTime_tv, startTime_tv, title_text;
    ;
    Spinner rent_spin;
    TimePicker start_pick;
    String general_num;
    DatabaseReference mDatabase;
    HashMap result;
    int count = 0;
    int cnt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {//관광택시 이용시간에 따라 시작가능 시간 설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_taxi_activity);
        setToolbar();

        tripDate = new String[4];

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        //값 받아오기
        Intent i = getIntent();
        general_num = (String) i.getSerializableExtra("general_num");

        Intent intent = getIntent();
        date = intent.getStringExtra("tripDate");
        tripDays = intent.getIntExtra("tripDays", 0);
        startDay = intent.getStringExtra("startingDay");
        endDay = intent.getStringExtra("endDay");

        int j = startDay.indexOf("월");
        tripDate[0] = startDay.substring(6, j);
        tripDate[1] = startDay.substring(j + 2, startDay.length() - 1);
        j = endDay.indexOf("월");
        tripDate[2] = endDay.substring(6, j);
        tripDate[3] = endDay.substring(j + 2, endDay.length() - 1);

        ok = findViewById(R.id.ok);
        ListView_taxi = findViewById(R.id.ListView_taxi);
        generalTaxiAdapter adapter = new generalTaxiAdapter();
        ListView_taxi.setAdapter(adapter);
        setList(adapter);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity();
            }
        });
    }

    public void setList(generalTaxiAdapter adapter) {
        if (!(tripDate[0].equals(tripDate[2]))) {
            int j = 1;
            if (tripDate[0] == "1" || tripDate[0] == "3" || tripDate[0] == "5" || tripDate[0] == "7" || tripDate[0] == "8" || tripDate[0] == "10" || tripDate[0] == "12") {
                for (int i = 0; i < tripDays; i++) {
                    if ((Integer.parseInt(tripDate[1]) + i) >= 32 && Integer.parseInt(tripDate[3]) >= j) {
                        tripDay = tripDate[2] + "월 " + j++ + "일";
                    } else {
                        tripDay = tripDate[0] + "월 " + (Integer.parseInt(tripDate[1]) + i) + "일";
                    }
                    adapter.addItem(tripDay); //하루씩
                }
            } else if (tripDate[0] == "4" || tripDate[0] == "6" || tripDate[0] == "9" || tripDate[0].equals("11")) {
                for (int i = 0; i < tripDays; i++) { //날짜 개수
                    if ((Integer.parseInt(tripDate[1]) + i) >= 31 && Integer.parseInt(tripDate[3]) >= j) {
                        tripDay = tripDate[2] + "월 " + j++ + "일";
                    } else {
                        tripDay = tripDate[0] + "월 " + (Integer.parseInt(tripDate[1]) + i) + "일";
                    }
                    adapter.addItem(tripDay);
                }
            } else {
                for (int i = 0; i < tripDays; i++) {
                    tripDay = tripDate[0] + "월 " + (Integer.parseInt(tripDate[1]) + i) + "일";
                    if ((tripDate[1] + i) == "29" && Integer.parseInt(tripDate[3]) < j) {
                        tripDay = tripDate[2] + "월 " + j++ + "일";
                    }
                    adapter.addItem(tripDay);
                }
            }
        } else {
            for (int i = 0; i < tripDays; i++) {
                tripDay = tripDate[0] + "월 " +  (Integer.parseInt(tripDate[1]) + i) + "일";
                adapter.addItem(tripDay);
            }
        }
    }

    public class generalTaxiAdapter extends BaseAdapter {

        public ArrayList<generalTaxiItem> listViewItemList = new ArrayList<generalTaxiItem>();

        public generalTaxiAdapter() {
        }

        @Override
        public int getCount() {
            return listViewItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.general_taxi_item, parent, false);

                ViewGroup.LayoutParams params = convertView.getLayoutParams();
                params.height = 110;
                convertView.setLayoutParams(params);
            }

            TextView taxi_day = convertView.findViewById(R.id.taxi_day);
            final Button choice = convertView.findViewById(R.id.choice);
            final TextView rent_time = convertView.findViewById(R.id.rent_time);
            final TextView start_time = convertView.findViewById(R.id.start_time);
            generalTaxiItem listViewItem = listViewItemList.get(position);


            choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //초기화 하는 경우 고려해서 카운트도 초기화
                    AlertDialog.Builder dlg = new AlertDialog.Builder(generalSTaxiActivity.this);
                    View taxi_plus = View.inflate(generalSTaxiActivity.this, R.layout.general_choice_taxi, null);
                    final HashMap resultTaxi = new HashMap<>();
                    use = taxi_plus.findViewById(R.id.use);
                    yes = taxi_plus.findViewById(R.id.yes);

                    taxiTime_tv = taxi_plus.findViewById(R.id.taxiTime_tv);
                    startTime_tv = taxi_plus.findViewById(R.id.startTime_tv);
                    rent_spin = taxi_plus.findViewById(R.id.rent_spin);
                    start_pick = taxi_plus.findViewById(R.id.start_pick);

                    dlg.setTitle("택시 선택");
                    dlg.setView(taxi_plus);
                    use.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.yes: //이용
                                    resultTaxi.put("boarding_status", true); //이용여부 Boolean
                                    Log.d("Moon-Test", "0");
                                    taxiTime_tv.setVisibility(View.VISIBLE);
                                    rent_spin.setVisibility(View.VISIBLE);
                                    break;
                                case R.id.no: //아니오
                                    resultTaxi.put("boarding_status", false);
                                    Log.d("Moon-Test", "1");
                                    taxiTime_tv.setVisibility(View.GONE);
                                    startTime_tv.setVisibility(View.GONE);
                                    rent_spin.setVisibility(View.GONE);
                                    start_pick.setVisibility(View.GONE);
                                    break;

                            }
                        }
                    });

                    rent_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            rentTime = String.valueOf(parent.getItemAtPosition(position)); //대여시간
                            resultTaxi.put("taxi_time", rentTime);
                            Log.d("Moon-Test", rentTime);
                            if (!rentTime.equals("0")) {
                                startTime_tv.setVisibility(View.VISIBLE);
                                start_pick.setVisibility(View.VISIBLE);
                            } else {
                                startTime_tv.setVisibility(View.GONE);
                                start_pick.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    start_pick.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            startTime = hourOfDay + "시" + minute + "분"; //탑승 시각
                            resultTaxi.put("start_time", startTime);
                            Log.d("Moon-Test", startTime);
                        }
                    });


                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yes.isChecked()) {
                                chkCnt();
                                Log.d("Moon-Test", Integer.toString(cnt));
                                choice.setVisibility(View.GONE);
                                rent_time.setVisibility(View.VISIBLE);
                                rent_time.setText(rentTime);
                                if (rentTime.equals("0")) {
                                    startTime = "-";
                                }
                                start_time.setVisibility(View.VISIBLE);
                                start_time.setText(startTime);
                                //count를 어떻게 클릭할 때마다 줄 것인가?
                                mDatabase.child(general_num).child("Schedule").child("days").child("Date_Schedule").child(Integer.toString(cnt)).updateChildren(resultTaxi);
                                Log.d("Moon-Test", "success");
                                mDatabase.push();

                            } else { //관광택시 이용 안할시 어케 할꺼?

                            }
                        }
                    });
                    dlg.setNegativeButton("취소", null);
                    dlg.show();
                }
            });
            taxi_day.setText(listViewItem.getTripDate());

            return convertView;
        }

        //초기화 경우 고려하여 함수
        public void chkCnt() {
            if (cnt < count) {
                cnt++;
            } else {
                cnt = count - cnt;
            }
        }

        // ListView 하나씩 추가
        public void addItem(String tripDay) {
            generalTaxiItem item = new generalTaxiItem();
            item.setTripDate(tripDay);
            listViewItemList.add(item);
            makeSchOne();
        }

        public void clearItem() {
            listViewItemList.clear();
        }
    }


    //날짜별 DB 저장
    public void makeSchOne() {
        if (count != tripDays) {
            Log.d("Moon-Test", "start");
            HashMap result = new HashMap<>();
            result.put("schedule_num", Integer.toString(count + 1));
            mDatabase.push();
            Log.d("Moon-Test", "며칠" + (count + 1));
            HashMap resultDay = new HashMap<>();
            resultDay.put("schedule_date", tripDay);
            mDatabase.child(general_num).child("Schedule").child("days").child("Date_Schedule").child(Integer.toString(count + 1)).updateChildren(resultDay);
            Log.d("Moon-Test", "언제" + tripDay);
            Log.d("Moon-Test", "wow");
            mDatabase.push();
            count++;
        } else {
            Log.d("Moon-test", "faild");
        }
    }

    //화면 이동
    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), generalMakeScheActivity.class);
        intent.putExtra("general_num", general_num);
        intent.putExtra("tripDays", tripDays);
        intent.putExtra("startDay", startDay);
        intent.putExtra("endDay", endDay);
        startActivity(intent);
        finish();
    }

    //네비게이션
    public void naviItem() {
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.drawer_schTrip) {
                    Intent intent = new Intent(getApplicationContext(), generalSDriverActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_myInfo) {
                    Intent intent = new Intent(getApplicationContext(), generalCheckEpilogueActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_modify) {
                    Intent intent = new Intent(getApplicationContext(), generalModifyId.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.drawer_out) {
                    Intent intent = new Intent(getApplicationContext(), generalWriteWithdrawalActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
}