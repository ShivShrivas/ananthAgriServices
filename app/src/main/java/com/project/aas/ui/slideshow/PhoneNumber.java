package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.aas.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PhoneNumber extends AppCompatActivity {

    Button send_otp;
    EditText phoneNumber;
    private String TAG = "PhoneNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        send_otp=findViewById(R.id.verify);

        phoneNumber=findViewById(R.id.phoneNumber);
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(!phoneNumber.getText().toString().trim().isEmpty())
                {if(phoneNumber.getText().toString().length()==10){
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"
                                    + phoneNumber.getText().toString(), 100, TimeUnit.SECONDS,
                            PhoneNumber.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                    Intent intent = new Intent(PhoneNumber.this,OtpVerification.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                    Log.i(TAG, "onVerificationFailed: " + e.getMessage());
                                    Toast.makeText(PhoneNumber.this,e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                                    Intent intent = new Intent(PhoneNumber.this,OtpVerification.class);
                                    intent.putExtra("mobile",phoneNumber.getText().toString());
                                    intent.putExtra("backentOtp",s);
                                    startActivity(intent);
                                }
                            });


                }else {
                    Toast.makeText(PhoneNumber.this,"enter a valid 10 digit phone number",Toast.LENGTH_SHORT).show();
                }
                }else {
                    Toast.makeText(PhoneNumber.this,"enter a phone number",Toast.LENGTH_SHORT).show();
                }

            }*/
                startActivity(new Intent(PhoneNumber.this,OtpVerification.class));
            }
        });

    }


}