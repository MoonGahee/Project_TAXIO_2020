package com.example.project_taxio_2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.akshaykale.swipetimeline.TimelineFragment;
import com.akshaykale.swipetimeline.TimelineGroupType;
import com.akshaykale.swipetimeline.TimelineObject;
import com.akshaykale.swipetimeline.TimelineObjectClickListener;

import java.util.ArrayList;

public class generalMakeScheActivity extends AppCompatActivity implements TimelineObjectClickListener {

    Button edit_btn, taxi_btn, trip_fin;
    Toolbar toolbar;
    TextView title_text;
    TimelineFragment mFragment = new TimelineFragment();
    String jeju[] = {"용두암", "용머리해안", "성산일출봉", "한라산"};
    /*ListView listView;
    generalTimelineAdapter generalTimelineAdapter;
    ArrayList<generalTimelineItem> list_itemArrayList;*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_make_sche_activity);
        setToolbar();

        AutoCompleteTextView search = findViewById(R.id.search1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,jeju);
        search.setAdapter(adapter);

        mFragment.setData(loadData(), TimelineGroupType.DAY);
        mFragment.addOnClickListener(this);

        mFragment.setTimelineHeaderSize(0);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.trip1, mFragment);
        transaction.replace(R.id.trip1, mFragment);
        transaction.commit();

        /*
        * listView = findViewById(R.id.trip1);
        * list_itemArrayList = new ArrayList<generalTimelineItem>();
        * generalTimelineAdapter = new generalTimelineAdapter(generalMakeScheActivity.this, list_itemArrayList);
        * listView.setAdapter(myListAdapter);
        * */

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

        edit_btn = findViewById(R.id.trip_edit1);
        taxi_btn = findViewById(R.id.taxi_btn1);
        trip_fin = findViewById(R.id.trip_fin);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalUpdateScheActivity.class);
                startActivity(i);
            }
        });

        taxi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalSTaxiActivity.class);
                startActivity(i);
            }
        });

        trip_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalSDriverActivity.class);
                startActivity(i);
            }
        });
    }

    private ArrayList<TimelineObject> loadData() {
        ArrayList<TimelineObject> obj = new ArrayList<>();

        obj.add(new generalTimeline(Long.parseLong("1483196400000"), "제주공항", "url"));
        obj.add(new generalTimeline(Long.parseLong("1484146800000"), "용두암", "url"));
        obj.add(new generalTimeline(Long.parseLong("1485961200000"), "성산일출봉", "url"));
        obj.add(new generalTimeline(Long.parseLong("1487084400000"), "동문시장", "url"));
        obj.add(new generalTimeline(Long.parseLong("1489244400000"), "하얏트호텔", "url"));

        return obj;
    }

    public boolean onOptionsItemSelected(MenuItem item) {//toolbar의 back키 눌렀을 시
        switch (item.getItemId()){
            case android.R.id.home:{//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimelineObjectClicked(TimelineObject timelineObject) {

    }

    @Override
    public void onTimelineObjectLongClicked(TimelineObject timelineObject) {

    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }
}