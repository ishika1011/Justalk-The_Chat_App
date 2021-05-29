package com.example.justalk_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justalk_main.adapters.AdapterPosts;
import com.example.justalk_main.models.ModelPost;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    TextView verifyMsg;
    Button verifyEmail;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        verifyMsg = view.findViewById(R.id.verifyEmailMsg1);
        verifyEmail = view.findViewById(R.id.verifyEmailBtn1);
        if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
            verifyEmail.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send verification mail
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        verifyEmail.setVisibility(View.GONE);
                        verifyMsg.setVisibility(View.GONE);
                    }
                });
            }
        });
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        recyclerView = view.findViewById(R.id.postsRecyclerview2);
        postList = new ArrayList<>();
        loadPosts();
        return view;

    }

    private void loadPosts() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    adapterPosts = new AdapterPosts(getActivity(), postList);
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchPosts(String searchQuery){
        //path of all posts
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postList.clear();
//                for(DataSnapshot ds: dataSnapshot.getChildren()){
//                    ModelPost modelPost = ds.getValue(ModelPost.class);
//
//                    if(modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())|| modelPost.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())){
//                        postList.add(modelPost);
//                    }
//                    postList.add(modelPost);
//
//                    //adapter
//                    adapterPosts = new AdapterPosts(getActivity(),postList);
//                    //set adapter to recycler view
//                    recyclerView.setAdapter(adapterPosts);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //in case of error
//                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
//
//            }
//        });



    }


    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){

        }
        else{
            startActivity(new Intent(getActivity(),DashboardActivity.class));
            getActivity().finish();
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstances) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstances);
    }
    @Override
    public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //inflating menu
        inflater.inflate(R.menu.menu_main,menu);
        //searchview to search posts by post title description
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);

        //search listner
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //called when user press search button
                if(!TextUtils.isEmpty(query)){
                    searchPosts(query);
                }
                else{
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called when user press any letter
                if(!TextUtils.isEmpty(newText)){
                    searchPosts(newText);
                }
                else{
                    loadPosts();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);


        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_add_post){
            startActivity(new Intent(getActivity(),AddPostActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }

}