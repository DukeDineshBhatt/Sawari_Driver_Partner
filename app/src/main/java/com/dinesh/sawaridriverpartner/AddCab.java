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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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


public class AddCab extends AppCompatActivity {

    Toolbar toolbar;
    EditText car_type, brand, seats, ac, number;
    String car_type_txt, car_brand, seats_txt, ac_txt, number_txt, user_id;
    List<String> listItems;
    Button save;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    ProgressBar progressBar;
    boolean alrdyCab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cab);

        toolbar = findViewById(R.id.toolbar);
        car_type = findViewById(R.id.car_type);
        brand = findViewById(R.id.brand);
        seats = findViewById(R.id.seats);
        ac = findViewById(R.id.ac);
        save = findViewById(R.id.save);
        number = findViewById(R.id.number);
        progressBar = findViewById(R.id.progressbar);

        toolbar.setTitle("Add Car");
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


        database = FirebaseDatabase.getInstance();

        usersRef = database.getReference("Drivers");


        car_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"Lite", "Comfort", "6 Plus", "6 Pro",};

                AlertDialog.Builder builder = new AlertDialog.Builder(AddCab.this);
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
                    Toast.makeText(AddCab.this, "Select Car Type!", Toast.LENGTH_SHORT).show();

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


                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCab.this);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(AddCab.this);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(AddCab.this);
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


                    HashMap<String, Object> taskMap = new HashMap<>();
                    taskMap.put("number", number_txt);
                    taskMap.put("type", car_type_txt);
                    taskMap.put("brand", car_brand);
                    taskMap.put("seats", seats_txt);
                    taskMap.put("ac", ac_txt);
                    taskMap.put("available", "yes");

                    usersRef.child(user_id).child("Cabs").child(number_txt).updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                //Toast.makeText(AddCab.this, "Car Added Successfully.", Toast.LENGTH_LONG).show();

                                progressBar.setVisibility(View.GONE);
                                number.setText("");
                                car_type.setText("");
                                brand.setText("");
                                seats.setText("");
                                ac.setText("");

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddCab.this);
                                builder
                                        .setTitle("Car Added Successfully")
                                        .setIcon(R.drawable.checked)
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(AddCab.this, Car.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {

                                progressBar.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddCab.this);
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
        });
    }
}