package com.project.aas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aas.databinding.ActivityAdDetailBinding;
import com.project.aas.model.AdPost;
import com.project.aas.model.AdReview;
import com.project.aas.model.Review;
import com.project.aas.ui.CallDialog;
import com.project.aas.ui.SendWhatsappMessageDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdDetail extends AppCompatActivity {

    private androidx.appcompat.widget.AppCompatButton callBtn;
    private androidx.appcompat.widget.AppCompatButton makeOfferBtn;
    private ActivityAdDetailBinding binding;
    private String TAG = "AdDetail";
    private AdPost mAd;
    private AdReview mAdReview;
    FirebaseUser firebaseUser;
    ImageButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        save=findViewById(R.id.btn_save_ad);

        mAd = (AdPost) getIntent().getSerializableExtra("AdObject");
        Log.i(TAG, "onCreate: Ad Rec : "+mAd.getPostedBy() +" | "+mAd.getPrice());
        postReview(4);
        getReview(mAd.getId());
        initSlider();

        initAdDetails(mAd);
        binding.btnAdDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.btnCallSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: Called " + mAd.getSellerPhone());
                CallDialog dialog = new CallDialog();
                dialog.setPhone(mAd.getSellerPhone());
                dialog.show(getSupportFragmentManager(), "call dialog");

            }
        });

        binding.btnMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendWhatsappMessageDialog makeOfferDialog = new SendWhatsappMessageDialog();
                makeOfferDialog.setName(mAd.getPostedBy());
                makeOfferDialog.setPhone(mAd.getSellerWhatsapp());
                makeOfferDialog.show(getSupportFragmentManager(), "sendWhatsappMessageDialog dialog");
            }
        });

        binding.btnSaveAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.btnSaveAd.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid())
                            .child(mAd.getId()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid())
                            .child(mAd.getId()).removeValue();
                }
            }
        });

        binding.imageButtonMoreReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdReviews.class);
                intent.putExtra("ReviewObject",mAdReview);
                startActivity(intent);
            }
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

    private void getReview(String adId) {
        FirebaseDatabase.getInstance()
                .getReference().child("Reviews")
                .child(adId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() != null) {
                        Log.i(TAG, "onComplete: found rating");
                        mAdReview = task.getResult().getValue(AdReview.class);
                        double stars = Double.parseDouble(mAdReview.getNumStars());
                        double votes = Double.parseDouble(mAdReview.getNumVotes());
                        double totalVotes = votes * 5;
                        float rating = (float) ((stars / totalVotes) * 5);
                        binding.adRatingBar.setRating(rating);
                    }
                }
            }
        });
    }


    private void postReview(double val) {
        Review review = new Review(val,"TestReviewer","This is a great product.","testauthorid");
        String key = mAd.getId();

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Reviews").child(key);
        dbref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    AdReview newReview = new AdReview();
                    if(task.getResult().getValue() != null) {
                        Log.i(TAG, "onComplete: Null Review");
                        AdReview res = task.getResult().getValue(AdReview.class);
                        double stars = Double.parseDouble(res.getNumStars()) + val;
                        double votes = Double.parseDouble(res.getNumVotes()) + 1;
                        double totalVotes = votes*5;
                        newReview.setRatingValue((stars/totalVotes)*5);
                        newReview.setNumStars(""+stars);
                        newReview.setNumVotes(""+votes);

                        List<Review> oldReviews = res.getReviewList();
                        oldReviews.add(review);
                        newReview.setReviewList(oldReviews);

                    }else {
                        newReview = new AdReview(val,String.valueOf(val),String.valueOf(1));
                        List<Review> list = new ArrayList<>();
                        list.add(review);
                        newReview.setReviewList(list);
                        Log.i(TAG, "onComplete: new Review");
                    }

                    dbref.setValue(newReview);

                }
                Log.i(TAG, "onComplete: "+task.toString());
            }
        });

//        AdReview obj = new AdReview();

                // Code below adds new review to the list of reviews.

    }
}