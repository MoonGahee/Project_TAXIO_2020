package com.example.project_taxio_2020;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.akshaykale.swipetimeline.TimelineFragment;
import com.akshaykale.swipetimeline.TimelineGroupType;
import com.akshaykale.swipetimeline.TimelineObject;
import com.akshaykale.swipetimeline.TimelineObjectClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class generalUpdateScheActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button finish_btn, previous, next;
    Toolbar toolbar;
    TextView title_text, day2, date2, people2;
    String jeju[] = {"용두암", "용머리해안", "성산일출봉", "한라산"};
    ListView listView;
    ScrollView scroll2;
    generalTimelineAdapter generalTimelineAdapter;
    ArrayList<generalTimelineItem> list_itemArrayList;
    int firstPos, secondPos;
    int count = 1;
    int day = 1;
    int tripdays = 3;

    MapFragment mapFrag;
    GoogleMap gMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_update_sche_activity);
        setToolbar();

        AutoCompleteTextView search = findViewById(R.id.search2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,jeju);
        search.setAdapter(adapter);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MODE_PRIVATE);

        mapFrag = (MapFragment)getFragmentManager().findFragmentById(R.id.map2);
        mapFrag.getMapAsync(this);

        scroll2 = findViewById(R.id.scroll2);

        previous = findViewById(R.id.previous2);
        next = findViewById(R.id.next2);

        previous.setVisibility(View.INVISIBLE);

        finish_btn = findViewById(R.id.update_finish2);

        day2 = findViewById(R.id.day2);

        day2.setText(Integer.toString(day) + "일차");

        date2 = findViewById(R.id.date2);

        people2 = findViewById(R.id.people2);

        listView = findViewById(R.id.trip2);
        list_itemArrayList = new ArrayList<generalTimelineItem>();

        list_itemArrayList.add(new generalTimelineItem("제주공항", "1", "1시간", 0, 0));
        list_itemArrayList.add(new generalTimelineItem("성산일출봉", "2", "1시간", 0, 0));
        list_itemArrayList.add(new generalTimelineItem("오셜록", "3", "1시간", 0, 0));
        list_itemArrayList.add(new generalTimelineItem("주상절리", "4", "1시간", 0, 0));
        list_itemArrayList.add(new generalTimelineItem("용두암", "5", "1시간", 0, 0));

        generalTimelineAdapter = new generalTimelineAdapter(generalUpdateScheActivity.this, list_itemArrayList);
        listView.setAdapter(generalTimelineAdapter);

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

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day--;

                if (day == 1) {
                    previous.setVisibility(View.INVISIBLE);
                }

                if (day < tripdays) {
                    next.setVisibility(View.VISIBLE);
                }

                day2.setText(Integer.toString(day) + "일차");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day++;

                if(day > 1) {
                    previous.setVisibility(View.VISIBLE);
                }

                if (day == tripdays) {
                    next.setVisibility(View.INVISIBLE);
                }

                day2.setText(Integer.toString(day) + "일차");
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

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll2.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = 1;
                int size = generalTimelineAdapter.getCount();

                if (count % 2 == 1) {
                    firstPos = position + 1;
                    count++;

                }
                else {
                    secondPos = position + 1;
                    count++;

                    list_itemArrayList.clear();

                    while (true) {
                        if (size == i) {
                            list_itemArrayList.add(new generalTimelineItem("용두암", Integer.toString(i), "1시간", 0, 0));
                            break;
                        }

                        if (firstPos == i)
                            list_itemArrayList.add(new generalTimelineItem("제주공항", Integer.toString(secondPos), "1시간 30분", R.drawable.ic_arrow_downward_black_24dp, 0));
                        else if (secondPos == i)
                            list_itemArrayList.add(new generalTimelineItem("제주공항", Integer.toString(firstPos), "1시간 30분", R.drawable.ic_arrow_downward_black_24dp, 0));
                        else
                            list_itemArrayList.add(new generalTimelineItem("제주공항", Integer.toString(i), "1시간 30분", R.drawable.ic_arrow_downward_black_24dp, 0));

                        i++;
                    }

                    generalTimelineAdapter = new generalTimelineAdapter(generalUpdateScheActivity.this, list_itemArrayList);
                    listView.setAdapter(generalTimelineAdapter);
                }
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

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //홈으로 가기 버튼 활성화
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng location = new LatLng(37.568256, 126.897240);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        gMap.getUiSettings().setZoomControlsEnabled(true);

        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                scroll2.requestDisallowInterceptTouchEvent(true);
            }
        });
    }
}
