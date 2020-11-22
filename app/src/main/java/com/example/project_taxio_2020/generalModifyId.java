package com.example.project_taxio_2020;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class generalModifyId extends AppCompatActivity {
    private final int GALLERY_CODE = 10;
    private DrawerLayout drawerLayout;
    NavigationView nDrawer;
    EditText edtPassword, edtCheckPass, edtNum1, edtNum2;
    Spinner spinnerNum;
    Button btnComplete;
    TextView btnImg;
    ImageView photo;
    private FirebaseStorage storage;
    StorageReference storageRef, Ref;
    String general_num, imagePath, imageName, origin_pic;
    View header;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_modify_id);

        setToolbar();

        Intent i = getIntent();
        general_num = i.getStringExtra("general_num");

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nDrawer = (NavigationView)findViewById(R.id.nDrawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        header = nDrawer.getHeaderView(0);
        storage = FirebaseStorage.getInstance();
        Ref = storage.getReference();
        naviItem();
        setHeaderImage();

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(); //얘한테 줄거야

        edtPassword = findViewById(R.id.edtPassword);
        edtCheckPass = findViewById(R.id.edtCheckPass);
        edtNum1 = findViewById(R.id.edtNum1);
        edtNum2 = findViewById(R.id.edtNum2);


        spinnerNum = findViewById(R.id.spinnerNum);

        ArrayAdapter phoneAdapter = ArrayAdapter.createFromResource(this, R.array.phone, android.R.layout.simple_spinner_item);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(phoneAdapter);

        btnImg = findViewById(R.id.btnImg);
        btnComplete = findViewById(R.id.btnComplete);
        photo = findViewById(R.id.photo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        DatabaseReference gDatabase = FirebaseDatabase.getInstance().getReference("General");
        gDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot generalSnapshot : snapshot.getChildren()) {
                    if(generalSnapshot.child("general_num").getValue().toString().equals(general_num)) {
                        origin_pic = generalSnapshot.child("general_route").getValue().toString();
                        Log.d("KOO", origin_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri file = Uri.fromFile(new File(imagePath));
                StorageReference ref = Ref.child("general/"+file.getLastPathSegment());
                UploadTask uploadTask =  ref.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KOO", "completeFail");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("KOO", "complete");
                    }
                });
                String getGeneral_password = edtPassword.getText().toString();
                String getGeneral_call = spinnerNum.getSelectedItem().toString() + "-" + edtNum1.getText().toString() + "-" + edtNum2.getText().toString();
                String getGeneral_route = imageName;

                //부모 전화
                // 이미지 루트 데려오기
                HashMap result = new HashMap<>();
                if(!(getGeneral_password.equals(""))){
                    result.put("general_password", getGeneral_password);
                }
                if(!(getGeneral_call.equals("010--"))){
                    result.put("general_call", getGeneral_call);
                }
                if(getGeneral_route!=null){
                    DatabaseReference gDatabase = FirebaseDatabase.getInstance().getReference("General");
                    gDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot generalSnapshot : snapshot.getChildren()) {
                                if(generalSnapshot.child("general_num").getValue().toString().equals(general_num)) {
                                    storageRef = storage.getReferenceFromUrl("gs://taxio-b186e.appspot.com/general/"+generalSnapshot.child("general_route").getValue().toString());
                                    if(storageRef.getName().equals("member.png")){
                                        ;
                                    }else{
                                        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("KOO", "complete!");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                                Log.d("KOO", "Fail");
                                            }
                                        });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    result.put("general_route", getGeneral_route);
                }
                mDatabase.child("General").child(general_num).updateChildren(result);
                Intent intent = new Intent(getApplicationContext(), generalMainActivity.class);
                intent.putExtra("general_num", general_num);
                startActivity(intent);
                finish();

            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture();
            }
        });
    }
    public void getPicture(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE){
            imagePath = getPath(data.getData());
            Log.d("KOO", imagePath);
            File f = new File(imagePath);
            photo.setImageURI(data.getData());

            imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);

        }
    }

    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cu = cursorLoader.loadInBackground();
        int index = cu.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cu.moveToFirst();

        return cu.getString(index);
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
    public void naviItem(){

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
    public void setHeaderImage(){
        final TextView userName = header.findViewById(R.id.userName);
        final ImageView profile_pic = header.findViewById(R.id.profile_pic);

        DatabaseReference gDatabase = FirebaseDatabase.getInstance().getReference("General");
        gDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot generalSnapshot : snapshot.getChildren()) {
                    if(generalSnapshot.child("general_num").getValue().toString().equals(general_num)) {
                        userName.setText(generalSnapshot.child("general_name").getValue().toString());
                        storage = FirebaseStorage.getInstance();
                        storageRef = storage.getReferenceFromUrl("gs://taxio-b186e.appspot.com/general/"+generalSnapshot.child("general_route").getValue().toString());
                        GlideApp.with(getApplicationContext()).load(storageRef).into(profile_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
