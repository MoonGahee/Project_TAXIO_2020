package com.example.project_taxio_2020;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.AccessNetworkConstants;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; //ItemViewHolder와 ViewHolder의 차이?

import com.example.project_taxio_2020.databinding.GeneralDriverItemDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

// 기사 선택 리사이클러뷰 개당 by 가희

public class generalDriverAdapter extends RecyclerView.Adapter<generalDriverAdapter.ItemViewHolder> {
    // 주석 체크만 한 경우 ViewHolder > itemViewHolder로 변경함

    //Adapter에 들어갈 리스트트
    generalDriverDetailAdapter adapter = new generalDriverDetailAdapter();
    private ArrayList<generalDriverItem> dData = new ArrayList<>();
    generalEpilogueItem Edata;
    Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    final String distinction = "..........................";
    final String review = "기사님이 너무 친절하셨어요!";
    String general_num, schedule_num, date, driver_num;
    HashMap result = new HashMap<>();
    HashMap resultG, requestDay;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference("Driver");
    DatabaseReference Edatabase = database.getReference("Epilogue");
    DataSnapshot snapshot;
    int i = 0;

    FirebaseStorage storage;
    StorageReference storageRef;

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //
        TextView driverName;
        TextView infoDriver;
        TextView infoPrice;
        ImageView driverImg;
        ImageView plusBtn;
        TextView recruit;
        TextView sendMsg;
        RecyclerView recyclerView_driver_detail;
        generalDriverItem dataDriver;
        private int position;


        ItemViewHolder(View itemView) { //
            super(itemView);

            //객체 생성
            driverName = itemView.findViewById(R.id.driverName);
            infoDriver = itemView.findViewById(R.id.infoDriver);
            infoPrice = itemView.findViewById(R.id.infoPrice);
            driverImg = itemView.findViewById(R.id.driverImg);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            recruit = itemView.findViewById(R.id.recruit);
            sendMsg = itemView.findViewById(R.id.sendMsg);
            recyclerView_driver_detail = itemView.findViewById(R.id.recyclerView_driver_detail);
        }

        //값을 하나하나 보여주는 함수
        void onBind(generalDriverItem dataDriver, int position) {

            this.dataDriver = dataDriver;
            this.position = position;

            driverName.setText(dataDriver.getDriverName());
            infoDriver.setText(dataDriver.getDriverInfo());
            infoPrice.setText(dataDriver.getDirverPrice());
            storage = FirebaseStorage.getInstance();
            String route = "gs://taxio-b186e.appspot.com/driver/"+dataDriver.getDriverPhoto();
            storageRef = storage.getReferenceFromUrl(route);
            GlideApp.with(context).load(storageRef).into(driverImg);
            recyclerView_driver_detail.setAdapter(adapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView_driver_detail.setLayoutManager(linearLayoutManager);

            changeVisibility(selectedItems.get(position));

            plusBtn.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.plusBtn:
                    if (selectedItems.get(position)) {
                        //펼쳐진 아이템 클릭시
                        selectedItems.delete(position);
                        adapter.removeData(Edata);

                    } else {
                        selectedItems.delete(prePosition); //직전 클릭한 상태 삭제
                        selectedItems.put(position, true); //position에 저장

                        getData(dataDriver.getDriverPhoto(), dataDriver.getDriverName());
                    }
                    //해당 포지션의 변화
                    if (prePosition != -1)
                        notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;

                    break;
            }
        }

        private void changeVisibility(final boolean isExpanded) {
            int dpValue = 100;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);


