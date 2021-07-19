package com.project.aas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.aas.ui.slideshow.LoginActivity;
import com.project.aas.ui.slideshow.PhoneNumber;
import com.project.aas.ui.slideshow.UserDetailsActivity;

public class getStarted extends AppCompatActivity {

    Button getStarted;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
       super.onStart();

       firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if( firebaseUser != null) {
            startActivity(new Intent(getStarted.this, HomePage.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        getStarted=findViewById(R.id.getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.project.aas.getStarted.this, LoginActivity.class));
            }
        });
    }
}