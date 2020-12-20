package com.example.project_taxio_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private RecyclerView chat_recycler;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager manager;
    EditText chatEdit;
    Button chatBtn;
    String email, destinationEmail, chatRoomId;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    ArrayList<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent i = getIntent();
        destinationEmail = i.getStringExtra("email");
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        
        chatBtn = findViewById(R.id.chatBtn);
        chatEdit = findViewById(R.id.chatEdit);
        chat_recycler = findViewById(R.id.chat_recycler);
        chat_recycler.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        myAdapter = new MyAdapter(chats, email);
        chat_recycler.setAdapter(myAdapter);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = new Chat();
                chat.users.put(email, true);
                chat.users.put(destinationEmail, true);
                FirebaseDatabase.getInstance().getReference().child("chatRooms").push().setValue(chat);

                if(chatRoomId ==null){
                    chatBtn.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatRooms").push().setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                }else{
                    Chat.Comment comment = new Chat.Comment();
                    comment.email = email;
                    comment.text = chatEdit.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatRooms").child(chatRoomId).child("comments").push().setValue(comment);
                }



                Calendar c = Calendar.getInstance();
                SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd hh:mm");
                String dateTime = date.format(c.getTime());


            }
        });
    }

    public void checkChatRoom(){
        chatBtn.setEnabled(true);
        FirebaseDatabase.getInstance().getReference().child("chatRooms").orderByChild("users/"+email).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()){
                    Chat chat = item.getValue(Chat.class);
                    if(chat.users.containsKey(destinationEmail)){
                        chatRoomId = item.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*class chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Chat.Comment> comments;

        public chatAdapter() {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,
            int viewType){
                return null;
            }

            @Override
            public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder,int position){

            }

            @Override
            public int getItemCount () {
                return 0;
            }


        }
    }*/
}
