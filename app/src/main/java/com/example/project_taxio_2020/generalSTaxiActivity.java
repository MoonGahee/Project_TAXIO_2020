
package com.example.project_taxio_2020;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import android.widget.LinearLayout;
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
    LinearLayout choose;
    Spinner rent_spin;
    TimePicker start_pick;
    String general_num, schedule_num;
    DatabaseReference mDatabase;
    HashMap result;
    int count = 0;
    int cnt = 0;
    HashMap resultTaxi;

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
        setHomeBtn();

        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        //값 받아오기
        Intent i = getIntent();
        general_num = (String) i.getSerializableExtra("general_num");
        schedule_num = (String) i.getSerializableExtra("schedule_num");

        Intent intent = getIntent();
        date = intent.getStringExtra("tripDate");
        tripDays = intent.getIntExtra("tripDays", 0);
        startDay = intent.getStringExtra("startingDay");
        endDay = intent.getStringExtra("endDay");

        result = new HashMap<>();
        resultTaxi = new HashMap<>();

        int j = startDay.indexOf("월");
        tripDate[0] = startDay.substring(6, j);
        tripDate[1] = startDay.substring(j + 2, startDay.length() - 1);
        int k = endDay.indexOf("월");
        tripDate[2] = endDay.substring(6, k);
        tripDate[3] = endDay.substring(k + 2, endDay.length() - 1);

        ok = findViewById(R.id.ok);
        ListView_taxi = findViewById(R.id.ListView_taxi);
        generalTaxiAdapter adapter = new generalTaxiAdapter();
        ListView_taxi.setAdapter(adapter);
        setList(adapter);

        title_text = findViewById(R.id.title_text);
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
                tripDay = tripDate[0] + "월 " + (Integer.parseInt(tripDate[1]) + i) + "일";
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
        public View getView(final int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
            final int posi = pos;

            final Context[] context = {parent.getContext()};

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context[0].getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.general_taxi_item, parent, false);

                ViewGroup.LayoutParams params = convertView.getLayoutParams();
                params.height = 110;
                convertView.setLayoutParams(params);
            }

            TextView taxi_day = convertView.findViewById(R.id.taxi_day);
            final Button choice = convertView.findViewById(R.id.choice);
            final Spinner rent_time = convertView.findViewById(R.id.rent_time);
            final TextView start_time = convertView.findViewById(R.id.start_time);
            generalTaxiItem listViewItem = listViewItemList.get(posi);
            rent_time.getBackground().setColorFilter(getResources().getColor(R.color.button), PorterDuff.Mode.SRC_ATOP);


            choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //초기화 하는 경우 고려해서 카운트도 초기화
                    final AlertDialog.Builder[] dlg = {new AlertDialog.Builder(generalSTaxiActivity.this)};
                    View taxi_plus = View.inflate(generalSTaxiActivity.this, R.layout.general_choice_taxi, null);

                    use = taxi_plus.findViewById(R.id.use);
                    yes = taxi_plus.findViewById(R.id.yes);

                    taxiTime_tv = taxi_plus.findViewById(R.id.taxiTime_tv);
                    startTime_tv = taxi_plus.findViewById(R.id.startTime_tv);
                    rent_spin = taxi_plus.findViewById(R.id.rent_spin);
                    start_pick = taxi_plus.findViewById(R.id.start_pick);
                    choose = taxi_plus.findViewById(R.id.choose);

                    final int[] timepos = {0};

                    dlg[0].setTitle("택시 선택");
                    dlg[0].setView(taxi_plus);
                    use.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.yes: //이용
                                    resultTaxi.put("boarding_status", true); //이용여부 Boolean
                                    choose.setVisibility(View.VISIBLE);
                                    break;
                                case R.id.no: //아니오
                                    resultTaxi.put("boarding_status", false);
                                    choose.setVisibility(View.GONE);
                                    break;

                            }
                        }
                    });

                    rent_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            timepos[0] = position;
                            rentTime = String.valueOf(parent.getItemAtPosition(position)); //대여시간
                            resultTaxi.put("taxi_time", rentTime);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            rentTime = String.valueOf(parent.getItemAtPosition(0)); //대여시간

                        }
                    });

                    //시간 선택시
                    start_pick.setHour(9);
                    start_pick.setMinute(00);
                    int hour = start_pick.getHour();
                    int min = start_pick.getMinute();
                    startTime = hour + "시" + min + "분"; //탑승 시각
                    start_pick.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            startTime = hourOfDay + "시" + minute + "분";
                        }
                    });

                    dlg[0].setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yes.isChecked()) {
                                chkCnt();
                                Log.d("Moon-Test", Integer.toString(cnt));
                                choice.setVisibility(View.GONE);
                                rent_time.setVisibility(View.VISIBLE);
                                rent_time.setSelection(timepos[0]);
                                start_time.setVisibility(View.VISIBLE);
                                start_time.setText(startTime);
                                //count를 어떻게 클릭할 때마다 줄 것인가?
                                resultTaxi.put("taxi_time", rentTime);
                                resultTaxi.put("start_time", startTime);
                                mDatabase.child(general_num).child("Schedule").child(schedule_num).child("days").child(Integer.toString(cnt)).child("Date_Schedule").updateChildren(resultTaxi);
                                mDatabase.push();
                            } else {
                                chkCnt();
                                Log.d("Moon-Test", Integer.toString(cnt));
                                choice.setText("이용 안함");
                                mDatabase.child(general_num).child("Schedule").child(schedule_num).child("days").child(Integer.toString(cnt)).child("Date_Schedule").updateChildren(resultTaxi);
                                mDatabase.push();
                            }
                        }
                    });
                    dlg[0].setNegativeButton("취소", null);
                    dlg[0].show();


                }
            });
            start_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog dlg = new TimePickerDialog(generalSTaxiActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            startTime = hourOfDay + "시" + minute + "분"; //탑승 시각
                            resultTaxi.put("start_time", startTime);
                            start_time.setText(startTime);
                        }
                    }, 9, 0, false);

                    dlg.show();

                }
            });

            rent_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    rentTime = String.valueOf(parent.getItemAtPosition(position)); //대여시간
                    resultTaxi.put("taxi_time", rentTime);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ;
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
            HashMap result = new HashMap<>();
            result.put("schedule_num", Integer.toString(count + 1));
            mDatabase.push();
            HashMap resultDay = new HashMap<>();
            resultDay.put("schedule_date", tripDay);
            mDatabase.child(general_num).child("Schedule").child(schedule_num).child("days").child(Integer.toString(count + 1)).child("Date_Schedule").updateChildren(resultDay);
            mDatabase.push();
            count++;
        } else {
        }
    }

    //화면 이동
    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(), generalMakeScheActivity.class);
        intent.putExtra("general_num", general_num);
        intent.putExtra("schedule_num", schedule_num);  //회원번호
        intent.putExtra("tripDays", tripDays);
        intent.putExtra("startDay", startDay);
        intent.putExtra("endDay", endDay);
        startActivity(intent);
        finish();
    }

    //홈버튼
    public void setHomeBtn() {
        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(generalSTaxiActivity.this);
                builder.setTitle("예약 종료");
                builder.setMessage("지금 화면을 나가면 일정 정보는 삭제됩니다.\n 그래도 괜찮으신가요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // DB에 데이터 삭제 시작
                        mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
                        // DB에 데이터 삭제 완료
                        Intent i = new Intent(getApplicationContext(), generalMainActivity.class); //삭제 후 홈으로 돌아가기
                        i.putExtra("general_num", general_num);
                        startActivity(i);
                        finish();
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

    //뒤로 가기


    //네비게이션
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

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(generalSTaxiActivity.this);
        builder.setTitle("예약 종료");
        builder.setMessage("지금 화면을 나가면 일정 정보는 삭제됩니다.\n그래도 괜찮으신가요?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DB에 데이터 삭제 시작
                mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
                // DB에 데이터 삭제 완료
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class); //삭제 후 홈으로 돌아가기
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();

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