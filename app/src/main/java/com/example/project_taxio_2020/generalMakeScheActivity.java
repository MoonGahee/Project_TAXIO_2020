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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLngBounds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class generalMakeScheActivity extends AppCompatActivity implements OnMapReadyCallback {
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    Button edit_btn, trip_fin;
    Button previous, next;
    Toolbar toolbar;
    TextView title_text, day1, date1;
    String jeju, date;
    int k = 1;
    int day = 1;
    int tripdays = 3;
    float width = 1000f, height = 1000f;
    float zoom = 12;
    GroundOverlay imageOverlay; //이미지 마커 수정용
    ListView listView; //일정 띄우는 뷰
    ScrollView scroll1;
    generalTimelineAdapter generalTimelineAdapter; //listview와 arraylist를 연결해주는 adapter
    ArrayList<generalTimelineItem> list_itemArrayList; //일정을 저장하는 arraylist
    LatLng latLng1, latLng2; //거리 계산을 위한 변수
    LatLng lat; //검색된 장소의 위도, 경도를 저장하는 임시변수
    LatLng latLngs[][]; //검색된 장소의 위도, 경도를 저장하는 변수(사용 x)
    ArrayList<LatLng> latLng = new ArrayList<>(); //위도, 경도를 저장하는 최종 변수
    ArrayList<String> places = new ArrayList<>(); //장소들의 이름을 저장하는 arraylist
    String distance[] = new String[100]; //계산된 거릿 값을 저장하는 변수
    String place_name[][]; //장소들의 이름을 저장하는 배열
    MapFragment mapFrag; //구글 맵 프레그먼트
    GoogleMap gMap;
    GroundOverlayOptions videoMark; //이미지 마커 띄우는 용
    String TAG = "what?";
    int size;
    int p[];
    int[][] count; //택시 선택 유무 저장하는 변수
    String general_num, schedule_num;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_make_sche_activity);
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView) findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();
        setHomeBtn();

        mDatabase = FirebaseDatabase.getInstance().getReference("General");
        //값 받아오기
        Intent intent = getIntent();
        general_num = (String) intent.getSerializableExtra("general_num");
        schedule_num = (String) intent.getSerializableExtra("schedule_num");
        tripdays = intent.getIntExtra("tripDays", 3); //며칠
        date = intent.getStringExtra("tripDate");
        if (date == null)
            date = intent.getStringExtra("startDay") + " ~ " + intent.getStringExtra("endDay"); //언제부터 언제까지
        //latLng = intent.getParcelableArrayListExtra("tripLatLng");
        //places = intent.getStringArrayListExtra("trip");

        count = new int[tripdays][];
        place_name = new String[tripdays][];
        p = new int[tripdays];
        latLngs = new LatLng[tripdays][];

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoSearch);
        Places.initialize(getApplicationContext(), "AIzaSyBqG5eLPu4MXzGZN4BueA0AEDwriSqCtGU");


        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLngBounds cityBounds = new LatLngBounds(new LatLng(33.0000000, 125.0000000), new LatLng(35.0000000, 127.0000000));
                lat = place.getLatLng();

                if (cityBounds.equals(cityBounds.including(lat))) {
                    jeju = place.getName();
                    listView.setVisibility(View.VISIBLE);

                    if (k == 1) {
                        latLng1 = lat;
                        latLng.add(lat);
                    } else {
                        latLng2 = lat;
                        latLng.add(lat);
                        distance[k - 2] = calDistance(latLng1, latLng2);
                        gMap.addPolyline(new PolylineOptions().add(latLng1, latLng2).width(5).color(Color.RED));
                        latLng1 = latLng2;
                    }

                    places.add(jeju);

                    videoMark = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).position(lat, width, height);
                    imageOverlay = gMap.addGroundOverlay(videoMark);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, zoom));

                    list_itemArrayList.add(new generalTimelineItem(jeju, Integer.toString(k), "", 0, 0));

                    generalTimelineAdapter = new generalTimelineAdapter(generalMakeScheActivity.this, list_itemArrayList);
                    listView.setAdapter(generalTimelineAdapter);

                    size = generalTimelineAdapter.getCount(); //ListView count

                    count[day - 1] = new int[size];

                    p[day - 1] = 1;

                    k++;
                } else {
                    Toast.makeText(getApplicationContext(), "제주도만 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MODE_PRIVATE);

        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFrag.getMapAsync(this);

        scroll1 = findViewById(R.id.scroll1);

        trip_fin = findViewById(R.id.trip_fin);

        day1 = findViewById(R.id.day1);

        day1.setText(Integer.toString(day) + "일차");

        date1 = findViewById(R.id.date1);
        date1.setText(date);

        previous = findViewById(R.id.previous1);
        next = findViewById(R.id.next1);

        previous.setVisibility(View.INVISIBLE);
        trip_fin.setVisibility(View.INVISIBLE);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;

                if (list_itemArrayList.size() != 0) {
                    Log.d("tttest", "idx" + day);
                    place_name[day - 1] = new String[list_itemArrayList.size()];

                    for (int n = 0; n < list_itemArrayList.size(); n++) {
                        place_name[day - 1][n] = list_itemArrayList.get(n).getPlace();
                    }

                    addDateActivity();

                    day--;

                    if (day == 1) {
                        previous.setVisibility(View.INVISIBLE);
                    }

                    if (day < tripdays) {
                        next.setVisibility(View.VISIBLE);
                        trip_fin.setVisibility(View.INVISIBLE);
                    }

                    day1.setText(Integer.toString(day) + "일차");


                    k = 1;

                    if (list_itemArrayList.size() > 0) {
                        list_itemArrayList.clear();
                        places.clear();
                    }

                    while (true) {
                        if (p[day - 1] != 0) {
                            if ((place_name[day - 1].length - 1) == i) {
                                list_itemArrayList.add(new generalTimelineItem(place_name[day - 1][i], Integer.toString(i + 1), "", 0, 0));
                                break;
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(place_name[day - 1][i], Integer.toString(i + 1), "", R.drawable.arrow, 0));
                            }


                            i++;
                        } else break;
                    }

                    if (list_itemArrayList.size() != 0) {
                        generalTimelineAdapter = new generalTimelineAdapter(generalMakeScheActivity.this, list_itemArrayList);
                        listView.setAdapter(generalTimelineAdapter);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "장소를 한 곳 이상 선택해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;

                if (list_itemArrayList.size() != 0) {
                    Log.d("tttest", "idx" + day);
                    place_name[day - 1] = new String[list_itemArrayList.size()];

                    for (int n = 0; n < list_itemArrayList.size(); n++) {
                        place_name[day - 1][n] = list_itemArrayList.get(n).getPlace();
                    }

                    addDateActivity();

                    day++;

                    if (day > 1) {
                        previous.setVisibility(View.VISIBLE);
                    }

                    if (day == tripdays) {
                        next.setVisibility(View.INVISIBLE);
                        trip_fin.setVisibility(View.VISIBLE);
                    }

                    day1.setText(Integer.toString(day) + "일차");

                    k = 1;

                    if (list_itemArrayList.size() > 0) {
                        list_itemArrayList.clear();
                        places.clear();
                    }

                    while (true) {
                        if (p[day - 1] != 0) {
                            if ((place_name[day - 1].length - 1) == i) {
                                list_itemArrayList.add(new generalTimelineItem(place_name[day - 1][i], Integer.toString(i + 1), "", 0, 0));
                                break;
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(place_name[day - 1][i], Integer.toString(i + 1), "", R.drawable.arrow, 0));
                            }

                            i++;
                        } else break;
                    }

                    if (list_itemArrayList.size() != 0) {
                        generalTimelineAdapter = new generalTimelineAdapter(generalMakeScheActivity.this, list_itemArrayList);
                        listView.setAdapter(generalTimelineAdapter);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "장소를 한 곳 이상 선택해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        edit_btn = findViewById(R.id.trip_edit1);

        listView = findViewById(R.id.trip1);
        list_itemArrayList = new ArrayList<generalTimelineItem>();

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDateActivity();
                Intent i = new Intent(getApplicationContext(), generalUpdateScheActivity.class);
                i.putExtra("general_num", general_num);
                i.putExtra("schedule_num", schedule_num);  //회원번호
                i.putExtra("tripDate", date);
                i.putExtra("tripDays", day);
                i.putExtra("tripLatLng", latLng);
                i.putExtra("trip", places);
                startActivity(i);
            }
        });

        //완료 버튼 눌렀을 때
        trip_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDateActivity();
                moveActivity();
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll1.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                places.clear();

                for (int n = 0; n < list_itemArrayList.size(); n++) {
                    places.add(list_itemArrayList.get(n).getPlace());
                }

                int i = 0;

                list_itemArrayList.clear();

                while (true) {
                    if (position == i) {
                        if ((size - 1) == i) {
                            if (count[day - 1][i] != 1) {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], 0, R.drawable.car));
                                count[day - 1][i] = 1;
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], 0, 0));
                                count[day - 1][i] = 0;
                            }
                            break;
                        } else {
                            if (count[day - 1][i] != 1) {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], R.drawable.arrow, R.drawable.car));
                                count[day - 1][i] = 1;
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], R.drawable.arrow, 0));
                                count[day - 1][i] = 0;
                            }
                        }
                    } else {
                        if ((size - 1) == i) {
                            if (count[day - 1][i] == 1) {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], 0, R.drawable.car));
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], 0, 0));
                            }
                            break;
                        } else {
                            if (count[day - 1][i] == 1) {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], R.drawable.arrow, R.drawable.car));
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(places.get(i), Integer.toString(i + 1), distance[i], R.drawable.arrow, 0));
                            }
                        }
                    }

                    i++;
                }

                generalTimelineAdapter = new generalTimelineAdapter(generalMakeScheActivity.this, list_itemArrayList);
                listView.setAdapter(generalTimelineAdapter);

            }
        });
    }

    //화면 이동
    public void moveActivity() {
        isCorrect = true;
        Intent intent = new Intent(getApplicationContext(), generalSDriverActivity.class);
        intent.putExtra("general_num", general_num);
        intent.putExtra("schedule_num", schedule_num);  //회원번호
        intent.putExtra("tripDate", date);
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
            mDatabase.child(general_num).child("Schedule").child(schedule_num).child("days").child(Integer.toString(day)).child("Date_Course").child(number).updateChildren(resultDay);
            mDatabase.push();
        }
    }


    public void setHomeBtn() {
        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(generalMakeScheActivity.this);
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
        });
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
        actionBar.setDisplayHomeAsUpEnabled(false); //뒤로 가기 버튼 활성화
    }

    final long FINISH_INTERVAK_TIME = 2000;
    long backPressedTime = 0;
    Toast toast;

    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        toast = Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);

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
        if (!isCorrect)
            mDatabase.child(general_num).child("Schedule").child(schedule_num).removeValue(); //moon2대신에 id를 데려오면 되지용!
        // DB에 데이터 삭제 완료
        super.onDestroy();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng location = new LatLng(33.4996213, 126.5311884);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        gMap.getUiSettings().setZoomControlsEnabled(true);

        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                scroll1.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    public String calDistance(LatLng lat1, LatLng lat2) {
        double earth_R = 6371000.0, radian, radLat1, radLat2, radDist, distances, ret;
        double latitude1 = lat1.latitude;
        double longitude1 = lat1.longitude;
        double latitude2 = lat2.latitude;
        double longitude2 = lat2.longitude;

        radian = Math.PI / 180;

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