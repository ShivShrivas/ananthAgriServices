package com.project.aas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.adapter.MyPostsAdapter;
import com.project.aas.model.AdPost;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tractor extends AppCompatActivity {

    RecyclerView recyclerView;
    MyPostsAdapter myPostsAdapter;
    List<AdPost> MyAdsList;

    ImageView back;
    TextView backk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tractor);

        back = findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(Tractor.this, HomePage.class)));
        backk = findViewById(R.id.backkk);
        backk.setOnClickListener(v -> startActivity(new Intent(Tractor.this, HomePage.class)));

        recyclerView = findViewById(R.id.recyclerviewTractor);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager adsLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(adsLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        MyAdsList = new ArrayList<>();
        myPostsAdapter = new MyPostsAdapter(MyAdsList, this);
        recyclerView.setAdapter(myPostsAdapter);
        myTractorAds();

    }

    private void myTractorAds() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                MyAdsList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    AdPost adPost = snapshot1.getValue(AdPost.class);
                    if (adPost.getCategory().equals("Farm machinery and Tools")) {
                        MyAdsList.add(adPost);
                    }
                    Collections.reverse(MyAdsList);
                    myPostsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}