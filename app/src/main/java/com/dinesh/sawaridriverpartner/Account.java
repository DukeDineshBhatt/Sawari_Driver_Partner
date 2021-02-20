package com.dinesh.sawaridriverpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.internal.AccountType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Account extends BaseActivity {
    ImageView  edit,edit1,edit2;
    Button logout;
    String user_id;
    TextView name, mobile, alt_mobile, email;
    ProgressBar progressbar;
    private final int PICK_IMAGE_REQUEST = 22;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private Uri filePath;
    DatabaseReference usersRef;
    FirebaseDatabase database;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        alt_mobile = findViewById(R.id.alt_mobile);
        email = findViewById(R.id.email);
        progressbar = findViewById(R.id.progressbar);
        edit = findViewById(R.id.edit);
        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        user_id = (shared.getString("user_id", ""));

        mobile.setText(user_id);
        progressbar.setVisibility(View.VISIBLE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database  = FirebaseDatabase.getInstance();

        usersRef  = database.getReference("Drivers");

        usersRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if (dataSnapshot.hasChild("name")) {

                    name.setText(dataSnapshot.child("name").getValue().toString());
                } else {

                    name.setText(" ");
                }
                if (dataSnapshot.hasChild("alt_number")) {

                    alt_mobile.setText(dataSnapshot.child("alt_number").getValue().toString());
                } else {

                    alt_mobile.setText(" ");
                }
                if (dataSnapshot.hasChild("email")) {

                    email.setText(dataSnapshot.child("email").getValue().toString());
                } else {

                    email.setText(" ");
                }


                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);

            }
        });


        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Account.this, EditProfile.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });
        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Account.this, EditProfile.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });
        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Account.this, EditProfile.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Account.this)
                        .setTitle("Sign Out")
                        .setMessage("are you sure want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Account.super.onBackPressed();

                                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("hasLoggedIn", false);


                                SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = mPrefs.edit();
                                editor1.remove("user_id");

                                editor.commit();
                                editor1.commit();

                                Intent intent = new Intent(Account.this, StartActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).create().show();


            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Account.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_account;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.acount;
    }
}
