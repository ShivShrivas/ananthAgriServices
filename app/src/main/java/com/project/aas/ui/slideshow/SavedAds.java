package com.project.aas.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.adapter.SavedAdsAdapter;
import com.project.aas.model.AdPost;
import com.project.aas.model.SavedAdsModel;
import com.project.aas.ui.AddAds;
import com.project.aas.ui.EditProfile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SavedAds extends AppCompatActivity {

    public static ProgressDialog progressDialog;
    BottomNavigationView bottomNavigationView;
    ImageView back, searchh;
    TextView backt, searchhh, no;
    FloatingActionButton floatingActionButton;
    RecyclerView savedAdsRecyclerView;
    List<SavedAdsModel> savedAdsList;
    FirebaseUser firebaseUser;
    SavedAdsAdapter savedAdsAdapter;
    private AdPost mAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_ads);

        searchh = findViewById(R.id.searchh);
        searchhh = findViewById(R.id.searchhh);
        no = findViewById(R.id.no);

        back = findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(SavedAds.this, HomePage.class)));
        backt = findViewById(R.id.backkk);
        backt.setOnClickListener(v -> startActivity(new Intent(SavedAds.this, HomePage.class)));

        floatingActionButton = findViewById(R.id.add_ads);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedAds.this, AddAds.class);
                startActivity(intent);
            }
        });

        savedAdsRecyclerView = findViewById(R.id.savedAdsRecycler);
        savedAdsRecyclerView.setHasFixedSize(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("data fetching...");
        progressDialog.show();


        RecyclerView.LayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        FirebaseRecyclerOptions<AdPost> options = new FirebaseRecyclerOptions.Builder<AdPost>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saved"), AdPost.class)
                .build();

        savedAdsAdapter = new SavedAdsAdapter(SavedAds.this, options);
        savedAdsRecyclerView.setLayoutManager(linearLayoutManager);
        savedAdsRecyclerView.setAdapter(savedAdsAdapter);
        savedAdsAdapter.notifyDataSetChanged();
//            savedAdsRecyclerView.setNestedScrollingEnabled(false);
//        savedAdsAdapter = new SavedAdsAdapter(SavedAds.this,options);
//        savedAdsRecyclerView.setAdapter(savedAdsAdapter);
        savedAdsOfUser();
//
        bottomNavigationView = findViewById(R.id.bottomView);
        //bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.savedAds);
//        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.homePage) {
                    startActivity(new Intent(SavedAds.this, HomePage.class));
                }
                if (id == R.id.edit_profilebo) {
                    startActivity(new Intent(SavedAds.this, EditProfile.class));
                }
                if (id == R.id.orders) {
                    startActivity(new Intent(SavedAds.this, MyOrders.class));
                }
                return false;
            }
        });

    }


    private void savedAdsOfUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child("saved");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                savedAdsList.clear();

                if (snapshot.exists()) {
                    no.setVisibility(View.GONE);
                    searchh.setVisibility(View.GONE);
                    searchhh.setVisibility(View.GONE);
                    savedAdsAdapter.notifyDataSetChanged();
                } else {
                    no.setVisibility(View.VISIBLE);
                    searchh.setVisibility(View.VISIBLE);
                    searchhh.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (savedAdsAdapter != null) {
            savedAdsAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (savedAdsAdapter != null) {
            savedAdsAdapter.stopListening();
        }

    }
}