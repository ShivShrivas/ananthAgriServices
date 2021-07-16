package com.project.aas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.aas.ui.AddAds;
import com.project.aas.ui.slideshow.PhoneNumber;

public class getStarted extends AppCompatActivity {

    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        getStarted=findViewById(R.id.getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(com.project.aas.getStarted.this, PhoneNumber.class));
                startActivity(new Intent(com.project.aas.getStarted.this, HomePage.class));
            }
        });
    }
}