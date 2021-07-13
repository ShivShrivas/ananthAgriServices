package com.project.aas.ui.slideshow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.aas.R;

public class Feedback extends AppCompatActivity {

    EditText name,feedback;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        name=findViewById(R.id.edit1);
        feedback=findViewById(R.id.edit2);
        send=findViewById(R.id.send_feedback);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/html");
                intent.putExtra(Intent.EXTRA_EMAIL,new String("xyz@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback From App");
                intent.putExtra(Intent.EXTRA_TEXT,"Name:"+name.getText()+"\n Message:"+feedback.getText());
                try {
                    startActivity(Intent.createChooser(intent,"Please select Email"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}