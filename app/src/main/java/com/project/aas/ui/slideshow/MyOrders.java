package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.adapter.AdRecyclerViewAdapter;
import com.project.aas.adapter.MyPostsAdapter;
import com.project.aas.model.AdPost;
import com.project.aas.model.MyAdsPost;
import com.project.aas.model.UserProfile;
import com.project.aas.ui.EditProfile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MyOrders extends AppCompatActivity {

    ImageView back;
    TextView backk;
    BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;
    MyPostsAdapter myPostsAdapter;
    List<MyAdsPost> MyAdsList;
    String fUser;
    FirebaseDatabase databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(MyOrders.this, HomePage.class)));
        backk=findViewById(R.id.backkk);
        backk.setOnClickListener(v -> startActivity(new Intent(MyOrders.this, HomePage.class)));

       recyclerview();

       fUser= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

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

    private void recyclerview() {
        recyclerView = findViewById(R.id.myAdsRecycler);
        RecyclerView.LayoutManager myAdsLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(myAdsLayoutManager);
        MyPostsAdapter adapter=new MyPostsAdapter(MyAdsList,this);
        recyclerView.setAdapter(adapter);
        myAds();
    }

    private void myAds(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                MyAdsPost myAds = snapshot.getValue(MyAdsPost.class);
                assert myAds != null;
                databaseReference.getReference("Ads").child(myAds.getPostedBy()).get()
                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (myAds.getId().equals(fUser)) {
                                        MyAdsList.add(myAds);
                                    }
                                    Collections.reverse(MyAdsList);
                                    myPostsAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}