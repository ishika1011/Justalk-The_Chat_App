package com.example.justalk_main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justalk_main.adapters.Adapterusers;

import com.example.justalk_main.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class usersFragment extends Fragment {


    RecyclerView recyclerView;
    Adapterusers adapterusers;
    List<ModelUser> userList;

    //firebase auth
    FirebaseAuth firebaseAuth;

    public usersFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_users,viewGroup,false);

        //init
        firebaseAuth=FirebaseAuth.getInstance();

        recyclerView=view.findViewById(R.id.users_recyclerview);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //init userlist
        userList=new ArrayList<>();
        getAllUsers();

        return view;

    }

    private void getAllUsers() {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    ModelUser modelUers = ds.getValue(ModelUser.class);
                    userList.add(modelUers);

                    // assert modelUers != null;
                    //assert firebaseUser != null;
                    //if (modelUers.getId().equals(firebaseUser.getUid())) {
                    // userList.add(modelUers);
                    //}

                }

                adapterusers=new Adapterusers(getActivity(),userList);
                recyclerView.setAdapter(adapterusers);
                LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                layoutManager.setStackFromEnd(true);
                layoutManager.setReverseLayout(true);


            }


            @Override

            public void onCancelled(@NonNull DatabaseError error) {
                //  Toast.makeText(getActivity(),""+ error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUsers(String query) {

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                //get all search users
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    ModelUser modelUers = ds.getValue(ModelUser.class);
                    userList.add(modelUers);

                    // assert modelUers != null;
                    //assert firebaseUser != null;
                    //if (modelUers.getId().equals(firebaseUser.getUid())) {
                    //  userList.add(modelUers);
                    //}


                    adapterusers = new Adapterusers(getActivity(), userList);

                    //refresh adapter
                    // adapterusers.notifyItemChanged();
                    recyclerView.setAdapter(adapterusers);
                }

                LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                layoutManager.setStackFromEnd(true);
                layoutManager.setReverseLayout(true);


            }


            @Override

            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {


        }
        else {
            startActivity(new Intent(getActivity(),DashboardActivity.class));
            getActivity().finish();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);

        //searchview


        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);

        //search listner
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUsers(s);
                }
                else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUsers(s);
                }
                else {
                    getAllUsers();
                }
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
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
