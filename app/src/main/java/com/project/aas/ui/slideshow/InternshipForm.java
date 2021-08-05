package com.project.aas.ui.slideshow;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.aas.R;

public class InternshipForm extends AppCompatActivity {

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internship_form);

        submit = findViewById(R.id.submitInternshipForm);
        submit.setOnClickListener(v -> {

        });
    }
}