package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aas.HomePage;
import com.project.aas.R;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    EditText mobile,password;
    private long backPressedTime;
    TextInputEditText passwordI;
    FirebaseAuth auth;
    TextView forgotPassword,signuptop,signupbottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth= FirebaseAuth.getInstance();

        signuptop=findViewById(R.id.textView2);
        signupbottom=findViewById(R.id.textView18);
        signupbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
        signuptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        mobile=findViewById(R.id.editTextTextPersonName);
        passwordI=findViewById(R.id.editTextTextPersonName2);
        forgotPassword=findViewById(R.id.textView7);

        login=findViewById(R.id.button3);
        login.setOnClickListener(v -> {
            ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please Wait...");
            pd.show();

            String str_mobile=mobile.getText().toString();
            String str_password= passwordI.getText().toString();
            if(TextUtils.isEmpty(str_mobile)||TextUtils.isEmpty(str_password)){
                Toast.makeText(LoginActivity.this,"All fields are required",
                        Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }else {
                auth.signInWithEmailAndPassword(str_mobile,str_password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if(task.isSuccessful()){
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(auth.getCurrentUser().getUid());

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        pd.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        pd.dismiss();
                                    }
                                });
                            }else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this,"Please enter correct credentials",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        forgotPassword.setOnClickListener(v -> {
            EditText resetmail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password");
            passwordResetDialog.setMessage("Enter your email to receive reset link");
            passwordResetDialog.setView(resetmail);

            passwordResetDialog.setPositiveButton("yes", (dialog, which) -> {
                String mail = resetmail.getText().toString();
                auth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> Toast.makeText(LoginActivity.this,"Reset link set to your email",
                        Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(LoginActivity.this,"Error! please try again"+e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            });
            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {

            });

            passwordResetDialog.create().show();
        });



    }
    @Override
    public void onBackPressed() {
        if(backPressedTime+2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(LoginActivity.this,"Press back again to exit",Toast.LENGTH_SHORT).show();
        }


        backPressedTime=System.currentTimeMillis();

    }

}