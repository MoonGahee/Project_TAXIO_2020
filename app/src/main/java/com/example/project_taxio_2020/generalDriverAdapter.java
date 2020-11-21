package com.example.project_taxio_2020;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; //ItemViewHolder와 ViewHolder의 차이?

import com.example.project_taxio_2020.databinding.GeneralDriverItemDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference("Driver");


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

                        Edata = new generalEpilogueItem(R.drawable.profile, dataDriver.getDriverName(), 4.0f, distinction, review);
                        adapter.addData(Edata);
                        adapter.notifyDataSetChanged();
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
            int dpValue = 200;
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
                builder.setMessage(dData.get(position).getDriverName() + " 기사님에게 요청하시겠습니까?\n금액은 60,000원입니다.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot driver : snapshot.getChildren()) {
                                    if (dData.get(position).getDriverName().equals(driver.child("driver_name").getValue(String.class))) {
                                        driver_num = driver.getKey();
                                    }
                                }
                                getDate();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Intent i = new Intent(context, generalReservationCompleteActivity.class);
                        i.putExtra("general_num", general_num);
                        i.putExtra("schedule_num", schedule_num);
                        i.putExtra("tripDate", date);
                        context.startActivity(i);
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
                builder.setMessage("아이린 기사님에게 문자를 \n전송하시겠습니까?");
                builder.setPositiveButton("예", null);
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

    void getTime() {
    }

    void getDate() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("General");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            //int i = Integer.parseInt(databaseRef.child(driver_num).child("Driver_Schedule").getKey()); //if문 설정해야함
            int i = 1;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column : snapshot.child(general_num).child("Schedule").child(schedule_num).child("days").getChildren()) {
                    //추가할 수 있도록
                    DataSnapshot dateSchedule = column.child("Date_Schedule");
                    DataSnapshot dateCourse = column.child("Date_Course");
                    Date_Schedule dateScheduleItem = new Date_Schedule();
                    dateScheduleItem.setGeneral_num(general_num);
                    dateScheduleItem.setSchedule_date(dateSchedule.child("schedule_date").getValue(String.class));
                    dateScheduleItem.setStart_time(dateSchedule.child("start_time").getValue(String.class));
                    dateScheduleItem.setTaxi_time(dateSchedule.child("taxi_time").getValue(String.class));
                    dateScheduleItem.setBoarding_status(dateSchedule.child("boarding_status").getValue(Boolean.class));
                    StringJoiner lists = new StringJoiner("-");
                    String day = dateSchedule.child("schedule_date").getValue(String.class);
                    for (DataSnapshot couresPlace : dateCourse.getChildren()) {
                        lists.add(couresPlace.child("coures_place").getValue(String.class));
                    }
                    String list = lists.toString();

                    result.put("day", day);
                    result.put("course", list);
                    Log.d("Moon-Test", column.toString());
                    databaseRef.child(driver_num).child("Driver_Schedule").child(Integer.toString(i)).updateChildren(result);
                    i++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}