package com.project.aas.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.aas.ExampleDialog1;
import com.project.aas.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class
SignupActivity extends AppCompatActivity {

    EditText name, mobile, email, password, password2;
    TextInputEditText password1, password0;
    TextView terms;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ProgressDialog pd;
    TextView LoginUp, Loginbottom;
    CheckBox individual, Dealer, termscheckbox;
    private Button register;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        individual = findViewById(R.id.individual);
        Dealer = findViewById(R.id.Dealer);
        termscheckbox = findViewById(R.id.checkBox);

        password1 = findViewById(R.id.editTextTextPersonName5);
        password0 = findViewById(R.id.editTextTextPersonName6);

        LoginUp = findViewById(R.id.logintitle2);
        Loginbottom = findViewById(R.id.textView15);
        Loginbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
        LoginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));

            }
        });

        register = findViewById(R.id.register_user);
        name = findViewById(R.id.editTextTextPersonName3);
        //mobile=findViewById(R.id.mobile_signup);
        email = findViewById(R.id.editTextTextPersonName4);
        terms = findViewById(R.id.textView14);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(SignupActivity.this);
                pd.setMessage("Please Wait... \n" + "It may take a while");
                pd.show();

                String str_name = name.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password1.getText().toString();
                String str_password2 = password0.getText().toString();

                if (TextUtils.isEmpty(str_name)
                        || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)
                        || TextUtils.isEmpty(str_password2)) {
                    Toast.makeText(SignupActivity.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (str_password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "password must be" +
                                    " of minimum six characters",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (!str_password.equals(str_password2)) {
                    Toast.makeText(SignupActivity.this, "password not matching",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (!individual.isChecked() && !Dealer.isChecked()) {
                    Toast.makeText(SignupActivity.this, "please tell us if you are a dealer or an individual",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (!termscheckbox.isChecked()) {
                    Toast.makeText(SignupActivity.this, "please Accept the terms of Service",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (individual.isChecked() && Dealer.isChecked()) {
                    Toast.makeText(SignupActivity.this, "please Select only one option that if you're dealer or an individual",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    register(str_name, str_email, str_password);
                }
            }
        });
    }

    private void register(final String usernameName, final String email,
                          final String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            // assert firebaseUser != null;
                            String name = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference().child("Users").child(name);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            if (individual.isChecked()) {
                                hashMap.put("name", name);
                                hashMap.put("userName", usernameName.toLowerCase());
                                hashMap.put("userType", "Individual");
                                hashMap.put("userEmail", email);
                                hashMap.put("desc", "");
                                hashMap.put("UserImage", "");
                                hashMap.put("location", "");
                                hashMap.put("phoneNumber", "");
                                hashMap.put("publisher", "");
                            } else if (Dealer.isChecked()) {
                                hashMap.put("name", name);
                                hashMap.put("userName", usernameName.toLowerCase());
                                hashMap.put("userType", "Dealer");
                                hashMap.put("userEmail", email);
                                hashMap.put("desc", "");
                                hashMap.put("UserImage", "");
                                hashMap.put("location", "");
                                hashMap.put("phoneNumber", "");
                                hashMap.put("publisher", "");
                            }

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        if (individual.isChecked()) {
                                            Intent intent = new Intent(SignupActivity.this, UserDetailsActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else if (Dealer.isChecked()) {
                                            Intent intent = new Intent(SignupActivity.this, UserDetailsActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }

                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(SignupActivity.this, "Please register with a different Mail Id",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void openDialog() {
        ExampleDialog1 exampleDialog = new ExampleDialog1();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(SignupActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }


        backPressedTime = System.currentTimeMillis();

    }


}