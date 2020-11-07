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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class generalMakeScheActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button edit_btn, taxi_btn, trip_fin;
    Button previous, next;
    Toolbar toolbar;
    TextView title_text, day1;
    String jeju;
    int k = 1;
    int day = 1;
    int tripdays = 3, tripday, tripmonth;
    ListView listView;
    ScrollView scroll1;
    generalTimelineAdapter generalTimelineAdapter;
    ArrayList<generalTimelineItem> list_itemArrayList;

    String places[][];

    MapFragment mapFrag;
    GoogleMap gMap;
    GroundOverlayOptions videoMark;
    String TAG="what?";
    int size;
    int[][] count;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_make_sche_activity);
        setToolbar();

        //Intent intent = getIntent();
        //tripdays = intent.getIntExtra("tripDays", 0);
        //tripmonth = intent.getIntExtra("tripMonth", 0);
        //tripday = intent.getIntExtra("tripDay", 0);

        Toast.makeText(getApplicationContext(), Integer.toString(tripdays), Toast.LENGTH_SHORT).show();

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoSearch);
        Places.initialize(getApplicationContext(), "AIzaSyBqG5eLPu4MXzGZN4BueA0AEDwriSqCtGU");


        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng lat = place.getLatLng();

                videoMark = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).position(lat, 100f, 100f);
                gMap.addGroundOverlay(videoMark);

                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));

                jeju = place.getName();

                list_itemArrayList.add(new generalTimelineItem(jeju, Integer.toString(k), "", 0, 0));

                generalTimelineAdapter = new generalTimelineAdapter(generalMakeScheActivity.this, list_itemArrayList);
                listView.setAdapter(generalTimelineAdapter);

                size = generalTimelineAdapter.getCount();
                count = new int[tripdays][size];

                places = new String[tripdays][size];

                k++;
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

        previous = findViewById(R.id.previous1);
        next = findViewById(R.id.next1);

        if (day == 1) {previous.setVisibility(View.INVISIBLE);}

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
        taxi_btn = findViewById(R.id.taxi_btn1);
        trip_fin = findViewById(R.id.trip_fin);

        listView = findViewById(R.id.trip1);
        list_itemArrayList = new ArrayList<generalTimelineItem>();

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
                //Intent i = new Intent(getApplicationContext(), generalSTaxiActivity.class);
                //startActivity(i);
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

                for (int n = 0; n < size; n++)
                    places[day-1][n] = list_itemArrayList.get(n).getPlace();

                list_itemArrayList.clear();

                while (true) {
                    if (position == i) {
                        if ((size-1) == i){
                            if (count[day-1][i] != 1){
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "30분", 0, R.drawable.ic_local_taxi_black_24dp));
                                count[day-1][i] = 1;
                            }
                            else {
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "30분", 0, 0));
                                count[day-1][i] = 0;
                            }
                            break;
                        }
                        else {
                            if (count[day-1][i] != 1) {
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "1시간", R.drawable.ic_arrow_downward_black_24dp, R.drawable.ic_local_taxi_black_24dp));
                                count[day-1][i] = 1;
                            } else {
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "1시간", R.drawable.ic_arrow_downward_black_24dp, 0));
                                count[day-1][i] = 0;
                            }
                        }
                    }
                    else {
                        if ((size-1) == i){
                            if (count[day-1][i] == 1){
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "30분", 0, R.drawable.ic_local_taxi_black_24dp));
                            }
                            else {
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "30분", 0, 0));
                            }
                            break;
                        }else {
                            if (count[day-1][i] == 1) {
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "1시간", R.drawable.ic_arrow_downward_black_24dp, R.drawable.ic_local_taxi_black_24dp));
                            }
                            else {
                                list_itemArrayList.add(new generalTimelineItem(places[day-1][i], Integer.toString(i + 1), "1시간", R.drawable.ic_arrow_downward_black_24dp, 0));
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
                scroll1.requestDisallowInterceptTouchEvent(true);
            }
        });
    }
}