package com.example.justalk_main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    ImageView change_picture_pic,change_name_pic,change_background_pic,change_number_pic,change_password_pic,block_account_pic,logout_pic,delete_account_pic;

    TextView change_picture_text,change_name_text,change_background_text,change_number_text,change_password_text,block_account_text,logout_text,delete_account_text;
    TableLayout gridView;


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference ;
    //path where images will be stored
    String storagePath = "Users_profile_cover_Imgs/";

    //progressDilaog
    ProgressDialog pd;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];

    //uri of picked image
    Uri image_uri;

    //for checking profile or cover photo
    String profileOrCoverPhoto;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // init arrays of permissions
        cameraPermissions= new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        databaseReference = firebaseDatabase.getReference("Users");

        //storageReference.getName().equals(storageReference.getName());    // true
        // storageReference.getPath().equals(storageReference.getPath()); //false


        // storageProfilepicRef= FirebaseStorage.getInstance().getReference().child("ProfilePic");

        // init values


        change_picture_pic =findViewById(R.id.change_picture_pic);
        change_name_pic =findViewById(R.id.change_name_pic);
        change_background_pic =findViewById(R.id.change_background_pic);
        change_number_pic =findViewById(R.id.change_number_pic);
        change_password_pic=findViewById(R.id.change_password_pic);
        block_account_pic =findViewById(R.id.block_account_pic);
        logout_pic =findViewById(R.id.logout_pic);
        delete_account_pic =findViewById(R.id.delete_account_pic);

        change_picture_text =findViewById(R.id.change_picture_text);
        change_name_text =findViewById(R.id.change_name_text);
        change_background_text =findViewById(R.id.change_background_text);
        change_number_text =findViewById(R.id.change_number_text);
        change_password_text=findViewById(R.id.change_password_text);
        block_account_text =findViewById(R.id.block_account_text);
        logout_text =findViewById(R.id.logout_text);
        delete_account_text =findViewById(R.id.delete_account_text);


        pd = new ProgressDialog(this);



        change_picture_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Profile Picture");
                profileOrCoverPhoto = "image";
                showImagePicDilog();
            }
        });

        change_picture_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating profile Picture");
                profileOrCoverPhoto = "image";
                showImagePicDilog();

            }
        });

        change_name_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Profile name");
                showNamePhoneUpdateDialog("name");

            }
        });

        change_name_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Profile name");
                showNamePhoneUpdateDialog("name");

            }
        });

        change_background_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Profile Background");
                profileOrCoverPhoto = "cover";
                showImagePicDilog();

            }
        });

        change_background_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Profile Background");
                profileOrCoverPhoto = "cover";
                showImagePicDilog();

            }
        });

        change_number_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Number");
                showNamePhoneUpdateDialog("phone");

            }
        });

        change_number_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Number");
                showNamePhoneUpdateDialog("phone");


            }
        });

        change_password_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this,ForgotpassActivity.class);
                startActivity(intent);


            }
        });

        change_password_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this,ForgotpassActivity.class);
                startActivity(intent);

            }
        });

        block_account_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Blocking Account");

            }
        });

        block_account_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Blocking Account");

            }
        });

        logout_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Logging Out");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EditProfileActivity.this.getApplicationContext(), LoginActivity.class));
                finish();

            }
        });

        logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Logging Out");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EditProfileActivity.this.getApplicationContext(), LoginActivity.class));
                finish();

            }
        });

        delete_account_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Deleting Account");

            }
        });

        delete_account_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Deleting Account");

            }
        });

    }

    private void showNamePhoneUpdateDialog(String key) {

        //custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update"+key);

        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        //add edit text
        EditText editText =  new EditText(this);
        editText.setHint("Enter"+ key);

        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add buttons
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();

                //validate
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key,value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(EditProfileActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(EditProfileActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            });

                    //if user change is name or phone number also update in his post
                    if(key.equals("name")){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                        Query query = ref.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for ( DataSnapshot ds: snapshot.getChildren()){
                                    String child = ds.getKey();
                                    snapshot.getRef().child(child).child("uName").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
                else{
                    Toast.makeText(EditProfileActivity.this,"Please Enter"+key,Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        //create and show dialog to cancel
        builder.create().show();



    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }


    private void showImagePicDilog() {
        String options[] = {"Camera","Gallery"};
        //alert dilog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //camera clicked
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        pickFromCamera();
                    }

                }
                else if(which == 1){
                    // gallery clicked
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{

                if (grantResults.length >0){
                    int cameraAccepted = grantResults[0] = PackageManager.PERMISSION_GRANTED;
                    int writeStorageAccepted = grantResults[1] = PackageManager.PERMISSION_GRANTED;

                    if((cameraAccepted == PackageManager.PERMISSION_GRANTED) && (writeStorageAccepted == PackageManager.PERMISSION_GRANTED)) {

                        pickFromCamera();

                    }
                    else{
                        Toast.makeText(this,"Please enable camera & storage permission",Toast.LENGTH_SHORT).show();
                    }

                }

            }
            break;
            case STORAGE_REQUEST_CODE:{

                if (grantResults.length >0){
                    int writeStorageAccepted = grantResults[1] = PackageManager.PERMISSION_GRANTED;
                    if((writeStorageAccepted == PackageManager.PERMISSION_GRANTED)) {

                        pickFromGallery();

                    }
                    else{
                        Toast.makeText(this,"Please enable storage permission",Toast.LENGTH_SHORT).show();
                    }

                }

            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                // image is picked from gallery,get uri of image
                image_uri = data.getData();

                uploadProfileCoverPhoto(image_uri);

            }

            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                // image is picked from camera,get uri of image

                uploadProfileCoverPhoto(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        pd.show();

        //path and name of the image to be stored in firebase storage
        String filePathAndName = storagePath+ ""+ profileOrCoverPhoto+"_"+user.getUid();

        StorageReference storageReference1 = storageReference.child(filePathAndName);
        storageReference1.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        //check if image is uploaded or not and url is received
                        if (uriTask.isSuccessful()) {
                            //image uploaded
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profileOrCoverPhoto, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            pd.dismiss();
                                            Toast.makeText(EditProfileActivity.this, "Image Updated....", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            pd.dismiss();
                                            Toast.makeText(EditProfileActivity.this, "Error Occoured....", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                        }
                        if (profileOrCoverPhoto.equals("image")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                            Query query = ref.orderByChild("uid").equalTo(uid);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String child = ds.getKey();
                                        snapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }



                        else{
                            //erroe
                            pd.dismiss();
                            Toast.makeText(EditProfileActivity.this,"some error occoured", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        //put_uri
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }
}