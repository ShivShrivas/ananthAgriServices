package com.project.aas.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.aas.databinding.ActivityAddAdsBinding;
import com.project.aas.model.AdPost;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAds extends AppCompatActivity {

    private ActivityAddAdsBinding binding;
    private EditText mTitle, mPrice, mPinCode,
            mDescription, mVideoUrl,mAddress,
            mPhoneNumber, mWhatsappNumber;

    private AppCompatButton mSubmitBtn;
    private CheckBox mCheckBox;
    private Spinner mPriceType, mPriceUnit, mAdType,mCategory, mSubCategory;
    private String title,price,pin,desc,address,phone,whatsapp,adType,adCategory,adSubCategory,videoUrl;
    private String TAG = "AddAdsActivity";
    private FirebaseDatabase mDatabaseReference;
    private static int CHOOSE_IMAGE_CODE = 17;
    private ProgressDialog pd;
    private List<String> mAdImageUrls;
    private List<String> mAdImageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");

        binding.btnBackPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        binding.ivAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void onSubmit() {
        title = mTitle.getText().toString();
        price = mPrice.getText().toString();
        pin = mPinCode.getText().toString();
        desc = mDescription.getText().toString();
        address = mAddress.getText().toString();
        phone = mPhoneNumber.getText().toString();
        whatsapp = mWhatsappNumber.getText().toString();

        videoUrl = mVideoUrl.getText().toString();

        if(title.equals("") || price.equals("") || pin.equals("") || desc.equals("")
                || address.equals("") || phone.equals("") || whatsapp.equals("")
                || adCategory.equals("") || adType.equals("") || adSubCategory.equals("")) {
            Toast.makeText(this, "Fields marked with * are mandatory. Make sure to fill them.", Toast.LENGTH_SHORT).show();
        }else {
            if(mAdImageUrls.size() == 0) {
                Toast.makeText(this, "You need to upload atleast ONE image of the product.", Toast.LENGTH_SHORT).show();
            }else {
                if(mCheckBox.isActivated()) {
                    AdPost mNewAd = createPost();
                    uploadAd(mNewAd);
                }else {
                    Toast.makeText(this, "Agree to our terms and conditions to proceed.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CHOOSE_IMAGE_CODE);
    }

    private void uploadAdImages() {
        for(int i=0;i<mAdImageUris.size();i++) {
            if(i==10) break;
            pd.show();
            //String path = "ads/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+"/img"+i +".jpeg";
            String path = "ads/testUser/img"+i +".jpeg";
            StorageReference adPictureRef = FirebaseStorage
                    .getInstance().getReference()
                    .child(path);
            String uri = mAdImageUris.get(i);
            UploadTask uploadTask = adPictureRef.putFile(Uri.parse(uri));

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return adPictureRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mAdImageUrls.add(downloadUri.toString());
                        Log.i(TAG, "onComplete: Uploaded successfully! Ad uri -> "+ downloadUri );

                    } else {
                        Log.i(TAG, "onComplete: Encountered an error : "+task.getException().toString());
                    }
                    pd.dismiss();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == CHOOSE_IMAGE_CODE) {
                if(data.getClipData() == null) {
                    Log.i(TAG, "onActivityResult: Single Image Uri captured : " + data.getData().toString());
                    mAdImageUris.add(data.getData().toString());
                }else {
                    for(int i=0; i<data.getClipData().getItemCount(); i++) {
                        mAdImageUris.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }

                uploadAdImages();
            }
        }
    }

    private AdPost createPost() {
        AdPost newAd = new AdPost();
        newAd.setTitle(title);
        newAd.setPrice(price);
        newAd.setPinCode(pin);
        newAd.setDescription(desc);
        newAd.setLocation(address);
        newAd.setSellerPhone(phone);
        newAd.setSellerWhatsapp(whatsapp);
        newAd.setAdType(adType);
        newAd.setCategory(adCategory);
        newAd.setSubCategory(adSubCategory);
        newAd.setImageUrls(mAdImageUrls);

        return newAd;
    }

    private void init() {
        title= "";
        price= "";
        pin= "";
        desc= "";
        address= "";
        phone= "";
        whatsapp= "";
        adType= "";
        adCategory= "";
        adSubCategory= "";

        mAdImageUrls = new ArrayList<>();
        mAdImageUris = new ArrayList<>();



        mTitle = binding.etNewAdTitle;
        mPrice = binding.etNewAdPrice;
        mPinCode = binding.etNewAdPinCode;
        mDescription = binding.etNewAdDescription;
        mVideoUrl = binding.etNewAdVideoUrl;
        mAddress = binding.etNewAdAddress;
        mPhoneNumber = binding.etNewAdPhoneNum;
        mWhatsappNumber = binding.etNewAdWhatsappNum;

        mSubmitBtn = binding.btnPostAd;
        mCheckBox = binding.checkboxAgree;
        mPriceType = binding.spinnerPriceType;
        mPriceUnit = binding.spinnerPriceUnit;
        mSubCategory = binding.spinnerSubCategory;
        mCategory = binding.spinnerCategory;
        mAdType = binding.spinnerAdType;

        initSpinners();

    }

    private void initSpinners() {
        String [] adTypes = {"Sell","Rent"};
        String [] subCategoryTypes = {"Agri Labours","Agricultural implements","Agricultural machineries"};
        String [] categoryTypes = {"Farming Machinery and Tools","Fertilizers and Chemicals","Seeds and Nursery","Other Products and Services"};
        ArrayAdapter adTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, adTypes);
        ArrayAdapter categoryTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, categoryTypes);
        ArrayAdapter subCategoryTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, subCategoryTypes);

        adTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCategory.setAdapter(categoryTypeAdapter);
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adCategory = categoryTypes[position];
                Log.i(TAG, "onItemSelected: Selecting" + adCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddAds.this, "Mandatory Field.", Toast.LENGTH_SHORT).show();
            }
        });

        mAdType.setAdapter(adTypeAdapter);
        mAdType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adType = adTypes[position];
                Log.i(TAG, "onItemSelected: Selecting" + adType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddAds.this, "Mandatory Field.", Toast.LENGTH_SHORT).show();
            }
        });

        mSubCategory.setAdapter(subCategoryTypeAdapter);
        mSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adSubCategory = subCategoryTypes[position];
                Log.i(TAG, "onItemSelected: Selecting" + adSubCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddAds.this, "Mandatory Field.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAd(AdPost ad) {
        Log.i(TAG, "uploadAd: Started");
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.show();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Ads");
        String key = dbref.push().getKey();
        ad.setId(key);
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        ad.setDatePosted(timeStamp);
        // ad.setPostedBy(FirebaseAuth.getInstance().getUid());
        ad.setPostedBy("test seller");
        ad.setImageUrls(mAdImageUrls);

        dbref.child(key).setValue(ad).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Ad posted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}