package com.project.aas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.aas.model.UserProfile;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class SignupFragment extends Fragment {

    private Button register;
    EditText name,mobile,email,password,password2;

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ProgressDialog pd;



    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);


        register=view.findViewById(R.id.register_user);
        name=view.findViewById(R.id.name);
        mobile=view.findViewById(R.id.mobile_signup);
        email=view.findViewById(R.id.emailid_signup);
        password=view.findViewById(R.id.password_signup);
        password2=view.findViewById(R.id.password2);

        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(getContext());
                pd.setMessage("Please Wait...");
                pd.show();

                String str_name=name.getText().toString();
                String str_mobile=mobile.getText().toString();
                String str_email=email.getText().toString();
                String str_password=password.getText().toString();
                String str_password2 = password2.getText().toString();

                if(TextUtils.isEmpty(str_name)|| TextUtils.isEmpty(str_mobile)
                        ||TextUtils.isEmpty(str_email)||TextUtils.isEmpty(str_password)
                        ||TextUtils.isEmpty(str_password2)){
                    Toast.makeText(getContext(),"All fields are required",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }else if(str_password.length()<6){
                    Toast.makeText(getContext(),"password must be" +
                                    " of minimum six characters",
                            Toast.LENGTH_SHORT).show();
                }else if (!str_password.equals(str_password2)){
                    Toast.makeText(getContext(),"password not matching",
                            Toast.LENGTH_SHORT).show();
                }else{
                    register(str_name,str_mobile,str_email,str_password);
                }

            }
        });
        return view;
    }

    private void register(final String usernameName,final String mobile,final String email,
                          final String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            // assert firebaseUser != null;
                            String name = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference().child("Users").child("name");

//                            UserProfile user = new UserProfile(name,usernameName.toLowerCase(),mobile,
//                                    email,"",null,null,null);
//
//                            updateDatabase(user);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", name);
                            hashMap.put("userName", usernameName.toLowerCase());
                            hashMap.put("mobile", mobile);
                            hashMap.put("email", email);

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(getContext(), HomePage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(getContext(), "You can't register with this phone number or password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

       }

       private void updateDatabase(UserProfile user) {
           DatabaseReference dbref = FirebaseDatabase.getInstance()
                   .getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
           dbref.setValue(user);
       }


    }