package com.example.justalk_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justalk_main.adapters.AdapterPosts;
import com.example.justalk_main.models.ModelPost;
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
import java.util.List;

import static com.example.justalk_main.R.menu.menu_main;

public class ThereProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    //views from xml
    ImageView avatarTv,coverIv;
    TextView nameTv, emailTv,phoneTv;

    RecyclerView postsRecyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // init views
        avatarTv = findViewById(R.id.avatarTv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.EmailTv);
        phoneTv = findViewById(R.id.phoneTv);
        coverIv = findViewById(R.id.coverIv);

        postsRecyclerView = findViewById(R.id.recyclerview_posts);
        firebaseAuth = FirebaseAuth.getInstance();

        //get uid of clicked user to retrive his posts
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");


        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(uid);
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
        postList = new ArrayList<>();

        loadHisPosts();
        checkUserStatus();
    }

    private void loadHisPosts() {
        //linear layout for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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

                    //add to list
                    postList.add(myPosts);


                    //adapter
                    adapterPosts = new AdapterPosts(ThereProfileActivity.this,postList);
                    postsRecyclerView.setAdapter(adapterPosts);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereProfileActivity.this,""+ error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void serachHisPosts(String searchQuery){
        //linear layout for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
                    adapterPosts = new AdapterPosts(ThereProfileActivity.this,postList);
                    postsRecyclerView.setAdapter(adapterPosts);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereProfileActivity.this,""+ error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private  void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!= null){


        }

        else{
            startActivity(new Intent(this,HomeFragment.class));
            finish();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_main,menu);
        menu.findItem(R.id.action_add_post).setVisible(false);


        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query)){
                    serachHisPosts(query);
                }
                else{
                    loadHisPosts();
                }
                //called when user press search button
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called when user type letter
                if(!TextUtils.isEmpty(newText)){
                    serachHisPosts(newText);
                }
                else{
                    loadHisPosts();
                }
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_add_post){
            startActivity(new Intent(ThereProfileActivity.this,AddPostActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }

}