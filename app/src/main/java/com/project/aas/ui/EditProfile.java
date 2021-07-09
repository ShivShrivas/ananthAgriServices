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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.aas.HomePage;
import com.project.aas.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditProfile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton back;
    private static int CHOOSE_IMAGE_CODE = 11;
    private String TAG = "EditProfileActivity";
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back=findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this,HomePage.class));
            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");




        ImageButton btn = findViewById(R.id.btn_upload_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        bottomNavigationView=findViewById(R.id.bottomView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.edit_profilebo);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homePage:
                        Intent intent1 = new Intent(EditProfile.this, HomePage.class);
                        startActivity(intent1);
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
        // Later 'image.jpeg' will be replaced by uid followed by .jpeg

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
        if(resultCode == RESULT_OK) {
            if(requestCode == CHOOSE_IMAGE_CODE) {
                // Image successfully selected for uploading
                String uri = data.getData().toString();
                Log.i(TAG, "onActivityResult: "+uri);
                pd.show();
                uploadProfilePicture(uri);
            }
        }
    }


}