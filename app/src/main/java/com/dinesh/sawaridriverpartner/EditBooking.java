package com.dinesh.sawaridriverpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditBooking extends AppCompatActivity {
    Toolbar toolbar;
    String car_number, intent_to, intent_from, intent_status, user_id, intent_price, number_txt, type, ac, brand, seats, name;
    TextView number, from, to;
    EditText price;
    DatabaseReference usersRef, mDriverCarDatabase, mTargetDatabase, mMyCabBooking;
    FirebaseDatabase database;
    ProgressBar progressBar;
    String str_cab_on = "yes";
    Button save, available, not_available;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        toolbar = findViewById(R.id.toolbar);
        available = findViewById(R.id.available);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        number = findViewById(R.id.number);
        not_available = findViewById(R.id.not_available);
        price = findViewById(R.id.price);
        progressBar = findViewById(R.id.progressbar);
        save = findViewById(R.id.save);

        toolbar.setTitle("Booking");
        toolbar.setTitleTextColor(Color.BLACK);

        Intent intent = getIntent();
        car_number = intent.getStringExtra("car_number");
        intent_status = intent.getStringExtra("status");
        intent_price = intent.getStringExtra("price");
        intent_from = intent.getStringExtra("from");
        intent_to = intent.getStringExtra("to");

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        user_id = (shared.getString("user_id", ""));

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        database = FirebaseDatabase.getInstance();

        usersRef = database.getReference("Drivers");
        mDriverCarDatabase = database.getReference("Drivers").child(user_id).child("Cabs");
        mTargetDatabase = database.getReference("DriversCabBookings");
        mMyCabBooking = database.getReference("Drivers").child(user_id).child("BookingRoutes");

        number.setText(car_number);
        price.setText(intent_price);
        from.setText(intent_from);
        to.setText(intent_to);


        if (intent_status.equals("yes")) {

            available.setBackgroundColor(Color.parseColor("#228B22"));
            available.setTextColor(Color.parseColor("#ffffff"));

        } else {

            available.setBackgroundResource(android.R.drawable.btn_default);

            not_available.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            not_available.setTextColor(Color.parseColor("#ffffff"));
        }

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent_status = "yes";

                available.setBackgroundColor(Color.parseColor("#228B22"));
                available.setTextColor(Color.parseColor("#ffffff"));

                not_available.setBackgroundResource(android.R.drawable.btn_default);
                not_available.setTextColor(Color.parseColor("#000000"));


            }
        });

        not_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent_status = "no";

                not_available.setBackgroundColor(ContextCompat.getColor(EditBooking.this, R.color.colorPrimaryDark));
                not_available.setTextColor(Color.parseColor("#ffffff"));

                available.setBackgroundResource(android.R.drawable.btn_default);
                available.setTextColor(Color.parseColor("#000000"));


            }
        });

        mDriverCarDatabase.child(car_number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                type = dataSnapshot.child("type").getValue().toString();
                brand = dataSnapshot.child("brand").getValue().toString();
                seats = dataSnapshot.child("seats").getValue().toString();
                ac = dataSnapshot.child("ac").getValue().toString();
                name = dataSnapshot.child("brand").getValue().toString();
                str_cab_on = dataSnapshot.child("available").getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                number_txt = number.getText().toString();

                if (TextUtils.isEmpty(number_txt)) {

                    number.requestFocus();
                    number.setError("Enter Car Number");
                    progressBar.setVisibility(View.GONE);
                    return;

                } else if (str_cab_on.equals("no")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditBooking.this);
                    builder.setTitle("Message")
                            .setMessage("This Car is OFF right now. Please ON this car, to add booking for this car.")
                            .setIcon(R.drawable.cross)
                            .setCancelable(false)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {

                    if (intent_status.equals("no")) {

                        progressBar.setVisibility(View.VISIBLE);

                        Map userMap = new HashMap();
                        userMap.put("price", price.getText().toString());
                        userMap.put("ac", ac);
                        userMap.put("name", name);
                        userMap.put("seats", seats);
                        userMap.put("type", type);

                        Map userMap1 = new HashMap();
                        userMap1.put("price", price.getText().toString());
                        userMap1.put("name", name);
                        userMap1.put("number", car_number);
                        userMap1.put("available", intent_status);
                        userMap1.put("route", intent_from + " - " + intent_to);

                        mTargetDatabase.child(intent_from + " - " + intent_to).child(intent_from + " - " + intent_to + " " + user_id + " " + car_number).removeValue().addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            mMyCabBooking.child(intent_from + " - " + intent_to + " " + user_id + " " + car_number).setValue(userMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        progressBar.setVisibility(View.GONE);
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditBooking.this);
                                                        builder.setTitle("Message")
                                                                .setMessage("Successfully done.")
                                                                .setIcon(R.drawable.checked)
                                                                .setCancelable(false)
                                                                .setPositiveButton("Ok",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {

                                                                                finish();
                                                                            }
                                                                        });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    } else {

                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });


                                        } else {

                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }
                        );


                    } else {

                        progressBar.setVisibility(View.VISIBLE);

                        Map userMap = new HashMap();
                        userMap.put("price", price.getText().toString());
                        userMap.put("ac", ac);
                        userMap.put("name", name);
                        userMap.put("seats", seats);
                        userMap.put("type", type);

                        Map userMap1 = new HashMap();
                        userMap1.put("price", price.getText().toString());
                        userMap1.put("name", name);
                        userMap1.put("number", car_number);
                        userMap1.put("available", intent_status);
                        userMap1.put("route", intent_from + " - " + intent_to);

                        mTargetDatabase.child(intent_from + " - " + intent_to).child(intent_from + " - " + intent_to + " " + user_id + " " + car_number).setValue(userMap).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            mMyCabBooking.child(intent_from + " - " + intent_to + " " + user_id + " " + car_number).setValue(userMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        progressBar.setVisibility(View.GONE);
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditBooking.this);
                                                        builder.setTitle("Message")
                                                                .setMessage("Successfully done.")
                                                                .setIcon(R.drawable.checked)
                                                                .setCancelable(false)
                                                                .setPositiveButton("Ok",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {

                                                                                finish();
                                                                            }
                                                                        });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    } else {

                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });


                                        } else {

                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }
                        );


                    }


                }


            }
        });
    }
}