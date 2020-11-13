package com.example.project_taxio_2020;

import android.Manifest;
import android.content.Intent;
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
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class generalMakeScheActivity extends AppCompatActivity implements OnMapReadyCallback {
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    Button edit_btn, trip_fin;
    Button previous, next;
    Toolbar toolbar;
    TextView title_text, day1, date1, people1;
    String jeju, date;
    int k = 1;
    int day = 1;
    int tripdays = 3, tripday, tripmonth;
    float width = 200f, height = 200f;
    float zoom = 15;
    GroundOverlay imageOverlay;
    ListView listView;
    ScrollView scroll1;
    generalTimelineAdapter generalTimelineAdapter;
    ArrayList<generalTimelineItem> list_itemArrayList;
    LatLng latLng1, latLng2, lat;
    String places[], distance[] = new String[100];
    String dis;
    MapFragment mapFrag;
    GoogleMap gMap;
    GroundOverlayOptions videoMark;
    String TAG="what?";
    int size;
    int[] count;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_make_sche_activity);
        setToolbar();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        naviItem();

        Intent intent = getIntent();
        date = intent.getStringExtra("tripDate");
        tripdays = intent.getIntExtra("tripDays", 0);
        tripmonth = intent.getIntExtra("tripMonth", 0);
        tripday = intent.getIntExtra("tripDay", 0);

        Toast.makeText(getApplicationContext(), Integer.toString(tripdays), Toast.LENGTH_SHORT).show();

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

                    if (k == 1) {
                        latLng1 = lat;
                    }
                    else {
                        latLng2 = lat;
                        distance[k-2] = calDistance(latLng1, latLng2);
                        latLng1 = latLng2;
                    }

                    count = new int[k];
                    places = new String[k];

                    videoMark = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).position(lat, width, height);
                    imageOverlay = gMap.addGroundOverlay(videoMark);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, zoom));

                    list_itemArrayList.add(new generalTimelineItem(jeju, Integer.toString(k), "", 0, 0));

                    generalTimelineAdapter = new generalTimelineAdapter(generalMakeScheActivity.this, list_itemArrayList);
                    listView.setAdapter(generalTimelineAdapter);

                    size = generalTimelineAdapter.getCount();

                    k++;
                }
                else {
                    Toast.makeText(getApplicationContext(), "제주도만 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MODE_PRIVATE);

        mapFrag = (MapFragment)getFragmentManager().findFragmentById(R.id.map1);
        mapFrag.getMapAsync(this);

        scroll1 = findViewById(R.id.scroll1);

        title_text = findViewById(R.id.title_text);
        title_text.setClickable(true);

        day1 = findViewById(R.id.day1);

        day1.setText(Integer.toString(day) + "일차");

        date1 = findViewById(R.id.date1);
        date1.setText(date);

        people1 = findViewById(R.id.people1);

        previous = findViewById(R.id.previous1);
        next = findViewById(R.id.next1);

        previous.setVisibility(View.INVISIBLE);

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

                day1.setText(Integer.toString(day) + "일차");

                k = 1;

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

                day1.setText(Integer.toString(day) + "일차");

                k = 1;

                list_itemArrayList.clear();
            }
        });

        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        edit_btn = findViewById(R.id.trip_edit1);
        trip_fin = findViewById(R.id.trip_fin);

        listView = findViewById(R.id.trip1);
        list_itemArrayList = new ArrayList<generalTimelineItem>();

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), generalUpdateScheActivity.class);
                i.putExtra("tripDate", date);
                i.putExtra("tripDays", tripdays);
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
                int i = 0;

                for (int n = 0; n < size; n++){
                    places[n] = list_itemArrayList.get(n).getPlace();
                }

                list_itemArrayList.clear();

                while (true) {
                    if (position == i) {
                        if ((size-1) == i){
                            if (count[i] != 1){
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], 0, R.drawable.ic_local_taxi_black_24dp));
                                count[i] = 1;
                            }
                            else {
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], 0, 0));
                                count[i] = 0;
                            }
                            break;
                        }
                        else {
                            if (count[i] != 1) {
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], R.drawable.ic_arrow_downward_black_24dp, R.drawable.ic_local_taxi_black_24dp));
                                count[i] = 1;
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], R.drawable.ic_arrow_downward_black_24dp, 0));
                                count[i] = 0;
                            }
                        }
                    }
                    else {
                        if ((size-1) == i){
                            if (count[i] == 1){
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], 0, R.drawable.ic_local_taxi_black_24dp));
                            }
                            else {
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], 0, 0));
                            }
                            break;
                        }else {
                            if (count[i] == 1) {
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], R.drawable.ic_arrow_downward_black_24dp, R.drawable.ic_local_taxi_black_24dp));
                            }
                            else {
                                list_itemArrayList.add(new generalTimelineItem(places[i], Integer.toString(i + 1), distance[i], R.drawable.ic_arrow_downward_black_24dp, 0));
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