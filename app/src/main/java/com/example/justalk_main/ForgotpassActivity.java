package com.example.justalk_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpassActivity extends AppCompatActivity {
    Button forgotpass;
    EditText emailEt;
    private FirebaseAuth mAuth;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        inflater = this.getLayoutInflater();
        mAuth = FirebaseAuth.getInstance();

        forgotpass = findViewById(R.id.fpass);
        emailEt = findViewById(R.id.email_fpass);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                EditText email = findViewById(R.id.email_fpass);
                if(email.getText().toString().isEmpty()){
                    email.setError("Fields are empty!");
                    return;
                }
                mAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                      Toast.makeText(ForgotpassActivity.this,"Reset Email Sent",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotpassActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });


    }


}

