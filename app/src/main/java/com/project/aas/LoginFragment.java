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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private Button login;
    EditText mobile,password;
    FirebaseAuth auth;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mobile=view.findViewById(R.id.phoneId);
        password=view.findViewById(R.id.password_login);

        auth= FirebaseAuth.getInstance();

        login=view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Please Wait...");
                pd.show();

                String str_mobile=mobile.getText().toString();
                String str_password= password.getText().toString();
                if(TextUtils.isEmpty(str_mobile)||TextUtils.isEmpty(str_password)){
                    Toast.makeText(getContext(),"All fields are required",
                            Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(str_mobile,str_password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                 if(task.isSuccessful()){
                                     DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                                             .child("Users").child(auth.getCurrentUser().getUid());

                                     reference.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                             pd.dismiss();
                                             Intent intent = new Intent(getContext(),HomePage.class);
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
                                     Toast.makeText(getContext(),"Please enter correct credentials",
                                             Toast.LENGTH_SHORT).show();
                                 }
                                }
                            });
                }
            }
        });

        return view;


    }
}