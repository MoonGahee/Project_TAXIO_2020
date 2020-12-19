package com.example.project_taxio_2020;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class driverMainDialog {

    private Context context;

    public driverMainDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String driver_num, final String number) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.driver_main_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();


        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView days = dlg.findViewById(R.id.days);
        final ListView dialog_list = dlg.findViewById(R.id.dialog_list);
        final Button negative = dlg.findViewById(R.id.negative);
        final Button positive = dlg.findViewById(R.id.positive);

        myAdapter adapter = new myAdapter();
        dialog_list.setAdapter(adapter);
        setList(adapter, driver_num);
        final DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("General");
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Driver");
        final String[] tripDate = {null};
        dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot column :snapshot.child(driver_num).child("Request").getChildren())
                    if(column.getKey().equals("number"))
                        tripDate[0] = column.child("days").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        days.setText(tripDate[0]);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] name = {null};
                dDatabase.child(driver_num).child("Request").child(number).child("state").setValue("2");
                dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (final DataSnapshot request : snapshot.child(driver_num).child("Request").getChildren()) {
                            if (request.getKey().equals(number)) {
                                name[0] = request.child("general_name").getValue(String.class);
                                final String date = request.child("RequestDay").child("day").getValue(String.class);
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot column : snapshot.child(name[0]).child("Schecule").getChildren()) {
                                            String day = column.child("days").child("schedule_date").getValue(String.class);
                                            if (date.equals(day)) {
                                                mDatabase.child(name[0]).child("Schedule").child("reservation_state").setValue("2");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                dlg.dismiss();
            }

            });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap result = new HashMap<>();
                        int i = 1;
                        for (DataSnapshot column : snapshot.child(driver_num).child("Driver_Schedule").getChildren()) {
                            if (Integer.parseInt(column.getKey()) != i) { //여기가 이상한 것 같은데
                                break;
                            } else {
                                i++;
                            }
                        }
                        for (final DataSnapshot request : snapshot.child(driver_num).child("Request").getChildren()) {
                                dDatabase.child(driver_num).child("Request").child("state").setValue("1");
                                result.put("days", request.child("RequestDay").child("day").getValue(String.class));
                                result.put("general_name", request.child("general_name").getValue(String.class));
                                result.put("couse", request.child("RequestDay").child("course").getValue(String.class));
                                result.put("time", request.child("RequestDay").child("time").getValue(String.class));
                                result.put("start_time", request.child("RequestDay").child("start_time").getValue(String.class));
                                databaseRef.child(driver_num).child("Request").child(Integer.toString(i)).updateChildren(result);
                                i++;
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot column : snapshot.child(request.child("general_name").getValue(String.class)).child("Schecule").getChildren()) {
                                            String day = column.child("days").child("schedule_date").getValue(String.class);
                                            if (request.child("RequestDay").child("day").getValue(String.class).equals(day)) {
                                                mDatabase.child(request.child("general_name").getValue(String.class)).child("Schedule").child("reservation_state").setValue("1");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dlg.dismiss();
            }
        });
        }
    public void setList(final myAdapter adapter, final String driver_num){//데이터베이스에 있는 동물병원 정보를 어댑터에 추가
        DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot column : snapshot.child(driver_num).child("Request").getChildren()){
                    if (column.child("state").getValue().toString().equals("0")) {
                        for(DataSnapshot day : snapshot.child(driver_num).child("Request").child("RequestDay").getChildren()){
                            String date = day.child("day").getValue(String.class);
                            String start_time = day.child("start_time").getValue(String.class);
                            String course = day.child("course").getValue(String.class);
                            String time = day.child("time").getValue(String.class);
                            adapter.addItem(start_time, time, course, date);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class myAdapter extends BaseAdapter {

        public ArrayList<RequestDayItem> list = new ArrayList<RequestDayItem>();

        //초기화
        public myAdapter() {
        }

        //리스트 뷰 아이템 개수 알려줌
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //현재 아이템 알려줌
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        //리스트뷰에서 아이템과 xml을 연결하여 화면에 출력
        @NonNull
        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

            final Context c = parent.getContext();
            if (view == null) {//xml로 된 layout파일을 view 객체로 변환
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.dialog_list, parent, false);
            }

            TextView date = view.findViewById(R.id.date);
            TextView time = view.findViewById(R.id.time);
            TextView start_time = view.findViewById(R.id.start_time);
            TextView course = view.findViewById(R.id.course);
            final RequestDayItem myList = list.get(position);
            date.setText(myList.getDay());
            time.setText(myList.getTime());
            start_time.setText(myList.getStart_time());
            course.setText(myList.getCourse());

            return view;
        }

        //HosListItem 클래스에 정의한 set 메소드에 데이터를 담아서 리턴함
        public void addItem(String start_time, String time, String course, String day) {
            RequestDayItem item = new RequestDayItem();

            item.setDay(day);
            item.setCourse(course);
            item.setStart_time(start_time);
            item.setTime(time);

            list.add(item);
        }
    }
}
