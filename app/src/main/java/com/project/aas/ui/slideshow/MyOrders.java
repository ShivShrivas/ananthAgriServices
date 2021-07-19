package com.project.aas.ui.slideshow;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.ui.EditProfile;

public class MyOrders extends AppCompatActivity {

    ImageView back;
    TextView backk;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(MyOrders.this, HomePage.class)));
        backk=findViewById(R.id.backkk);
        backk.setOnClickListener(v -> startActivity(new Intent(MyOrders.this, HomePage.class)));

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
}