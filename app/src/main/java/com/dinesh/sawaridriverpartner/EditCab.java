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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditCab extends AppCompatActivity {

    Toolbar toolbar;
    EditText car_type, brand, seats, ac;
    TextView number;
    String car_type_txt, car_brand, seats_txt, ac_txt, number_txt, user_id;
    List<String> listItems;
    Button save, available, not_available;
    DatabaseReference usersRef, mTargetDatabase, mMyCabBooking;
    FirebaseDatabase database;
    ProgressBar progressBar;
    //String str_available = "yes";
    String car_number, intent_type, intent_brand, intent_seats, intent_ac, intent_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cab);

        toolbar = findViewById(R.id.toolbar);
        car_type = findViewById(R.id.car_type);
        brand = findViewById(R.id.brand);
        seats = findViewById(R.id.seats);
        ac = findViewById(R.id.ac);
        save = findViewById(R.id.save);
        number = findViewById(R.id.number);
        available = findViewById(R.id.available);
        not_available = findViewById(R.id.not_available);
        progressBar = findViewById(R.id.progressbar);

        toolbar.setTitle("Car");
        toolbar.setTitleTextColor(Color.BLACK);

        Intent intent = getIntent();
        car_number = intent.getStringExtra("car_number");
        intent_type = intent.getStringExtra("car_type");
        intent_brand = intent.getStringExtra("car_brand");
        intent_seats = intent.getStringExtra("car_seats");
        intent_ac = intent.getStringExtra("car_ac");
        intent_status = intent.getStringExtra("car_status");

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
        mTargetDatabase = database.getReference("DriversCabBookings");
        mMyCabBooking = database.getReference("Drivers").child(user_id).child("BookingRoutes");

        number.setText(car_number);
        car_type.setText(intent_type);
        car_type_txt = intent_type;
        brand.setText(intent_brand);
        car_brand = intent_brand;
        seats.setText(intent_seats);
        seats_txt = intent_seats;
        ac.setText(intent_ac);
        ac_txt = intent_ac;

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

                not_available.setBackgroundColor(ContextCompat.getColor(EditCab.this, R.color.colorPrimaryDark));
                not_available.setTextColor(Color.parseColor("#ffffff"));

                available.setBackgroundResource(android.R.drawable.btn_default);
                available.setTextColor(Color.parseColor("#000000"));


            }
        });

        car_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"Lite", "Comfort", "6 Plus", "6 Pro",};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                builder.setTitle("Select type");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        //car_type.setText(items[item].toString());
                        car_type_txt = items[item].toString();

                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                car_type.setText(car_type_txt);
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

        car_type.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) {

                } else if (s.equals("Comfort")) {

                    brand.setText("");
                    listItems = new ArrayList<String>();
                    listItems.add("DZire");
                    listItems.add("Honda Amaze");
                    listItems.add("Maruti Baleno");

                } else if (s.equals("Lite")) {

                    brand.setText("");
                    listItems.add("Alto");
                    listItems.add("wagnoR");

                } else if (s.equals("6 Plus")) {

                    brand.setText("");

                    listItems.add("Maruti Ertiga");
                    listItems.add("Mahindra Bolero");
                    listItems.add("Mahindra TUV300");
                } else if (s.equals("6 Pro")) {

                    brand.setText("");

                    listItems.add("Toyota Innova");
                    listItems.add("Mahindra Scorpio");
                    listItems.add("Mahindra Scorpio");
                }
            }


            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                brand.setText("");
            }
        });

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(car_type_txt)) {

                    brand.setClickable(false);
                    Toast.makeText(EditCab.this, "Select Car Type!", Toast.LENGTH_SHORT).show();

                } else {

                    if (car_type_txt.equals("Lite")) {

                        //final CharSequence[] items = {"Alto", "WagnoR"};

                        listItems = new ArrayList<String>();

                        listItems.add("Alto");
                        listItems.add("wagnoR");


                    } else if (car_type_txt.equals("Comfort")) {

                        listItems = new ArrayList<String>();
                        listItems.add("DZire");
                        listItems.add("Honda Amaze");
                        listItems.add("Maruti Baleno");

                    } else if (car_type_txt.equals("6 Plus")) {

                        listItems = new ArrayList<String>();
                        listItems.add("Maruti Ertiga");
                        listItems.add("Mahindra Bolero");
                        listItems.add("Mahindra TUV300");

                    } else if (car_type_txt.equals("6 Pro")) {

                        listItems = new ArrayList<String>();
                        listItems.add("Toyota Innova");
                        listItems.add("Mahindra Scorpio");
                        listItems.add("Mahindra Scorpio");

                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                    builder.setTitle("Select City");
                    builder.setCancelable(false);
                    builder.setSingleChoiceItems(listItems.toArray(new String[listItems.size()]), -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                            car_brand = listItems.get(item).toString();

                        }
                    });

                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    brand.setText(car_brand);
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


            }
        });

        seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"1", "2", "3", "4", "5", "6", "7", "8"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                builder.setTitle("Select Seats");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        seats_txt = items[item].toString();


                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                seats.setText(seats_txt);
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

        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"yes", "no"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                builder.setTitle("Select");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        ac_txt = items[item].toString();


                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ac.setText(ac_txt);
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

                progressBar.setVisibility(View.VISIBLE);

                number_txt = number.getText().toString();


                if (TextUtils.isEmpty(number_txt)) {

                    number.requestFocus();
                    number.setError("Enter Car Number");
                    progressBar.setVisibility(View.GONE);
                    return;

                } else if (TextUtils.isEmpty(car_type_txt)) {

                    car_type.requestFocus();
                    car_type.setError("Enter Car Type");
                    progressBar.setVisibility(View.GONE);
                    return;


                } else if (TextUtils.isEmpty(car_brand)) {

                    brand.requestFocus();
                    brand.setError("Enter Car Brand");
                    progressBar.setVisibility(View.GONE);
                    return;


                } else if (TextUtils.isEmpty(seats_txt)) {

                    seats.requestFocus();
                    seats.setError("Enter Car Brand");
                    progressBar.setVisibility(View.GONE);
                    return;


                } else if (TextUtils.isEmpty(ac_txt)) {

                    ac.requestFocus();
                    ac.setError("Enter Car Brand");
                    progressBar.setVisibility(View.GONE);
                    return;


                } else {

                    if (intent_status.equals("yes")) {

                        HashMap<String, Object> taskMap = new HashMap<>();
                        taskMap.put("number", number_txt);
                        taskMap.put("type", car_type_txt);
                        taskMap.put("brand", car_brand);
                        taskMap.put("seats", seats_txt);
                        taskMap.put("ac", ac_txt);
                        taskMap.put("available", intent_status);

                        usersRef.child(user_id).child("Cabs").child(number_txt).updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    mMyCabBooking.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot datas : dataSnapshot.getChildren()) {

                                                if (datas.child("number").getValue().equals(number_txt)) {

                                                    mMyCabBooking.child(datas.getKey()).child("available").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                progressBar.setVisibility(View.GONE);
                                                                finish();

                                                            } else {

                                                                progressBar.setVisibility(View.GONE);
                                                                finish();
                                                            }

                                                        }
                                                    });

                                                } else {


                                                    HashMap<String, Object> taskMap = new HashMap<>();
                                                    taskMap.put("number", number_txt);
                                                    taskMap.put("type", car_type_txt);
                                                    taskMap.put("brand", car_brand);
                                                    taskMap.put("seats", seats_txt);
                                                    taskMap.put("ac", ac_txt);
                                                    taskMap.put("available", intent_status);

                                                    usersRef.child(user_id).child("Cabs").child(number_txt).updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                progressBar.setVisibility(View.GONE);

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                                                                builder
                                                                        .setTitle("Saved.")
                                                                        .setIcon(R.drawable.checked)
                                                                        .setCancelable(false)
                                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                Intent intent = new Intent(EditCab.this, Car.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        });
                                                                AlertDialog dialog = builder.create();
                                                                dialog.show();

                                                            } else {

                                                                progressBar.setVisibility(View.GONE);
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                                                                builder
                                                                        .setTitle("Something is wrong. Try Again")
                                                                        .setIcon(R.drawable.cross)
                                                                        .setPositiveButton(android.R.string.ok, null);
                                                                AlertDialog dialog = builder.create();
                                                                dialog.show();

                                                            }

                                                        }
                                                    });


                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });




                                    /*progressBar.setVisibility(View.GONE);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                                    builder
                                            .setTitle("Saved.")
                                            .setIcon(R.drawable.checked)
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(EditCab.this, Car.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();*/

                                } else {

                                    progressBar.setVisibility(View.GONE);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                                    builder
                                            .setTitle("Something is wrong. Try Again")
                                            .setIcon(R.drawable.cross)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                }

                            }
                        });

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                        builder
                                .setTitle("Warning")
                                .setMessage("This will Delete all bookings by this car")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                })
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        mMyCabBooking.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot datas : dataSnapshot.getChildren()) {

                                                    if (datas.child("number").getValue().equals(number_txt)) {

                                                        mMyCabBooking.child(datas.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {

                                                                    String route = datas.child("route").getValue().toString()+" "+user_id+" "+datas.child("number").getValue().toString();
                                                                    mTargetDatabase.child(datas.child("route").getValue().toString()).child(route).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            progressBar.setVisibility(View.GONE);
                                                                            finish();
                                                                            Toast.makeText(EditCab.this, "Successful.", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });



                                                                } else {

                                                                    progressBar.setVisibility(View.GONE);
                                                                    finish();
                                                                }

                                                            }
                                                        });

                                                    } else {


                                                        HashMap<String, Object> taskMap = new HashMap<>();
                                                        taskMap.put("number", number_txt);
                                                        taskMap.put("type", car_type_txt);
                                                        taskMap.put("brand", car_brand);
                                                        taskMap.put("seats", seats_txt);
                                                        taskMap.put("ac", ac_txt);
                                                        taskMap.put("available", intent_status);

                                                        usersRef.child(user_id).child("Cabs").child(number_txt).updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {

                                                                    progressBar.setVisibility(View.GONE);

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                                                                    builder
                                                                            .setTitle("Saved.")
                                                                            .setIcon(R.drawable.checked)
                                                                            .setCancelable(false)
                                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    Intent intent = new Intent(EditCab.this, Car.class);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                }
                                                                            });
                                                                    AlertDialog dialog = builder.create();
                                                                    dialog.show();

                                                                } else {

                                                                    progressBar.setVisibility(View.GONE);
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCab.this);
                                                                    builder
                                                                            .setTitle("Something is wrong. Try Again")
                                                                            .setIcon(R.drawable.cross)
                                                                            .setPositiveButton(android.R.string.ok, null);
                                                                    AlertDialog dialog = builder.create();
                                                                    dialog.show();

                                                                }

                                                            }
                                                        });


                                                    }

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        progressBar.setVisibility(View.GONE);
                    }


                }


            }
        });
    }
}