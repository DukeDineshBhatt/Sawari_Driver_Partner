package com.dinesh.sawaridriverpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBooking extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    String user_id;
    DatabaseReference mLocationsDatabase, mDriverCarDatabase, mTargetDatabase, mMyCabBooking;
    List<String> listItems, listItems1;
    EditText to, from, car, price;
    Button save;
    String from_city, to_city, str_car, type, ac, brand, seats, name;
    String str_cab_on = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking);

        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressbar);
        to = findViewById(R.id.to);
        from = findViewById(R.id.from);
        car = findViewById(R.id.car);
        save = findViewById(R.id.save);
        price = findViewById(R.id.price);

        listItems = new ArrayList<String>();
        listItems1 = new ArrayList<String>();

        toolbar.setTitle("Add Booking");
        toolbar.setTitleTextColor(Color.BLACK);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        user_id = (shared.getString("user_id", ""));

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mLocationsDatabase = database.getReference("Locations");
        mDriverCarDatabase = database.getReference("Drivers").child(user_id).child("Cabs");
        mTargetDatabase = database.getReference("DriversCabBookings");
        mMyCabBooking = database.getReference("Drivers").child(user_id).child("BookingRoutes");

        mLocationsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    listItems.add(postSnapshot.child("Name").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDriverCarDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    listItems1.add(postSnapshot.child("number").getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //final CharSequence[] items = {"Pithoragarh", "Haldwani", "Nainital", "Delhi",};

                AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                builder.setTitle("Select City");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(listItems.toArray(new String[listItems.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        from_city = listItems.get(item).toString();

                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                from.setText(from_city);
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                builder.setTitle("Select City");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(listItems.toArray(new String[listItems.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items1[item], Toast.LENGTH_SHORT).show();
                        to_city = listItems.get(item);

                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                to.setText(to_city);
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                builder.setTitle("Select Car");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(listItems1.toArray(new String[listItems1.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items1[item], Toast.LENGTH_SHORT).show();
                        str_car = listItems1.get(item);

                        mDriverCarDatabase.child(str_car).addValueEventListener(new ValueEventListener() {
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

                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                car.setText(str_car);
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (from.getText().toString().trim().isEmpty()) {

                    Toast.makeText(AddBooking.this, "Select Pickup location", Toast.LENGTH_SHORT).show();

                } else if (to.getText().toString().trim().isEmpty()) {

                    Toast.makeText(AddBooking.this, "Select Drop location", Toast.LENGTH_SHORT).show();

                } else if (car.getText().toString().trim().isEmpty()) {

                    Toast.makeText(AddBooking.this, "Select Car", Toast.LENGTH_SHORT).show();

                } else if (price.getText().toString().trim().isEmpty()) {

                    Toast.makeText(AddBooking.this, "Enter Price", Toast.LENGTH_SHORT).show();

                } else if (from.getText().toString().equals(to.getText().toString())) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                    builder.setTitle("Message")
                            .setMessage("Pickup and Drop location cannot be same city.")
                            .setIcon(R.drawable.cross)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else if (str_cab_on.equals("no")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                    builder.setTitle("Message")
                            .setMessage("This Car is OFF right now. Please ON this car, to add booking for this car.")
                            .setIcon(R.drawable.cross)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    progressBar.setVisibility(View.GONE);
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
                    userMap1.put("number", str_car);
                    userMap1.put("available", "yes");
                    userMap1.put("route", from_city + " - " + to_city);

                    mTargetDatabase.child(from_city + " - " + to_city).child(from_city + " - " + to_city + " " + user_id + " " + str_car).setValue(userMap).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mMyCabBooking.child(from_city + " - " + to_city + " " + user_id + " " + str_car).setValue(userMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    progressBar.setVisibility(View.GONE);
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                                                    builder.setTitle("Message")
                                                            .setMessage("Successfully added.")
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

                    /*mMyCabBooking.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot datas : dataSnapshot.getChildren()) {

                                String route = datas.child("route").getValue().toString();
                                String number = datas.child("number").getValue().toString();

                                if (route.equals(from_city + " - " + to_city) && number.equals(str_car)) {

                                    progressBar.setVisibility(View.GONE);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                                    builder.setTitle("Oops")
                                            .setMessage("This Booking Is Already added.")
                                            .setIcon(R.drawable.cross)
                                            .setCancelable(false)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    return;
                                } else {

                                    mTargetDatabase.child(from_city + " - " + to_city).child(str_car).setValue(userMap).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        mMyCabBooking.push().setValue(userMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {

                                                                    progressBar.setVisibility(View.GONE);
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBooking.this);
                                                                    builder.setTitle("Message")
                                                                            .setMessage("Successfully added.")
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


                }

            }
        });

    }


}