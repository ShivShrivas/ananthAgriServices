package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.project.aas.HomePage;
import com.project.aas.R;

public class ContactUs extends AppCompatActivity {
    TextView backt;

    ImageView phone,chat,copybuttonone, copybuttontwo,back;
    private static final int REQUEST_CALL=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        chat=findViewById(R.id.chatwhat);
        chat.setOnClickListener(v -> openDialog());

        phone=findViewById(R.id.phonecall);
        phone.setOnClickListener(v -> makePhoneCall());
        copybuttonone=findViewById(R.id.copyone);
        copybuttonone.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copybuttoneone", "9704218219");
            clipboard.setPrimaryClip(clip);
            clip.getDescription();

            Toast.makeText(ContactUs.this, "number copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(ContactUs.this, HomePage.class)));
        backt=findViewById(R.id.backkk);
        backt.setOnClickListener(v -> startActivity(new Intent(ContactUs.this, HomePage.class)));

        copybuttontwo=findViewById(R.id.copytwo);
        copybuttontwo.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copybuttontwo", "ananthagriservices@gmail.com");
            clipboard.setPrimaryClip(clip);
            clip.getDescription();

            Toast.makeText(ContactUs.this, "number copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }
    public void makePhoneCall(){
        String number = "9704218219";
        if(ContextCompat.checkSelfPermission(ContactUs.this,
                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactUs.this,
                    new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else{
            String dial = "tel:" +number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(ContactUs.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openDialog(){
        ExampleeDialog exampleDialog = new ExampleeDialog();
        exampleDialog.show(getSupportFragmentManager(),"example dialog");
    }

}