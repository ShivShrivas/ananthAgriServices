package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.aas.HomePage;
import com.project.aas.R;

import org.jetbrains.annotations.NotNull;

public class OtpVerification extends AppCompatActivity implements TextWatcher {

    EditText ed1,ed2,ed3,ed4,ed5,ed6;
    String getOtpBackend;
    Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        verifyButton=findViewById(R.id.otpVerify);

        ed1=findViewById(R.id.ed1);
        ed2=findViewById(R.id.ed2);
        ed3=findViewById(R.id.ed3);
        ed4=findViewById(R.id.ed4);
        ed5=findViewById(R.id.ed5);
        ed6=findViewById(R.id.ed6);
        ed1.addTextChangedListener( this);
        ed2.addTextChangedListener( this);
        ed3.addTextChangedListener( this);
        ed4.addTextChangedListener( this);
        ed5.addTextChangedListener(this);
        ed6.addTextChangedListener(this);

        getOtpBackend = getIntent().getStringExtra("backentOtp");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(!ed1.getText().toString().trim().isEmpty()&&
                        !ed2.getText().toString().trim().isEmpty()&&
                        !ed3.getText().toString().trim().isEmpty()&&
                        !ed4.getText().toString().trim().isEmpty()&&
                        !ed5.getText().toString().trim().isEmpty()&&
                        !ed6.getText().toString().trim().isEmpty()){
                    String codeOTP = ed1.getText().toString()+
                            ed2.getText().toString()+
                            ed3.getText().toString()+
                            ed4.getText().toString()+
                            ed5.getText().toString()+
                            ed6.getText().toString();
                    if(getOtpBackend!=null){
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getOtpBackend,codeOTP
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                           Intent intent = new Intent(OtpVerification.this,UserDetailsActivity.class);
                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                           startActivity(intent);

                                           // startActivity(new Intent(OtpVerification.this, HomePage.class));
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(OtpVerification.this,"check internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                }*/startActivity(new Intent(OtpVerification.this,HomePage.class));
            }
        });

    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()==1){
            if(ed1.length()==1){
                ed2.requestFocus();
            }
            if(ed2.length()==1){
                ed3.requestFocus();
            }
            if(ed3.length()==1){
                ed4.requestFocus();
            }
            if(ed4.length()==1) {
                ed5.requestFocus();
            }
            if(ed5.length()==1) {
                ed6.requestFocus();
            }
        }else if(s.length()==0){
            if(ed6.length()==0){
                ed5.requestFocus();
            }
            if(ed5.length()==0){
                ed4.requestFocus();
            }
            if(ed4.length()==0){
                ed3.requestFocus();
            }
            if(ed3.length()==0){
                ed2.requestFocus();
            }if(ed2.length()==0){
                ed1.requestFocus();
            }
        }

    }
}