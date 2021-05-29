package com.example.justalk_main.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justalk_main.R;
import com.example.justalk_main.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
        Context context;
        List<ModelChat>chatList;
        String imageUrl;

        FirebaseUser fUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.chat_right,viewGroup,false);
            return new MyHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_left,viewGroup,false);
            return new MyHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder, int i) {
        //get data
        String message=chatList.get(i).getMessage();
        String timeStamp=chatList.get(i).getTimestamp();

        //convert time stamp....
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dataTime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

        //set data
        myholder.messageTv.setText(message);
        myholder.timetV.setText(dataTime);
        try {
            Picasso.get().load(imageUrl).into(myholder.profileTv);
        }
        catch (Exception e){

        }

        //click to shoe delete dialog
       /*
       * myholder.messageLAyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show delete message.....
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you to delete this message?");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //deleteMEssage(i);
                    }
                });
                //cancle....
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss dialog
                        dialog.dismiss();
                    }
                });
                //create and....
                builder.create().show();

                }
        });*/

        //set seen/deriverd.....
        if (i==chatList.size()-1){
            if(chatList.get(i).isSeen()){
                myholder.isSeen.setText("seen");
            }
            else {
                myholder.isSeen.setText("Deliverd");
            }
        }
        else {
            myholder.isSeen.setVisibility(View.GONE);
        }

    }

    /*
    * private void deleteMEssage(int position) {
        String myUID=FirebaseAuth.getInstance().getCurrentUser().getUid();

        //logic....
        String msgTimeStamp=chatList.get(position).getTimestamp();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Chats");
        Query query=dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    if (ds.child("sender").getValue().equals(myUID)){
                        ///1) first method
                       // ds.getRef().removeValue();

                        //2)2nd method....
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("message","This message was deleted....");
                        ds.getRef().updateChildren(hashMap);

                        Toast.makeText(context,"message deleted....",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,"You can delete only your message...",Toast.LENGTH_SHORT).show();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently...
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }

    //viewholder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views
        ImageView profileTv;
        TextView messageTv,timetV,isSeen;
        LinearLayout messageLAyout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            profileTv=itemView.findViewById(R.id.profileIv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timetV=itemView.findViewById(R.id.timeTv);
            isSeen=itemView.findViewById(R.id.isSeenTv);
            messageLAyout=itemView.findViewById(R.id.messageLayout);
        }
    }
}

