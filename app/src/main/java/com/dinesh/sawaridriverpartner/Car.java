package com.dinesh.sawaridriverpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Car extends BaseActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String SELECTED_ITEM = "arg_selected_item";
    int mSelectedItem;
    String user_id;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    Button add, add_new_car;
    LinearLayout layout, main_layout;
    ProgressBar progressbar;
    RecyclerView cabs;
    private LinearLayoutManager linearLayoutManager;
    myadapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        add = findViewById(R.id.add);
        add_new_car = findViewById(R.id.add_new_car);
        layout = findViewById(R.id.layout);
        main_layout = findViewById(R.id.main_layout);
        progressbar = findViewById(R.id.progressbar);
        cabs = (RecyclerView) findViewById(R.id.cabs);
        progressbar.setVisibility(View.VISIBLE);
        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        user_id = (shared.getString("user_id", ""));

        database = FirebaseDatabase.getInstance();

        usersRef = database.getReference("Drivers");
        linearLayoutManager = new LinearLayoutManager(this);
        cabs.setLayoutManager(linearLayoutManager);

        usersRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild("Cabs")) {


                    FirebaseRecyclerOptions<CabsModel> options =
                            new FirebaseRecyclerOptions.Builder<CabsModel>()
                                    .setQuery(usersRef.child(user_id).child("Cabs"), CabsModel.class)
                                    .build();


                    adapter = new myadapter(options);
                    cabs.setAdapter(adapter);
                    adapter.startListening();


                } else {

                    progressbar.setVisibility(View.GONE);
                    main_layout.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Car.this, AddCab.class);
                startActivity(intent);

            }
        });

        add_new_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Car.this, AddCab.class);
                startActivity(intent);

            }
        });

    }


    public class myadapter extends FirebaseRecyclerAdapter<CabsModel, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<CabsModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, final int position, @NonNull final CabsModel model) {

            progressbar.setVisibility(View.VISIBLE);
            holder.setIsRecyclable(false);

            if (model.getType().equals("Lite")) {

                Glide.with(getApplicationContext()).load(R.drawable.wagonr).into(holder.image);
            } else if (model.getType().equals("Comfort")) {

                Glide.with(getApplicationContext()).load(R.drawable.dzire).into(holder.image);

            } else {
                Glide.with(getApplicationContext()).load(R.drawable.innova).into(holder.image);

            }


            if (model.getAvailable().equals("yes")) {
                holder.status.setText("Available");
            } else {

                holder.status.setText("Not Available");
                holder.status.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            holder.number.setText(model.getNumber());
            if (model.getAc().equals("yes")) {

                holder.ac.setText("AC");
            } else {
                holder.ac.setText("Non-AC");
            }
            if (model.getType().equals("Lite")) {

                holder.bags.setText("2 bags");
            } else if (model.getType().equals("Comfort")) {
                holder.bags.setText("3 bags");
            } else {
                holder.bags.setText("4 bags");
            }
            holder.seats.setText(model.getSeats() + " seats");
            holder.brand.setText(model.getBrand());
            holder.type.setText(model.getType());



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Car.this, EditCab.class);

                    intent.putExtra("car_number", model.getNumber());
                    intent.putExtra("car_type", model.getType());
                    intent.putExtra("car_brand", model.getBrand());
                    intent.putExtra("car_seats", model.getSeats());
                    intent.putExtra("car_ac", model.getAc());
                    intent.putExtra("car_status", model.getAvailable());

                    startActivity(intent);

                }
            });

            progressbar.setVisibility(View.GONE);
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cabs_model, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView brand, number, ac, bags, seats, type, status;
            ImageView image;


            public myviewholder(@NonNull View itemView) {
                super(itemView);

                brand = (TextView) itemView.findViewById(R.id.brand);
                number = (TextView) itemView.findViewById(R.id.number);
                ac = (TextView) itemView.findViewById(R.id.ac);
                bags = (TextView) itemView.findViewById(R.id.bags);
                seats = (TextView) itemView.findViewById(R.id.seats);
                type = (TextView) itemView.findViewById(R.id.type);
                status = (TextView) itemView.findViewById(R.id.status);
                image = (ImageView) itemView.findViewById(R.id.image);


            }
        }

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_car;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.car;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Car.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}