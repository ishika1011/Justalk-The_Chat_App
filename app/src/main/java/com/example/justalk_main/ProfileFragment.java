package com.example.justalk_main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    FloatingActionButton fab;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageProfilepicRef;
    //path
    String storagePath = "Users_profile_cover_Imgs/";
    Uri imageUri;
    StorageTask uploadTask;


    //views from xml
    ImageView avatarTv,coverIv;
    TextView nameTv, emailTv,phoneTv;
    RecyclerView postsRecyclerView;

    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;
    private String profileOrCoverPhoto;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_profile,container,false);

        //inflate this fragment
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        //init firebase

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageProfilepicRef= FirebaseStorage.getInstance().getReference().child("Users");

        Intent intent = getActivity().getIntent();
        uid = intent.getStringExtra("uid");


        // init views
        avatarTv = view.findViewById(R.id.avatarTv);
        nameTv = view.findViewById(R.id.nameTv);
        emailTv = view.findViewById(R.id.EmailTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        coverIv = view.findViewById(R.id.coverIv);
        fab = view.findViewById(R.id.fab);
        postsRecyclerView = view.findViewById(R.id.recyclerview_posts);

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //check until required data get
                for(DataSnapshot ds : snapshot.getChildren()){
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();



                    // set data

                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);

                    try{
                        //if image is received then set
                        Picasso.get().load(image).into(avatarTv);

                    }
                    catch (Exception e){
                        //// while there is any exception while getting image then set default
                        Picasso.get().load(R.drawable.ic_profile_black).into(avatarTv);

                    }

                    try{
                        //if image is received then set
                        Picasso.get().load(cover).into(coverIv);

                    }
                    catch (Exception e){
                        //// while there is any exception while getting image then set default


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }


        });


        //fab button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), EditProfileActivity.class));
            }
        });

        postList = new ArrayList<>();

        checkUserStatus();
        loadMyPosts();

        return view;
    }

    private void loadMyPosts() {
        //linear layout for recycle view


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        //query to load post
        Query query = ref.orderByChild("uid").equalTo(uid);

        //get all data from this ref
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelPost myPosts = ds.getValue(ModelPost.class);
                    //add to list
                    postList.add(myPosts);
                    //adapter
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    postsRecyclerView.setAdapter(adapterPosts);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setStackFromEnd(true);
                    layoutManager.setReverseLayout(true);
                    //set this to recycle view
                    postsRecyclerView.setLayoutManager(layoutManager);
                    //init posts

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void searchMyPosts(String searchQuery) {
        //linear layout for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest post first, for this load from last

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set this to recycle view
        postsRecyclerView.setLayoutManager(layoutManager);
        //init posts

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        //query to load post
        Query query = ref.orderByChild("uid").equalTo(uid);

        //get all data from this ref
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    ModelPost myPosts = ds.getValue(ModelPost.class);

                    if(myPosts.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())||
                            myPosts.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())){
                        //add to list
                        postList.add(myPosts);

                    }




                    //adapter
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    postsRecyclerView.setAdapter(adapterPosts);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),""+ error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private  void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstances) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstances);
    }
    @Override
    public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query)){
                    searchMyPosts(query);
                }
                else{
                    loadMyPosts();
                }
                //called when user press search button
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called when user type letter
                if(!TextUtils.isEmpty(newText)){
                    searchMyPosts(newText);
                }
                else{
                    loadMyPosts();
                }
                return false;
            }
        });


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