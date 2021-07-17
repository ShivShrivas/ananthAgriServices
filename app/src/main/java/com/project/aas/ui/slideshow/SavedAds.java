package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.ui.EditProfile;

import org.jetbrains.annotations.NotNull;

public class SavedAds extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView back;
    TextView backt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_ads);

        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(SavedAds.this, HomePage.class)));
        backt=findViewById(R.id.backkk);
        backt.setOnClickListener(v -> startActivity(new Intent(SavedAds.this, HomePage.class)));


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
}