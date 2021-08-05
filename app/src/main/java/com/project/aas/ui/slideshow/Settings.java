package com.project.aas.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.aas.ExampleDialog;
import com.project.aas.R;
import com.project.aas.ui.home.Faq;

public class Settings extends AppCompatActivity {

    Switch notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notification = findViewById(R.id.switch1);
        if (notification.isChecked()) {
            Toast.makeText(Settings.this, "notifications off", Toast.LENGTH_SHORT).show();
        }

    }

    public void openfaq(View view) {
        Intent intent = new Intent(this, Faq.class);
        startActivity(intent);
    }

    public void logout(View view) {
        openDialog();

    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


}