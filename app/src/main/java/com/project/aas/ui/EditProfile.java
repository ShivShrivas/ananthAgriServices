package com.project.aas.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.ui.slideshow.MyOrders;
import com.project.aas.ui.slideshow.SavedAds;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private static final int REQUEST_CODE=1;
    ImageButton back;
    Uri imageUri;
    private static int CHOOSE_IMAGE_CODE = 11;
    private String TAG = "EditProfileActivity";
    private ProgressDialog pd;
    ImageView imageView;
    Button save;
    FirebaseAuth auth;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imageView= findViewById(R.id.iv_display_picture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagefrom();
            }
        });

        back=findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this,HomePage.class));
            }
        });
        TextView uname=findViewById(R.id.et_name);
        TextView description=findViewById(R.id.et_description);
        TextView phone=findViewById(R.id.et_phone);
        TextView mail=findViewById(R.id.et_email);

        save=findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pd=new ProgressDialog(getApplicationContext());
//                pd.setMessage("Please Wait...");
//                pd.show();
                //
                //FirebaseUser firebaseUser = auth.getCurrentUser();
                String str_name=uname.getText().toString();
                String str_desc=description.getText().toString();
                String str_phone=phone.getText().toString();
                String str_email=mail.getText().toString();
                String name=FirebaseAuth.getInstance().getUid();
                DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                        .getReference().child("Users").child(name);
                //String postDetails = databaseReference.push().getKey();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", str_name);
//                //hashMap.put("userName", usernameName.toLowerCase());
                hashMap.put("userName",str_phone);
                //hashMap.put("",str_desc);
                hashMap.put("email", str_email);
                databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(EditProfile.this,HomePage.class));
                    }
                });
            }
        });
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");

        floatingActionButton = findViewById(R.id.add_ads);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, AddAds.class);
                startActivity(intent);
            }
        });

        bottomNavigationView=findViewById(R.id.bottomView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.edit_profilebo);
        bottomNavigationView.getMenu().getItem(4).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.homePage) {
                    Intent intent1 = new Intent(EditProfile.this, HomePage.class);
                    startActivity(intent1);
                }
                if (id == R.id.orders) {
                    startActivity(new Intent(EditProfile.this, MyOrders.class));
                }
                if(id==R.id.savedAds){
                    startActivity(new Intent(EditProfile.this, SavedAds.class));
                }
                return false;
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CHOOSE_IMAGE_CODE);
    }

    private void uploadProfilePicture(String uri) {
        // Need to create path
        String path = "profile_pictures/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString() +".jpeg";

        StorageReference profilePictureRef = FirebaseStorage
                .getInstance().getReference()
                .child(path);
        UploadTask uploadTask = profilePictureRef.putFile(Uri.parse(uri));

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return profilePictureRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.i(TAG, "onComplete: Uploaded successfully! URI -> "+ downloadUri );
                    Toast.makeText(getApplicationContext(),"Upload Success",Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onComplete: Encountered an error : "+task.getException().toString());
                }
                pd.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data !=null &&data.getData()!=null){
            imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    private void openImagefrom() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CODE);
    }
}