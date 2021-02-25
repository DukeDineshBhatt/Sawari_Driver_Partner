package com.dinesh.sawaridriverpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingDetails extends AppCompatActivity {

    String car_numbertxt, user_id, statustxt, booking_details, user_info;
    String fromtxt, totxt, datetxt, timetxt, routeString, typetxt, nametxt, pricetxt, seatstxt, bagstxt, actxt, driver_token;
    ImageView image, call_us;
    TextView date, time, from, to, type, name, price, seats, bags, ac, car_number, status;
    DatabaseReference usersRef, mMyBookingDatabase, mUserBookingDatabase;
    FirebaseDatabase database;
    Toolbar toolbar;
    Button btn_accept, btn_decline;
    LinearLayout accept, decline, btn_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Booking Details");

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        user_id = (shared.getString("user_id", ""));

        Intent intent = getIntent();
        //driver_token = intent.getStringExtra("driver_token");
        datetxt = intent.getStringExtra("date");
        timetxt = intent.getStringExtra("time");
        typetxt = intent.getStringExtra("type");
        nametxt = intent.getStringExtra("name");
        pricetxt = intent.getStringExtra("price");
        seatstxt = intent.getStringExtra("seats");
        bagstxt = intent.getStringExtra("bags");
        actxt = intent.getStringExtra("ac");
        fromtxt = intent.getStringExtra("from");
        totxt = intent.getStringExtra("to");
        car_numbertxt = intent.getStringExtra("car_number");
        //statustxt = intent.getStringExtra("status");
        booking_details = intent.getStringExtra("booking_details");

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        image = findViewById(R.id.image);

        date = findViewById(R.id.datetxt);
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        car_number = findViewById(R.id.car_number);
        time = findViewById(R.id.time);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        type = findViewById(R.id.type);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        seats = findViewById(R.id.seats);
        bags = findViewById(R.id.bags);
        ac = findViewById(R.id.ac);
        btn_accept = findViewById(R.id.btn_accept);
        btn_decline = findViewById(R.id.btn_decline);
        status = findViewById(R.id.status);
        btn_layout = findViewById(R.id.btn_layout);

        String str = booking_details;
        str = str.replaceAll("[^a-zA-Z0-9]", " ");

        String[] splitStr = str.split("\\s+");

        user_info = splitStr[2];

        mMyBookingDatabase = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user_id).child("BookingRequest");
        mUserBookingDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_info).child("CabBooking");

        mMyBookingDatabase.child(booking_details).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                statustxt = dataSnapshot.child("status").getValue().toString();

                if (statustxt.equals("pending")) {

                    status.setText("This booking is Pending");
                    status.setTextColor(Color.parseColor("#AB192D"));
                    btn_layout.setVisibility(View.VISIBLE);

                } else if (statustxt.equals("accept")) {

                    status.setText("This booking is Accepted");
                    status.setTextColor(Color.parseColor("#228B22"));
                    btn_layout.setVisibility(View.GONE);

                } else if (statustxt.equals("decline")) {

                    status.setText("This booking is Cancel");
                    status.setTextColor(Color.parseColor("#C0C0C0"));
                    btn_layout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        date.setText(datetxt);
        time.setText(timetxt);
        from.setText(fromtxt);
        to.setText(totxt);
        type.setText(typetxt);
        name.setText(nametxt + " or Equivalent");
        price.setText(pricetxt);
        car_number.setText(car_numbertxt);
        seats.setText(seatstxt + " seats");

        if (typetxt.equals("Lite")) {
            bags.setText("2 Bags");
        } else if (typetxt.equals("comfort")) {
            bags.setText("3 Bags");
        } else if (typetxt.equals("comfort")) {
            bags.setText("4 Bags");
        } else if (typetxt.equals("6 Plus")) {
            bags.setText("4 Bags");
        } else if (typetxt.equals("6 Pro")) {
            bags.setText("4 Bags");
        }

        if (actxt.equals("yes")) {
            ac.setText("AC");

        } else {

            ac.setText("Non-Ac");

        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(BookingDetails.this);
                builder.setTitle("Booking Confirm ?")
                        .setMessage("Are You sure ?")
                        .setIcon(R.drawable.checked)
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                })
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        mMyBookingDatabase.child(fromtxt + " - " + totxt + " " + user_id + " " + car_numbertxt).child("status").setValue("accept").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    mUserBookingDatabase.child(booking_details).child("status").setValue("accept").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Toast.makeText(BookingDetails.this, "Successful", Toast.LENGTH_SHORT).show();


                                                        }
                                                    });

                                                } else {
                                                    Toast.makeText(BookingDetails.this, "Something wrong, Try again.", Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });

                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(BookingDetails.this);
                builder.setTitle("Booking Confirm ?")
                        .setMessage("Are You sure ?")
                        .setIcon(R.drawable.checked)
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BookingDetails.this);
                builder.setTitle("Cancel Booking ?")
                        .setMessage("Are You sure ?")
                        .setIcon(R.drawable.cross)
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BookingDetails.this);
                builder.setTitle("Cancel Booking ?")
                        .setMessage("Are You sure ?")
                        .setIcon(R.drawable.cross)
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }
}