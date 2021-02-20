package com.dinesh.sawaridriverpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends BaseActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String SELECTED_ITEM = "arg_selected_item";
    int mSelectedItem;
    String user_id, token;
    LinearLayout layout;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    Button add_car, add_new_booking;
    ProgressBar progressbar;
    TextView car;
    RecyclerView booking_route;
    LinearLayout main_layout;
    private LinearLayoutManager linearLayoutManager;
    myadapter adapter, adapter1;
    FirebaseStorage storage;
    String txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.commit();

        layout = findViewById(R.id.layout);
        add_car = findViewById(R.id.add_car);
        main_layout = findViewById(R.id.main_layout);
        add_new_booking = findViewById(R.id.add_new_booking);
        car = findViewById(R.id.car);
        progressbar = findViewById(R.id.progressbar);
        booking_route = (RecyclerView) findViewById(R.id.booking_route);
        progressbar.setVisibility(View.VISIBLE);


        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        user_id = (shared.getString("user_id", ""));
        token = (shared.getString("token", ""));

        database = FirebaseDatabase.getInstance();

        usersRef = database.getReference("Drivers");
        linearLayoutManager = new LinearLayoutManager(this);
        booking_route.setLayoutManager(linearLayoutManager);

        Log.d("TOKEN", token);

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Car.class);
                {
                    startActivity(intent);

                }

            }
        });

        add_new_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddBooking.class);
                startActivity(intent);


            }
        });

        usersRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild("Cabs")) {

                    progressbar.setVisibility(View.GONE);


                } else {

                    progressbar.setVisibility(View.GONE);
                    main_layout.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);

                }


                if (dataSnapshot.hasChild("BookingRoutes")) {

                    usersRef.child(user_id).child("BookingRoutes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            txt = dataSnapshot.getKey().toString();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    FirebaseRecyclerOptions<BookingRouteModel> options =
                            new FirebaseRecyclerOptions.Builder<BookingRouteModel>()
                                    .setQuery(usersRef.child(user_id).child("BookingRoutes"), BookingRouteModel.class)
                                    .build();


                    adapter = new myadapter(options);
                    booking_route.setAdapter(adapter);
                    adapter.startListening();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
            }
        });

        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, AddCab.class);
                startActivity(intent);

            }
        });

    }


    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.homee;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        finishAffinity();


    }

    public class myadapter extends FirebaseRecyclerAdapter<BookingRouteModel, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<BookingRouteModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, final int position, @NonNull final BookingRouteModel model) {

            progressbar.setVisibility(View.VISIBLE);
            holder.setIsRecyclable(false);


            String price = model.getPrice().toString();
            holder.price.setText(price);

            String number = model.getNumber().toString();
            holder.number.setText(number);

            String name = model.getName().toString();
            holder.name.setText(name);

            String available = model.getAvailable().toString();

            if (available.equals("yes")) {

                holder.status.setText("AVAILABLE");
            } else {

                holder.status.setText("NOT AVAILABLE");
                holder.status.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            String str = model.getRoute();
            str = str.replaceAll("[^a-zA-Z0-9]", " ");

            String[] splitStr = str.split("\\s+");

            holder.from.setText(splitStr[0]);
            holder.to.setText(splitStr[1]);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, EditBooking.class);


                    intent.putExtra("from", splitStr[0]);
                    intent.putExtra("to", splitStr[1]);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("car_number", model.getNumber());
                    intent.putExtra("price", String.valueOf(model.getPrice()));
                    intent.putExtra("status", model.getAvailable());
                    startActivity(intent);

                }
            });

            progressbar.setVisibility(View.GONE);
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_booking_route, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView from, to, price, number, status, name;
            ImageView image;


            public myviewholder(@NonNull View itemView) {
                super(itemView);

                from = (TextView) itemView.findViewById(R.id.from);
                to = (TextView) itemView.findViewById(R.id.to);
                price = (TextView) itemView.findViewById(R.id.price);
                number = (TextView) itemView.findViewById(R.id.number);
                status = (TextView) itemView.findViewById(R.id.status);
                name = (TextView) itemView.findViewById(R.id.name);

                // image = (ImageView) itemView.findViewById(R.id.image);


            }
        }

    }

}