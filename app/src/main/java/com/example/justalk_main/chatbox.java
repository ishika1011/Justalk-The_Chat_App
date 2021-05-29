package com.example.justalk_main;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class chatbox extends AppCompatActivity {

    EditText userInput;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
//     com.example.justalk_main.MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);
        userInput=findViewById(R.id.userInput);
        recyclerView=findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
       // messageAdapter=new com.example.justalk_main.MessageAdapter(responseMessageList);
        Object reverseLyout;
        //recyclerView.setLayoutManager(new LinearLayoutManager( this, LinearLayoutManager.VERTICAL,reverseLyout false));
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            private Object isMe;

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                  //  com.example.justalk_main.ResponseMessage message =new com.example.justalk_main.ResponseMessage(userInput.getText().toString(), isMe:true);
                    //responseMessageList.add(message);
                   // com.example.justalk_main.ResponseMessage message2 =new com.example.justalk_main.ResponseMessage(userInput.getText().toString(), isMe: true);
                   // responseMessageList.add(message2);
                  //  messageAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }
}