package com.project.aas.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class UserDetailsIndividual extends AppCompatActivity {

    Uri imageUri;
    String myUrl="";
    StorageTask uploadTask;
    StorageReference storageReference;
    private static final int REQUEST_CODE=1;
    private FirebaseUser firebaseUser;

    ImageView photo;
    EditText phoneNumber,Desc,editName,location;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_individual);

        photo=findViewById(R.id.imageView);
        phoneNumber=findViewById(R.id.EmailEdit);
        Desc=findViewById(R.id.editTextTextPersonName);
        submit=findViewById(R.id.submit);
        editName=findViewById(R.id.editName);
        location=findViewById(R.id.editTextTextPersonName2);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("details");

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagefrom();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(UserDetailsIndividual.this);
                pd.setMessage("Please Wait...");
                pd.show();

                if(imageUri!=null){
                    StorageReference fileReference= storageReference.child(System.currentTimeMillis()
                    +""+getFileExtensions(imageUri));

                    uploadTask=fileReference.putFile(imageUri);
                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull @NotNull Task task) throws Exception {
                            if(!task.isComplete()){
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri=task.getResult();
                                myUrl = downloadUri.toString();

                                String name = firebaseUser.getUid();

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(name);

                                String postDetails = reference.push().getKey();
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("UserImage",myUrl);
                                hashMap.put("phoneNumber",phoneNumber.getText().toString());
                                hashMap.put("Desc",Desc.getText().toString());
                                hashMap.put("username",editName.getText().toString());
                                hashMap.put("location",location.getText().toString());
                                hashMap.put("publisher",FirebaseAuth.getInstance().getCurrentUser().getUid());

                                assert postDetails != null;
                                reference.child(postDetails).setValue(hashMap);

                                pd.dismiss();

                                startActivity(new Intent(UserDetailsIndividual.this, HomePage.class));
                            }else{
                                Toast.makeText(UserDetailsIndividual.this,"taskFailed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(UserDetailsIndividual.this,""+e,Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }

    private String getFileExtensions(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap map=MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openImagefrom() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data !=null &&data.getData()!=null){
            imageUri=data.getData();
            photo.setImageURI(imageUri);
        }
    }

}