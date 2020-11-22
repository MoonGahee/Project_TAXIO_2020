package com.example.project_taxio_2020;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class generalUpdateScheActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button finish_btn;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView title_text, day2, date2;
    String jeju[];//장소 저장하는 배열
    String distance[] = new String[100];// 계산된 거릿 값 저장하는 변수
    String date; //여행 일자
    ListView listView; //여행 일자 띄우는 뷰
    ArrayList<LatLng> latLng; //가져온 장소들의 위도, 경도들을 저장하는 변수
    ArrayList<String> place; //가져온 장소들을 저장하는 변수
    LatLng latLng1, latLng2; //거리를 계산하기 위한 변수들
    ScrollView scroll2;
    generalTimelineAdapter generalTimelineAdapter; //listview와 arraylist를 연결해주는 adapter
    ArrayList<generalTimelineItem> list_itemArrayList; //여행 일정을 저장하는 최종변수
    int firstPos, secondPos;
    int count = 1;
    int day = 1;
    int tripdays;

    MapFragment mapFrag;
    GoogleMap gMap;
    GroundOverlayOptions videoMark;

    String general_num, schedule_num;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_update_sche_activity);
        setToolbar();

        mDatabase = FirebaseDatabase.getInstance().getReference("General");

        Intent i = getIntent();
        general_num = (String) i.getSerializableExtra("general_num");
        schedule_num = (String) i.getSerializableExtra("schedule_num");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        date = i.getStringExtra("tripDate");
        day = i.getIntExtra("tripDays", 3);
        latLng = i.getParcelableArrayListExtra("tripLatLng");
        place = i.getStringArrayListExtra("trip");


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MODE_PRIVATE);

        mapFrag = (MapFragment)getFragmentManager().findFragmentById(R.id.map2);
        mapFrag.getMapAsync(this);

        scroll2 = findViewById(R.id.scroll2);

        finish_btn = findViewById(R.id.update_finish2);

        day2 = findViewById(R.id.day2);

        day2.setText(Integer.toString(day) + "일차");

        date2 = findViewById(R.id.date2);
        date2.setText(date);

        listView = findViewById(R.id.trip2);
        list_itemArrayList = new ArrayList<generalTimelineItem>();

        for (int n = 0; n < place.size(); n++) { //가져온 일정 띄우는 코드
            if (n == (place.size()-1)) {
                list_itemArrayList.add(new generalTimelineItem(place.get(n), Integer.toString(n + 1), distance[n], 0, 0));
            }
            else {
                list_itemArrayList.add(new generalTimelineItem(place.get(n), Integer.toString(n + 1), distance[n], R.drawable.arrow, 0));
            }
        }

        generalTimelineAdapter = new generalTimelineAdapter(generalUpdateScheActivity.this, list_itemArrayList);
        listView.setAdapter(generalTimelineAdapter);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //변경 완료 누르면 나오는 다이얼로그 창
                AlertDialog.Builder builder = new AlertDialog.Builder(generalUpdateScheActivity.this);
                builder.setTitle("일정 확인");
                builder.setMessage("이대로 일정을 마무리하겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDateActivity();
                        moveActivity();
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //여행 일정 순서 변경하는 코드
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
                            list_itemArrayList.add(new generalTimelineItem(jeju[i-1], Integer.toString(i), distance[i-1], R.drawable.arrow, 0));
                        else if (secondPos == i)
                            list_itemArrayList.add(new generalTimelineItem(jeju[i-1], Integer.toString(i), distance[i-1], R.drawable.arrow, 0));
                        else
                            list_itemArrayList.add(new generalTimelineItem(jeju[i-1], Integer.toString(i), distance[i-1], R.drawable.arrow, 0));

                        i++;
                    }

                    generalTimelineAdapter = new generalTimelineAdapter(generalUpdateScheActivity.this, list_itemArrayList);
                    listView.setAdapter(generalTimelineAdapter);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { //여행 일정 중 선택한 일정 삭제하는 코드
                list_itemArrayList.remove(position);
                generalTimelineAdapter.notifyDataSetChanged();

                latLng.remove(position);

                Toast.makeText(getApplicationContext(), Integer.toString(position + 1) + "번째 장소가 삭제 됩니다.", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    public void moveActivity() {
        isCorrect = true;
        Intent intent = new Intent(getApplicationContext(), generalMakeScheActivity.class);
        intent.putExtra("general_num", general_num);
        intent.putExtra("schedule_num", schedule_num);  //회원번호
        intent.putExtra("tripDate", date);
        intent.putExtra("tripDays", day);
        startActivity(intent);
        finish();
    }

    public void addDateActivity() {

        for (int n = 0; n < list_itemArrayList.size(); n++) {
            String placeName = list_itemArrayList.get(n).getPlace();
            int taxiRide = list_itemArrayList.get(n).getTaxi();
            String number = list_itemArrayList.get(n).getNumber();
            Log.d("Moon", placeName);
            Log.d("Moon", String.valueOf(taxiRide));
            HashMap resultDay = new HashMap<>();
            HashMap result = new HashMap<>();
            result.put("course_order", number);
            resultDay.put("coures_place", placeName);
            resultDay.put("boarding_status", taxiRide);
            mDatabase.child(general_num).child("Schedule").child(schedule_num).child("days").child(Integer.toString(day)).child("Date_Course").child(Integer.toString(n+1)).updateChildren(resultDay);
            mDatabase.push();
        }


        /*int cnt = 1;
        for(int countDate = 0; countDate < tripdays; countDate++){
            HashMap result = new HashMap<>();
            result.put("schedule_num", Integer.toString(countDate + 1));
            mDatabase.push();
            Log.d("Moon-Test", "며칠" + (countDate + 1));
            if(cnt < size){
                HashMap resultDay = new HashMap<>();
                //resultDay.put("coures_place", list_itemArrayList);

                String a = list_itemArrayList.get(0).getPlace();
                Log.d("MoonTest", a);
                resultDay.put("boarding_status", count);

            }
        }*/
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
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng location = new LatLng(33.4996213, 126.5311884);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
        gMap.getUiSettings().setZoomControlsEnabled(true);

        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                scroll2.requestDisallowInterceptTouchEvent(true);
            }
        });

        for (int n = 0; n < latLng.size(); n++) {//가져온 위도, 경도들에 마커 찍고 연결선 나오게 하는 코드
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

            videoMark = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).position(lat, 1000f, 1000f);
            gMap.addGroundOverlay(videoMark);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 12));
        }
    }

    public String calDistance(LatLng lat1, LatLng lat2) { //위도랑 경도를 이용해서 거릿값 계산하는 메소드
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
    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar); // 툴바를 액티비티의 앱바로 지정 왜 에러?
        ImageButton menu = findViewById(R.id.menu);
        TextView title_text = findViewById(R.id.title_text);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        setSupportActionBar(toolbar); //툴바를 현재 액션바로 설정
        ActionBar actionBar = getSupportActionBar(); //현재 액션바를 가져옴
        actionBar.setDisplayShowTitleEnabled(false); //액션바의 타이틀 삭제 ~~~~~~~ 왜 에러냐는거냥!!
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로 가기 버튼 활성화

        title_text.setClickable(true); //홈으로 가기 버튼 활성화
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class); //삭제 후 홈으로 돌아가기
                i.putExtra("general_num", general_num);
                startActivity(i);
                finish();
            }
        });
    }
    final long FINISH_INTERVAK_TIME = 2000;
    long backPressedTime = 0;
    Toast toast;
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        toast  = Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);

        if (0 <= intervalTime && FINISH_INTERVAK_TIME >= intervalTime) {
            toast.cancel();
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            toast.show();
        }
    }
    boolean isCorrect = false;
    @Override
    protected void onDestroy() {
        if(!isCorrect)  mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
        // DB에 데이터 삭제 완료
        super.onDestroy();
    }
}
