package com.project.aas.ui.slideshow;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.adapter.MyPostsAdapter;
import com.project.aas.model.AdPost;
import com.project.aas.ui.EditProfile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    ImageView back;
    TextView backk;
    BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;
    MyPostsAdapter myPostsAdapter;
    List<AdPost> MyAdsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(MyOrders.this, HomePage.class)));
        backk=findViewById(R.id.backkk);
        backk.setOnClickListener(v -> startActivity(new Intent(MyOrders.this, HomePage.class)));

        recyclerView=findViewById(R.id.myAdsRecycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager adsLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(adsLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        MyAdsList = new ArrayList<>();
        myPostsAdapter = new MyPostsAdapter(MyAdsList,this);
        recyclerView.setAdapter(myPostsAdapter);
        myAds();

        bottomNavigationView=findViewById(R.id.bottomView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.orders);
      //  bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
                if(id==R.id.homePage) {
                    startActivity(new Intent(MyOrders.this, HomePage.class));
                }
               if(id==R.id.edit_profilebo){
                    startActivity(new Intent(MyOrders.this,EditProfile.class));
            }
            if(id==R.id.savedAds){
                startActivity(new Intent(MyOrders.this, SavedAds.class));
            }
            return false;
        });
    }

    private void myAds(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                MyAdsList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    AdPost adPost = snapshot1.getValue(AdPost.class);
                    if(adPost.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
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