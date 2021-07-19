package com.project.aas;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;



import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.project.aas.databinding.ActivityAdDetailBinding;

import com.project.aas.model.AdPost;
import com.project.aas.ui.CallDialog;
import com.project.aas.ui.SendWhatsappMessageDialog;

import java.util.ArrayList;
import java.util.List;


public class AdDetail extends AppCompatActivity {

    private ActivityAdDetailBinding binding;
    private String TAG = "AdDetail";
    private AdPost mAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mAd = (AdPost) getIntent().getSerializableExtra("AdObject");
        Log.i(TAG, "onCreate: Ad Rec : "+mAd.getPostedBy() +" | "+mAd.getPrice());

        initSlider();

        initAdDetails(mAd);
        binding.btnAdDetailBack.setOnClickListener(v -> onBackPressed());


        binding.btnCallSeller.setOnClickListener(v -> {

            Log.i(TAG, "onClick: Called " + mAd.getSellerPhone());
            CallDialog dialog = new CallDialog();
            dialog.setPhone(mAd.getSellerPhone());
            dialog.show(getSupportFragmentManager(), "call dialog");

        });

        binding.btnMakeOffer.setOnClickListener(v -> {
            SendWhatsappMessageDialog makeOfferDialog = new SendWhatsappMessageDialog();
            makeOfferDialog.setName(mAd.getPostedBy());
            makeOfferDialog.setPhone(mAd.getSellerWhatsapp());
            makeOfferDialog.show(getSupportFragmentManager(), "sendWhatsappMessageDialog dialog");
        });



    }


    private void initAdDetails(AdPost ad) {
        binding.tvAdDetailTitle.setText(ad.getTitle());
        binding.tvAdDetailLocation.setText(ad.getLocation());
        binding.adDetailPostedOn.setText("Posted On "+ad.getDatePosted());
        binding.adDetailSellerName.setText(ad.getPostedBy());
        binding.tvAdDetailPrice.setText("Rs. "+ad.getPrice());
        binding.tvAdDetailProductDescription.setText(ad.getDescription());

    }

    private void initSlider() {
        final ImageSlider imageSlider = findViewById(R.id.imageslider_ad_detail);
        List<SlideModel> slideModels = new ArrayList<>();
        for(String url : mAd.getImageUrls()) {
            slideModels.add(new SlideModel(url, ScaleTypes.CENTER_INSIDE));
        }
        imageSlider.setImageList(slideModels);
        imageSlider.startSliding(3000);
    }
}