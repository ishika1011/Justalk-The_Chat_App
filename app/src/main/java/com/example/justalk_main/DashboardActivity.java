package com.example.justalk_main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity {

    //firebase auth
    FirebaseAuth firebaseAuth;

    ActionBar actionBar;

    String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        actionBar= getSupportActionBar();
        actionBar.setTitle("Profile");

        //init
        firebaseAuth=FirebaseAuth.getInstance();


        //default fragment
        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {






            protected void onResume() {
                checkUserStatus();
                DashboardActivity.super.onResume();
            }


           /* public void updateToken(String token){
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
                Token mToken=new Token(token);
                ref.child(mUID).setValue(mToken);
            }*/

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        //fragment transaction
                        actionBar.setTitle("Home");
                        HomeFragment fragment1 = new HomeFragment();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content, fragment1, "");
                        ft1.commit();
                        return true;

                    case R.id.profile:

                        actionBar.setTitle("Profile");
                        ProfileFragment fragment2 = new ProfileFragment();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.content, fragment2, "");
                        ft2.commit();
                        return true;

                    case R.id.users:

                        actionBar.setTitle("Users");
                        usersFragment fragment3= new usersFragment();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.content, fragment3, "");
                        ft3.commit();
                        return true;

                    case R.id.chats:

                        actionBar.setTitle("Chats");
                        chatlistFragment fragment4= new chatlistFragment();
                        FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.content, fragment4, "");
                        ft4.commit();
                        return true;




                }
                return false;
            }

            private void checkUserStatus() {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUID=user.getUid();

                    SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("Current_USERID",mUID);
                    editor.apply();

                }
                else {
                    startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                    finish();
                }
            }

            protected void onStart(){
                checkUserStatus();
                DashboardActivity.super.onStart();
            }


        });


    }



}