            //뷰가 변할 값을 지정
            final ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);


            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue(); //height
                    recyclerView_driver_detail.getLayoutParams().height = value;
                    recyclerView_driver_detail.requestLayout();
                    recyclerView_driver_detail.setVisibility(isExpanded ? View.VISIBLE : View.GONE); //사라지게 됨
                }
            });
            va.start();
        }
    }


    @NonNull
    @Override //view를 인플레이터함
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_driver_item, parent, false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override // position에 맞추어 각 항목을 구성하기 위해서 호출함
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) { //
        holder.onBind(dData.get(position), position);

        holder.recruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("기사 요청");
                builder.setMessage(dData.get(position).getDriverName() + " 기사님에게 요청하시겠습니까?\n금액은 시간 당 " + dData.get(position).getDirverPrice() + "입니다.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDate(position);
                        Intent i = new Intent(context, generalReservationCompleteActivity.class);
                        i.putExtra("general_num", general_num);
                        i.putExtra("schedule_num", schedule_num);
                        i.putExtra("tripDate", date);
                        context.startActivity(i);

                        //문자 전송
                       String call = dData.get(position).getDriverCall();
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(call, null, "요청이 들어왔습니다.", null, null);


                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.show();
            }
        });

        holder.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("문자 전송");
                builder.setMessage(dData.get(position).getDriverName() + " 기사님에게 문자를 \n전송하시겠습니까?"); //기사 이름으로 변경
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("email", dData.get(position).getDriverEmail());
                                context.startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("아니오", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() { // recyclerview의 총 개수 반환
        return dData.size();
    }

    void addData(generalDriverItem data) { // 1. RecruitDriver에서 호출 > 2. DriverData값을 가져와서 > 3. 이 곳에 DriverData를 추가
        dData.add(data);
    }

    void getnum(String general_num, String schedule_num) {
        this.general_num = general_num;
        this.schedule_num = schedule_num;
    }

    void getData(final String image, final String name) {
        Edatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.getChildren()) {
                    Epilogue item = new Epilogue();
                    item.setImage(column.child("image").getValue(String.class));
                    item.setDriver_num(column.child("driver_name").getValue(String.class));
                    item.setGeneral_num(column.child("general_name").getValue(String.class));
                    item.setReview(column.child("content").getValue(String.class));
                    item.setScore(Float.parseFloat(column.child("rating").getValue(String.class)));

                    if (item.getDriver_num().equals(name)) {
                        Edata = new generalEpilogueItem(item.getImage(), item.getGeneral_num(), item.getScore(), item.getReview());
                    }

                    adapter.addData(Edata);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getDate(final int position) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("General");
        ValueEventListener generalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driver : snapshot.getChildren()) {
                    if (dData.get(position).getDriverName().equals(driver.child("driver_name").getValue(String.class))) {
                        driver_num = driver.getKey();
                    }
                }

                i = 1;
                Driver drivers = snapshot.child(driver_num).getValue(Driver.class);
                String driver_name = drivers.getDriver_name();
                resultG = new HashMap<>();
                resultG.put("driver_name", driver_name);

                for (DataSnapshot column : snapshot.child(driver_num).child("Request").getChildren()) {
                    if(Integer.parseInt(column.getKey()) != i)
                    { //여기가 이상한 것 같은데
                        break;
                    } else {
                        i++;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //없는 경우
            }
        };

        Log.d("moon", String.valueOf(i));
        databaseRef.addListenerForSingleValueEvent(generalListener); //콜백 한 번만 호출이 이뤄지는 경우

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase.child(general_num).child("Schedule").child(schedule_num).updateChildren(resultG);
                General general = snapshot.child(general_num).getValue(General.class);
                String general_name = general.getGeneral_name();
                result.put("general_name", general_name);


                Schedule schedule = snapshot.child(general_num).child("Schedule").child(schedule_num).getValue(Schedule.class);
                String date = schedule.getPrintDate();
                String departure = schedule.getDeparture_date();
                String arrival = schedule.getArrival_date();
                Log.d("KOO", date);
                result.put("departure", departure);
                result.put("arrival", arrival);
                result.put("days", date);
                result.put("state", "0");
                result.put("request_num", Integer.toString(i));
                String request_num=Integer.toString(i);
                databaseRef.child(driver_num).child("Request").child(Integer.toString(i)).updateChildren(result);

                if(!databaseRef.child(driver_num).child("Request").child(Integer.toString(i)).getKey().equals("")) {
                    for (DataSnapshot column : snapshot.child(general_num).child("Schedule").child(schedule_num).child("days").getChildren()) {

                        requestDay = new HashMap<>();

                        DataSnapshot dateSchedule = column.child("Date_Schedule");
                        DataSnapshot dateCourse = column.child("Date_Course");

                        String start_time = dateSchedule.child("start_time").getValue(String.class);
                        StringJoiner lists = new StringJoiner("-");

                        String day = dateSchedule.child("schedule_date").getValue(String.class);
                        String time = dateSchedule.child("taxi_time").getValue(String.class);
                        for (DataSnapshot couresPlace : dateCourse.getChildren()) {
                            lists.add(couresPlace.child("coures_place").getValue(String.class));
                        }

                        String list = lists.toString();

                        requestDay.put("day", day);
                        requestDay.put("time", time);
                        requestDay.put("start_time", start_time);
                        requestDay.put("course", list);


                        Log.d("Moon-Test", column.toString());

                        databaseRef.child(driver_num).child("Request").child(request_num).child("RequestDay").child(Integer.toString(i)).updateChildren(requestDay);
                        i++;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}