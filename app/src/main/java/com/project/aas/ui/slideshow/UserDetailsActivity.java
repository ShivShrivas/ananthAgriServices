package com.project.aas.ui.slideshow;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.PhoneAuthProvider;
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

public class UserDetailsActivity extends AppCompatActivity {

    public static final int REQUEST_CODEE = 1;
    private static final int REQUEST_CODE = 1;
    FirebaseStorage storage;
    Uri imageUri, UriL, UriA, UriS, UriP;
    String myUrl = "";
    CircleImageView photo;
    int counter;
    StorageTask uploadTask;
    StorageReference storageReference;
    ImageView license, shopPhoto, aadhar;
    EditText phoneNumber, Desc, editName, location, mail, shopName, GSTIN, AdharNumber;
    Button submitD;
    boolean verified = false;
    private long backPressedTime;
    private FirebaseUser firebaseUser;


    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        storageReference = FirebaseStorage.getInstance().getReference("details");
        mAuth = FirebaseAuth.getInstance();

        license = findViewById(R.id.imageView6);

        shopPhoto = findViewById(R.id.imageView5);
        aadhar = findViewById(R.id.imageView8);


        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 2;
                openImagefrom();
            }
        });
        shopPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 1;
                openImagefrom();
            }
        });
        aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 3;
                openImagefrom();
            }
        });
        photo = findViewById(R.id.camera);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 0;
                openImagefrom();
            }
        });

        phoneNumber = findViewById(R.id.phoneNumberdealer);
        editName = findViewById(R.id.EdiNameDealer);
        location = findViewById(R.id.locationEdit);
        Desc = findViewById(R.id.descdealer);
        shopName = findViewById(R.id.editTextTextPersonName3);
        GSTIN = findViewById(R.id.editTextPhone);
        AdharNumber = findViewById(R.id.editTextPhone2);

        submitD = findViewById(R.id.submitD);


        submitD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (license != null && shopName != null && GSTIN != null && shopPhoto != null &&
                        location != null && AdharNumber != null && Desc != null &&
                        phoneNumber != null && photo != null && aadhar != null) {
                    ProgressDialog pd = new ProgressDialog(UserDetailsActivity.this);
                    pd.setMessage("Please Wait...\n" + "This may take a while");
                    pd.show();

                    if (UriP != null) {
                        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                + "" + getFileExtensions(UriP));

                        uploadTask = fileReference.putFile(UriP);
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
                                    hashMap.put("name", editName.getText().toString());
                                    hashMap.put("email", mail.getText().toString());
                                    hashMap.put("phoneNumber", phoneNumber.getText().toString());
                                    hashMap.put("desc", Desc.getText().toString());
                                    hashMap.put("shopName", shopName.getText().toString());
                                    hashMap.put("GSTIN", GSTIN.getText().toString());
                                    hashMap.put("Aadhar", AdharNumber.getText().toString());
                                    hashMap.put("location", location.getText().toString());
                                    hashMap.put("type", "Dealer");
                                    hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

//                                    assert postDetails != null;
                                    reference.updateChildren(hashMap);
                                    Toast.makeText(UserDetailsActivity.this, "table creates", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();

                                    startActivity(new Intent(UserDetailsActivity.this, HomePage.class));
                                } else {
                                    Toast.makeText(UserDetailsActivity.this, "TaskFailed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(UserDetailsActivity.this, "" + e, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    if (UriS != null) {
                        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                + "" + getFileExtensions(UriS));
                        uploadTask = fileReference.putFile(UriP);
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

                                    String postDetails = reference.push().getKey();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("ShopPhoto", myUrl);

                                    assert postDetails != null;
                                    reference.child(postDetails).setValue(hashMap);
                                    pd.dismiss();
                                    startActivity(new Intent(UserDetailsActivity.this, HomePage.class));
                                } else {
                                    Toast.makeText(UserDetailsActivity.this, "TaskFailed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(UserDetailsActivity.this, "" + e, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    if (UriL != null) {
                        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                + "" + getFileExtensions(UriL));
                        uploadTask = fileReference.putFile(UriL);
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

                                    String postDetails = reference.push().getKey();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("License", myUrl);

                                    assert postDetails != null;
                                    reference.child(postDetails).setValue(hashMap);
                                    pd.dismiss();
                                    startActivity(new Intent(UserDetailsActivity.this, HomePage.class));
                                } else {
                                    Toast.makeText(UserDetailsActivity.this, "taskFailed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(UserDetailsActivity.this, "" + e, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    if (UriA != null) {
                        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                + "" + getFileExtensions(UriA));
                        uploadTask = fileReference.putFile(UriA);
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

                                    String postDetails = reference.push().getKey();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("Aadhar", myUrl);

                                    assert postDetails != null;
                                    reference.child(postDetails).setValue(hashMap);
                                    pd.dismiss();
                                    startActivity(new Intent(UserDetailsActivity.this, HomePage.class));
                                } else {
                                    Toast.makeText(UserDetailsActivity.this, "taskFailed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(UserDetailsActivity.this, "" + e, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                } else {
                    Toast.makeText(UserDetailsActivity.this,
                            "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            if (counter == 1) {
                shopPhoto.setImageURI(imageUri);
                UriS = imageUri;
            } else if (counter == 2) {
                license.setImageURI(imageUri);
                UriL = imageUri;
            } else if (counter == 3) {
                aadhar.setImageURI(imageUri);
                UriA = imageUri;
            } else if (counter == 0) {
                photo.setImageURI(imageUri);
                UriP = imageUri;
            }
        }
    }

    private String getFileExtensions(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(UserDetailsActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }


        backPressedTime = System.currentTimeMillis();

    }


}