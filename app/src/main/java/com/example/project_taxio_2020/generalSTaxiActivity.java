package com.example.project_taxio_2020;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//택시 선택 by 하은

public class generalSTaxiActivity extends AppCompatActivity {

    Button ok;
    TextView title_text;
    ListView ListView_taxi;
    Integer tripDays, tripMonth, tripDay;
    String sDate,sTime, rTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_select_taxi_activity);
        setToolbar();

        ok = findViewById(R.id.ok);

        Intent i = new Intent();
        tripDays= i.getIntExtra("days", 0);
        tripMonth=i.getIntExtra("startMonth", 0);
        tripDay=i.getIntExtra("startDay", 0);

        ListView_taxi = findViewById(R.id.ListView_taxi);

        myAdapter adapter = new myAdapter();
        ListView_taxi.setAdapter(adapter);
        setList(adapter);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMakeScheActivity.class);
                startActivity(i);
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMakeScheActivity.class);
                i.putExtra("days", tripDays);
                i.putExtra("startMonth", tripMonth);
                i.putExtra("startDay", tripDay);
                startActivity(i);
                finish();
            }
        });
    }

    private void setList(myAdapter adapter) {
        for(int i =0; i<tripDays;i++){
            adapter.addItem(tripMonth+"월"+(tripDay+i)+"일");
        }
    }

    public class myAdapter extends BaseAdapter {

        public ArrayList<generalTaxiItem> list = new ArrayList<generalTaxiItem>();

        //초기화
        public myAdapter() {}

        //리스트 뷰 아이템 개수 알려줌
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //현재 아이템 알려줌
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        //리스트뷰에서 아이템과 xml을 연결하여 화면에 출력
        @NonNull
        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

            final Context c = parent.getContext();
            if (view == null) {//xml로 된 layout파일을 view 객체로 변환
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.general_taxi_item, parent, false);
            }

            TextView  taxi_day = findViewById(R.id.taxi_day);
            Spinner rent_spin = findViewById(R.id.rent_spin);
            final Button start_btn = findViewById(R.id.start_btn);

            final generalTaxiItem myList = list.get(position);
            taxi_day.setText(myList.getTripDate());
            rent_spin.setSelection(Integer.parseInt(myList.getRentTime()));
            rent_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String rent_time = String.valueOf(parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            start_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog d = new TimePickerDialog(c, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            ;
                        }
                    }, 0, 0, true);

                    d.setMessage("출발 시간");
                    d.show();
                }
            });

            return view;
        }

        //HosListItem 클래스에 정의한 set 메소드에 데이터를 담아서 리턴함
        public void addItem(String date) {
           generalTaxiItem item = new generalTaxiItem();

            item.setTripDate(date);

            list.add(item);
        }
    }


    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화

    }
}
