/*
* package com.example.justalk_main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justalk_main.R;
import com.example.justalk_main.models.ModelUser;

import java.util.HashMap;
import java.util.List;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder> {

    Context context;
    List<ModelUser> mUser;
    private HashMap<String,String> lastMessageMap;

    public AdapterChatlist(Context context, List<ModelUser> mUser, HashMap<String, String> lastMessageMap) {
        this.context = context;
        this.mUser = mUser;
        this.lastMessageMap = lastMessageMap;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

       // View view= LayoutInflater.from(context).inflate(R.layout.row_chatlist,viewGroup,false);
       // return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        ImageView profileIv,onlineStatusIv;
        TextView nameTv,lastMessageTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileIv=itemView.findViewById(R.id.profileIv);
           // onlineStatusIv=itemView.findViewById(R.id.onlineStatusIv);
            //nameTv=itemView.findViewById(R.id.nameTv);
            //lastMessageTv=itemView.findViewById(R.id.lastMessageTv);

        }
    }
}
*/