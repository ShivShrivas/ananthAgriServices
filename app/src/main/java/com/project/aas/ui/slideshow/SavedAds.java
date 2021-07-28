package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.adapter.AdRecyclerViewAdapter;
import com.project.aas.model.AdPost;
import com.project.aas.ui.AddAds;
import com.project.aas.ui.EditProfile;
import com.project.aas.ui.Fertilizers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SavedAds extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView back;
    TextView backt;
    AdRecyclerViewAdapter saveAdapter;
    FloatingActionButton floatingActionButton;

    private List<String> savedAds;
    RecyclerView adsRecyclerView;
    List<AdPost> adsSaveList;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_ads);

        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(SavedAds.this, HomePage.class)));
        backt=findViewById(R.id.backkk);
        backt.setOnClickListener(v -> startActivity(new Intent(SavedAds.this, HomePage.class)));

        floatingActionButton = findViewById(R.id.add_ads);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedAds.this, AddAds.class);
                startActivity(intent);
            }
        });


        bottomNavigationView=findViewById(R.id.bottomView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.savedAds);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.homePage) {
                    startActivity(new Intent(SavedAds.this, HomePage.class));
                }
                if(id==R.id.edit_profilebo){
                    startActivity(new Intent(SavedAds.this, EditProfile.class));
                }
                if(id==R.id.orders){
                    startActivity(new Intent(SavedAds.this, MyOrders.class));
                }
                return false;
            }
        });

    }

    private void mySaves(){
        savedAds=new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("saved")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    savedAds.add(snapshot1.getKey());
                }
                readSaved();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void readSaved(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Ads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                savedAds.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    AdPost post = snapshot1.getValue(AdPost.class);

                    for(String id : savedAds){
                        if(post.getId().equals(id)){
                            adsSaveList.add(post);
                        }
                    }
                }saveAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}