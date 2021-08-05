package com.project.aas.ui.slideshow;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.project.aas.HomePage;
import com.project.aas.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsIndividual extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    public String verificationId;
    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase databaseReference;
    CircleImageView photo;
    EditText phoneNumberIndividual, Desc, editName, location, editEmail;
    Button submit;
    boolean verified = false;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private long backPressedTime;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_individual);
        mAuth = FirebaseAuth.getInstance();
        photo = findViewById(R.id.camera);
        editEmail = findViewById(R.id.EmailEdit);
        editName = findViewById(R.id.EdiNameIndividual);
        phoneNumberIndividual = findViewById(R.id.phoneNumberIndividual);
        Desc = findViewById(R.id.descIndividual);
        submit = findViewById(R.id.submit);
        location = findViewById(R.id.locationEdit);


        storageReference = FirebaseStorage.getInstance().getReference("details");

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagefrom();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneN = phoneNumberIndividual.getText().toString();
                String name = editName.getText().toString();


                String Location = location.getText().toString();
                if (TextUtils.isEmpty(phoneN) || TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(Location)) {
                    Toast.makeText(UserDetailsIndividual.this,
                            "Name, Location and Number are required", Toast.LENGTH_SHORT).show();
                } else if (phoneN.length() != 10) {
                    Toast.makeText(UserDetailsIndividual.this,
                            "Please enter a valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog pd = new ProgressDialog(UserDetailsIndividual.this);
                    pd.setMessage("Please Wait...\n" + "This may take a while");
                    pd.show();

                    if (imageUri != null) {
                        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                + "" + getFileExtensions(imageUri));

                        uploadTask = fileReference.putFile(imageUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull @NotNull Task task) throws Exception {
                                if (!task.isComplete()) {
                                    throw task.getException();
                                }
                                return fileReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    myUrl = downloadUri.toString();
                                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                    String name = firebaseUser.getUid();

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(name);

                                    //String postDetails = reference.push().getKey();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("UserImage", myUrl);
                                    hashMap.put("phoneNumber", phoneNumberIndividual.getText().toString());
                                    hashMap.put("desc", Desc.getText().toString());
                                    hashMap.put("name", editName.getText().toString());
                                    hashMap.put("email", editEmail.getText().toString());
                                    hashMap.put("type", "Individual");
                                    hashMap.put("location", location.getText().toString());
                                    hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    //assert postDetails != null;
                                    reference.updateChildren(hashMap);

                                    pd.dismiss();

                                    startActivity(new Intent(UserDetailsIndividual.this, HomePage.class));
                                } else {
                                    Toast.makeText(UserDetailsIndividual.this, "taskFailed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(UserDetailsIndividual.this, "" + e, Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        pd.dismiss();
                        Toast.makeText(UserDetailsIndividual.this, "Please select a profile picture",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


    private String getFileExtensions(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openImagefrom() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            photo.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(UserDetailsIndividual.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }


        backPressedTime = System.currentTimeMillis();

    }


}