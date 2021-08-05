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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.aas.R;
import com.project.aas.databinding.ActivityAddAdsBinding;
import com.project.aas.model.AdPost;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddAds extends AppCompatActivity {
    int count=0;
    private String userName;
    private ActivityAddAdsBinding binding;
    private EditText mTitle, mPrice, mPinCode,
            mDescription, mVideoUrl,mAddress,
            mPhoneNumber, mWhatsappNumber;

    private AppCompatButton mSubmitBtn;
    private CheckBox mCheckBox;
    private Spinner mPriceType, mPriceUnit, mAdType,mCategory, mSubCategory;
    private String title,price,pin,desc,address,phone,whatsapp,adType,adCategory,adSubCategory,videoUrl,priceType,priceUnit;
    private String TAG = "AddAdsActivity";
    private FirebaseDatabase mDatabaseReference;
    private static int CHOOSE_IMAGE_CODE = 17;
    private ProgressDialog pd;
    private List<String> mAdImageUrls;
    private List<String> mAdImageUris;
    private FirebaseUser firebaseUser;
    private String numberOfAds;
    private String StringOfAds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        init();
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        String user= FirebaseAuth.getInstance().getUid();
        DatabaseReference userId=FirebaseDatabase.getInstance().getReference().child("Users").child(user);
        userId.child("userName").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue().toString();
                Log.d("userName", userName);
            }
        });

        /****************************
         * CodePiece for Number of ads
         ****************************/
        userId.child("numberOfAds").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                numberOfAds=dataSnapshot.getValue().toString();
                Log.i("numberOfAds",numberOfAds);

            }
        });

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
                || adCategory.equals("") || adType.equals("") || adSubCategory.equals("")
                || priceUnit.equals("") || priceType.equals("")) {
            Toast.makeText(this, "Fields marked with * are mandatory. Make sure to fill them.", Toast.LENGTH_SHORT).show();
        } else {
            if(mAdImageUrls.size() == 0) {
                Toast.makeText(this, "You need to upload atleast ONE image of the product.", Toast.LENGTH_SHORT).show();
            }else if(whatsapp.length() != 10||phone.length()!=10){
                Toast.makeText(this, "Please give a valid number", Toast.LENGTH_SHORT).show();

            }
            else {
                if(mCheckBox.isChecked()) {
                    Log.i(TAG, "onSubmit: Activated");
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
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Calendar.getInstance().getTime());
            //String path = "ads/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+"/img"+i +".jpeg";
            String path = "ads/testUser/"+timeStamp +".jpeg";
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
        Uri imgUri;

        if(resultCode == RESULT_OK) {
            if(requestCode == CHOOSE_IMAGE_CODE) {
                ImageSlider imageSlider = findViewById(R.id.iv_ad_image);
                if(data.getClipData() == null) {
                    Log.i(TAG, "onActivityResult: Single Image Uri captured : " + data.getData().toString());
                    imgUri=data.getData();
                    ImageView setImage=findViewById(R.id.iv_add_images);
                    setImage.setImageURI(imgUri);
                    count+=1;
                    Toast.makeText(this, count+" image(s) uploaded!!", Toast.LENGTH_SHORT).show();
                    ImageView imgview=findViewById(R.id.imageView2);
                    imgview.setVisibility(View.GONE);
                    TextView tv=findViewById(R.id.textView6);
                    tv.setVisibility(View.GONE);
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

    private void initAdTypeSpinner() {
        String [] adTypes = {"Sell",
                "Rent"};
        ArrayAdapter adTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, adTypes);
        adTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
    }

    private void initCategorySpinner() {
        String [] categoryTypes = {"Farm machinery and Tools",
                "Fertilizers and chemicals",
                "Seeds and Nursery",
                "Other services"};

        ArrayAdapter categoryTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, categoryTypes);
        categoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
    }

    private void initSubCategorySpinner() {
        String [] subCategoryTypes = {"Agri labours",
                "Tools and Implements",
                "Agricultural Machineries",
                "Borewells",
                "Harvesters",
                "JCB",
                "Sprayers",
                "Tractors",
                "Transplanters",
                "Water tankers",
                "Transporting vehicles",
                "Other services"};

        ArrayAdapter subCategoryTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, subCategoryTypes);
        subCategoryTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    private void initPriceTypeSpinner() {
        String [] priceTypes = {"Fixed",
                "Negotiable",
                "On Call"
        };
        ArrayAdapter priceTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, priceTypes);
        priceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPriceType.setAdapter(priceTypeAdapter);
        mPriceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priceType = priceTypes[position];
                Log.i(TAG, "onItemSelected: Selecting" + adType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddAds.this, "Mandatory Field.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPriceUnitSpinner() {
        String [] priceUnits = {"Per Hour",
                "Per Day",
                "Per Week",
                "Per Month",
                "Kilo meters (KM)","Acres",
                "Hectares",
                "Quintals",
                "Tons",
                "Bags",
                "Kilograms (kg)",
                "Litres",
                "Tray",
                "Grams"};

        ArrayAdapter priceUnitAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, priceUnits);
        priceUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPriceUnit.setAdapter(priceUnitAdapter);
        mPriceUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priceUnit = priceUnits[position];
                Log.i(TAG, "onItemSelected: Selecting" + priceUnit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddAds.this, "Mandatory Field.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinners() {
        initAdTypeSpinner();
        initCategorySpinner();
        initSubCategorySpinner();
        initPriceTypeSpinner();
        initPriceUnitSpinner();
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
        Log.i("userName now",(String)userName);
        ad.setPostedBy((String) userName);

//        Log.d("userName",(String) userName);
//        Log.d("userName", "THisIIERFEI eiro ne"+(String)userName);
//        ad.setPostedBy((String) userName);
        //ad.setPostedBy("userName");
        ad.setImageUrls(mAdImageUrls);
        int numb=Integer.parseInt(numberOfAds);
        if(numb<5){
            String name = firebaseUser.getUid();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
            numb+=1;
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("numberOfAds",numb);
            Log.d("numb",""+numb);
            reference.updateChildren(hashMap);



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
        }else{
            pd.dismiss();
            Toast.makeText(this, "Limit of ads exceed!! Please upgrade your subscription...", Toast.LENGTH_SHORT).show();
        }




    }
}