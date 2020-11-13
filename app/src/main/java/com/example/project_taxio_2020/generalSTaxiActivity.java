
package com.example.project_taxio_2020;

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

import java.util.ArrayList;

public class generalSTaxiActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nDrawer;

    String  tripDate, rentTime , startTime;
    Integer tripMonth, tripDay, tripDays;
    String date;
    Button ok;
    ListView ListView_taxi;
    RadioGroup use;
    RadioButton yes, no;
    TextView taxiTime_tv, startTime_tv, title_text;;
    Spinner rent_spin;
    TimePicker start_pick;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {//관광택시 이용시간에 따라 시작가능 시간 설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_taxi_activity);
        setToolbar();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        Intent intent = getIntent();
        date = intent.getStringExtra("tripDate");
        tripDays = intent.getIntExtra("tripDays", 0);
        tripMonth = intent.getIntExtra("tripMonth", 0);
        tripDay = intent.getIntExtra("tripDay", 0);

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
                Intent i = new Intent(getApplicationContext(), generalMakeScheActivity.class);
                i.putExtra("tripDate", date);
                i.putExtra("tripDays", tripDays);
                i.putExtra("tripMonth", tripMonth);
                i.putExtra("tripDay", tripDay);
                startActivity(i);
                finish();
            }
        });
    }

    public void setList(generalTaxiAdapter adapter){
        for(int i = 0; i< tripDays; i++){
            tripDate = tripMonth+"월 "+(tripDay+i)+"일";
            adapter.addItem();
        }
    }
    public class generalTaxiAdapter extends BaseAdapter {

        public ArrayList<generalTaxiItem> listViewItemList = new ArrayList<generalTaxiItem>();

        public generalTaxiAdapter(){}
        @Override
        public int getCount() {
            return listViewItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return position ;
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
                params.height=110;
                convertView.setLayoutParams(params);
            }

            TextView taxi_day =  convertView.findViewById(R.id.taxi_day);
            final Button choice = convertView.findViewById(R.id.choice);
            final TextView rent_time = convertView.findViewById(R.id.rent_time);
            final TextView start_time = convertView.findViewById(R.id.start_time);
            generalTaxiItem listViewItem = listViewItemList.get(position);



            choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(generalSTaxiActivity.this);
                    View taxi_plus = View.inflate(generalSTaxiActivity.this, R.layout.general_choice_taxi,null);

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
                            switch (checkedId){
                                case R.id.yes :
                                    taxiTime_tv.setVisibility(View.VISIBLE);
                                    rent_spin.setVisibility(View.VISIBLE);
                                    break;
                                case R.id.no:
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
                            rentTime= String.valueOf(parent.getItemAtPosition(position));

                            if(!rentTime.equals("0")){
                                startTime_tv.setVisibility(View.VISIBLE);
                                start_pick.setVisibility(View.VISIBLE);
                            }
                            else{
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
                            startTime= hourOfDay+"시"+minute+"분";
                        }
                    });


                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yes.isChecked()) {
                                choice.setVisibility(View.GONE);
                                rent_time.setVisibility(View.VISIBLE);
                                rent_time.setText(rentTime);
                                if (rentTime.equals("0")) {
                                    startTime = "-";

                                }
                                start_time.setVisibility(View.VISIBLE);
                                start_time.setText(startTime);
                            }
                            else{ //관광택시 이용 안할시 어케 할꺼?

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

        public void addItem() {
            generalTaxiItem item = new generalTaxiItem();

            item.setTripDate(tripDate);

            listViewItemList.add(item);
        }

        public void clearItem(){
            listViewItemList.clear();
        }
    }

    public void naviItem(){
        nDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //Navigation Drawer 사용
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if(id == R.id.drawer_schTrip){
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
                }
                return true;
            }
        });
    }



    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
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