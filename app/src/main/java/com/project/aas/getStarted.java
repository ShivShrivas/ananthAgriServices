package com.project.aas;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.aas.ui.slideshow.UserDetailsActivity;
import com.project.aas.ui.slideshow.UserDetailsIndividual;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class getStarted extends AppCompatActivity {

    Button getStarted, IndividualBtn, DealerBtn;
    String firebaseUserNumber;
    FirebaseUser firebaseUser;
    FirebaseDatabase databaseReference;
    HashMap<String, String> data;
    private long backPressedTime;
    private String Individual, Dealer;
//    @Override
//    protected void onStart() {
//        super.onStart();
//        data=new HashMap<>();
//
//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//
//        if(firebaseUser!=null){
//            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
//            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
//            userReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
////                    data= (HashMap<String, String>) snapshot.getValue();
//                   // assert data != null;
//                    if (!snapshot.exists())
////                    Log.d(data.get("desc"),data.get("userType"));
////                    if(data.get("desc").equals("") && data.get("userType").equals("Individual")){
////                        startActivity(new Intent(getStarted.this,UserDetailsActivity.class));
////                    } else if (data.get("desc").equals("") && data.get("userType").equals("Dealer")){
////                        startActivity(new Intent(getStarted.this,UserDetailsActivity.class));
////                    }else
//                        {                        Toast.makeText(getStarted.this, "please login again...", Toast.LENGTH_SHORT).show();
//
//                    }
//                    else {
//                        startActivity(new Intent(getStarted.this, HomePage.class));
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//
//            finish();
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        IndividualBtn = findViewById(R.id.IndividualBtn);
        DealerBtn = findViewById(R.id.DealerBtn);
        DealerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getStarted.this, UserDetailsActivity.class));
            }
        });
        IndividualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getStarted.this, UserDetailsIndividual.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        startActivity(new Intent(getStarted.this, HomePage.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getStarted.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }


        backPressedTime = System.currentTimeMillis();

    }

}