package com.example.justalk_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText registerEmail, registerPassword,RegisterName,RegisterConfPass;
    Button btnSignUp;


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        mAuth = FirebaseAuth.getInstance();
        registerEmail = findViewById(R.id.signupEmail);
        registerPassword = findViewById(R.id.signupPassword);
        btnSignUp = findViewById(R.id.Signup);
        RegisterName= findViewById(R.id.signupName);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser() {
        final String email = registerEmail.getText().toString();
       final String pass = registerPassword.getText().toString();
        final String name = RegisterName.getText().toString();



        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if(!pass.isEmpty()){

                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String userId = firebaseUser.getUid();


                                root = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("id", userId);
                               // userMap.put("name", name);
                                userMap.put("email", email);
                               // userMap.put("phone_no","");
                               // userMap.put("image","");
                               // userMap.put("cover","");
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                               // intent.putExtra("userId", userId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                                root.push().setValue(userMap);


                                Toast.makeText(SignupActivity.this,"Registered",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this,DashboardActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this,"Registration Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                registerPassword.setError("Please enter your password");
            }
        }else if(email.isEmpty()){
            registerEmail.setError("Please enter your Email");
        }else {
            registerEmail.setError("Please enter Correct Email");
        }
    }
}