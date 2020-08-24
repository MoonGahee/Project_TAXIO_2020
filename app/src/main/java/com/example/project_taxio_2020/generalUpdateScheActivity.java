package com.example.project_taxio_2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.akshaykale.swipetimeline.TimelineFragment;
import com.akshaykale.swipetimeline.TimelineGroupType;
import com.akshaykale.swipetimeline.TimelineObject;
import com.akshaykale.swipetimeline.TimelineObjectClickListener;

import java.util.ArrayList;

public class generalUpdateScheActivity extends AppCompatActivity implements TimelineObjectClickListener {

    Button finish_btn;
    Toolbar toolbar;
    TextView title_text;
    TimelineFragment mFragment = new TimelineFragment();
    String jeju[] = {"용두암", "용머리해안", "성산일출봉", "한라산"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_update_sche_activity);
        setToolbar();

        AutoCompleteTextView search = findViewById(R.id.search2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,jeju);
        search.setAdapter(adapter);

        mFragment.setData(loadData(), TimelineGroupType.DAY);
        mFragment.addOnClickListener(this);

        mFragment.setTimelineHeaderSize(0);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.trip2, mFragment);
        transaction.replace(R.id.trip2, mFragment);
        transaction.commit();

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

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(generalUpdateScheActivity.this);
                builder.setTitle("일정 확인");
                builder.setMessage("이대로 일정을 마무리하겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), generalMakeScheActivity.class);
                        startActivity(intent);
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

    public boolean onOptionsItemSelected(MenuItem item) {//toolbar의 back키 눌렀을 시
        switch (item.getItemId()){
            case android.R.id.home:{//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
