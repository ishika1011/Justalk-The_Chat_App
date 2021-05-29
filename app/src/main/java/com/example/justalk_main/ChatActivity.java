package com.example.justalk_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv,userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;


    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersdRef;

    ValueEventListener seenListener;
   /* List<ModelChat> chatList;
    AdapterChat adapterChat;*/

    String hisid;
    String myid;
    String hisImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_activity);

        Toolbar toolbar=findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView=findViewById(R.id.chat_recyclerView);
        profileIv=findViewById(R.id.profileIv);
        nameTv=findViewById(R.id.nameTv);
        userStatusTv=findViewById(R.id.userStatusTv);
        messageEt=findViewById(R.id.messageEt);
        sendBtn=findViewById(R.id.sendBtn);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        //recycle....
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent=getIntent();
        hisid=intent.getStringExtra("hisUid");


        //init
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseDatabase=FirebaseDatabase.getInstance();
        usersdRef=firebaseDatabase.getReference("Users");

        //search user....
        Query userQuery=usersdRef.orderByChild("id").equalTo(hisid);
        //get...
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check until required....
                for(DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    String name=""+ds.child("name").getValue();
                    String image=""+ds.child("image").getValue();

                    //set data
                    nameTv.setText(name);
                    try{
                        Picasso.get().load(image).placeholder(R.drawable.ic_add_image).into(profileIv);
                    }
                    catch (Exception e){
                       // Picasso.get().load(R.drawable.ic_add_image).into(profileIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text....
                String message=messageEt.getText().toString().trim();
                //check.....
                if(TextUtils.isEmpty(message)){
                    //text empty
                    Toast.makeText(ChatActivity.this,"Cannot send empty text...",Toast.LENGTH_SHORT).show();
                }
                else {
                    sendMessage(message);
                }
            }
        });
        readMessage();

        seenMessage();


    }

    private void seenMessage() {
        DatabaseReference usersdRefSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=usersdRefSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                 // ModelChat chat=ds.getValue(ModelChat.class);
               // if(chat.getReceiver().equals(myid) && chat.getSender().equals(hisid)){
                 //       HashMap<String,Object> hasSeenHashMap=new HashMap<>();
                   //     hasSeenHashMap.put("isSeen",true);
                     //   ds.getRef().updateChildren(hasSeenHashMap);
                  //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage() {
        /*chatList=new ArrayList<>();*/
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               /* chatList.clear();*/
                for(DataSnapshot ds:snapshot.getChildren()){
                   // ModelChat chat=ds.getValue(ModelChat.class);

                    Toast.makeText(ChatActivity.this, hisid, Toast.LENGTH_SHORT).show();

                 /*   if(chat.getReceiver().equals(myid) && chat.getSender().equals(hisid) || chat.getReceiver().equals(hisid) && chat.getSender().equals(myid)){
                        chatList.add(chat);


                    }*/
                   // if(chat.getReceiver().equals(myid)){
                        //chatList.add(chat);
                  //  }
                    /*adapterChat=new AdapterChat(ChatActivity.this,chatList,hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

            String timestamp=String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myid);
        hashMap.put("Receiver",hisid);
        hashMap.put("message",message);
        hashMap.put("timeStamp",message);
        hashMap.put("isSeen",false);
        databaseReference.child("Chats").push().setValue(hashMap);

        //reset ...
        messageEt.setText("");
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            myid=user.getUid();


        }
        else {
            startActivity(new Intent(this,DashboardActivity.class));
            finish();
        }
    }


    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        usersdRef.removeEventListener(seenListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_search){
            firebaseAuth.signOut();
            checkUserStatus();

        }
        return super.onOptionsItemSelected(item);
    }
}
