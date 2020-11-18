package com.example.project_taxio_2020;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class generalUpdateScheActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button finish_btn, previous, next;
    Toolbar toolbar;
    TextView title_text, day2, date2;
    String jeju[], distance[] = new String[100];
    String date;
    ListView listView;
    ArrayList<LatLng> latLng;
    ArrayList<String> place;
    LatLng latLng1, latLng2;
    ScrollView scroll2;
    generalTimelineAdapter generalTimelineAdapter;
    ArrayList<generalTimelineItem> list_itemArrayList;
    int firstPos, secondPos;
    int count = 1;
    int day = 1;
    int tripdays;

    MapFragment mapFrag;
    GoogleMap gMap;
    GroundOverlayOptions videoMark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_update_sche_activity);
        setToolbar();

        Intent i = getIntent();
        date = i.getStringExtra("tripDate");
        tripdays = i.getIntExtra("tripDays", 0);
        latLng = i.getParcelableArrayListExtra("tripLatLng");
        place = i.getStringArrayListExtra("trip");


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
        date2.setText(date);

        listView = findViewById(R.id.trip2);
        list_itemArrayList = new ArrayList<generalTimelineItem>();

        for (int n = 0; n < place.size(); n++) {
            if (n != place.size()) {
                list_itemArrayList.add(new generalTimelineItem(place.get(n), Integer.toString(n + 1), distance[n], 0, 0));
            }
            else {
                list_itemArrayList.add(new generalTimelineItem(place.get(n), Integer.toString(n + 1), distance[n], R.drawable.ic_arrow_downward_black_24dp, 0));
            }
        }

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

                list_itemArrayList.clear();
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

                list_itemArrayList.clear();
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
                jeju = new String[size];

                for (int k = 0; k < size; k++) {
                    jeju[k] = list_itemArrayList.get(k).getPlace();
                }

                if (count % 2 == 1) {
                    firstPos = position+1;
                    count++;

                }
                else {
                    secondPos = position+1;

                    String n = jeju[firstPos-1];
                    jeju[firstPos-1] = jeju[secondPos-1];
                    jeju[secondPos-1] = n;

                    count++;

                    list_itemArrayList.clear();

                    while (true) {
                        if (size == i) {
                            list_itemArrayList.add(new generalTimelineItem(jeju[i-1], Integer.toString(i), distance[i-1], 0, 0));
                            break;
                        }

                        if (firstPos == i)
                            list_itemArrayList.add(new generalTimelineItem(jeju[i-1], Integer.toString(i), distance[i-1], R.drawable.ic_arrow_downward_black_24dp, 0));
                        else if (secondPos == i)
                            list_itemArrayList.add(new generalTimelineItem(jeju[i-1], Integer.toString(i), distance[i-1], R.drawable.ic_arrow_downward_black_24dp, 0));
                        else
                            list_itemArrayList.add(new generalTimelineItem(jeju[i-1], Integer.toString(i), distance[i-1], R.drawable.ic_arrow_downward_black_24dp, 0));

                        i++;
                    }

                    generalTimelineAdapter = new generalTimelineAdapter(generalUpdateScheActivity.this, list_itemArrayList);
                    listView.setAdapter(generalTimelineAdapter);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                list_itemArrayList.remove(position);
                generalTimelineAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), Integer.toString(position + 1) + "번째 장소가 삭제 됩니다.", Toast.LENGTH_SHORT).show();

                return true;
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
        LatLng location = new LatLng(33.4996213, 126.5311884);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        gMap.getUiSettings().setZoomControlsEnabled(true);

        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                scroll2.requestDisallowInterceptTouchEvent(true);
            }
        });

        for (int n = 0; n < latLng.size(); n++) {
            LatLng lat = new LatLng(latLng.get(n).latitude, latLng.get(n).longitude);

            if (n == 0) {
                latLng1 = lat;
            }
            else {
                latLng2 = lat;
                distance[n-1] = calDistance(latLng1, latLng2);
                gMap.addPolyline(new PolylineOptions().add(latLng1, latLng2).width(5).color(Color.RED));
                latLng1 = latLng2;
            }

            videoMark = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).position(lat, 200f, 200f);
            gMap.addGroundOverlay(videoMark);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
        }
    }

    public String calDistance(LatLng lat1, LatLng lat2) {
        double earth_R = 6371000.0, radian, radLat1, radLat2, radDist, distances, ret;
        double latitude1 = lat1.latitude;
        double longitude1 = lat1.longitude;
        double latitude2 = lat2.latitude;
        double longitude2 = lat2.longitude;

        radian = Math.PI/180;

        radLat1 = radian * latitude1;
        radLat2 = radian * latitude2;
        radDist = radian * (longitude1 - longitude2);

        distances = Math.sin(radLat1) * Math.sin(radLat2);
        distances += Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);

        ret = earth_R * Math.acos(distances);

        double rtn = Math.round(Math.round(ret) / 1000);

        if (rtn <= 0) return Double.toString(rtn) + " m";
        else return Double.toString(rtn) + "km";
    }
}
